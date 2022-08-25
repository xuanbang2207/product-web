package com.home.controller.admin;



import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.home.domain.Category;
import com.home.domain.Customer;
import com.home.domain.Order;
import com.home.domain.OrderDetail;
import com.home.model.OrderDto;
import com.home.service.OrderDetailService;
import com.home.service.OrderService;
import com.home.service.OrderService;

@Controller
@RequestMapping("admin/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @GetMapping("edit/{orderId}")
    public ModelAndView eidt(@PathVariable("orderId") Long orderId, Model model) {
	
	Optional<Order> opt = orderService.findById(orderId);
	OrderDto dto  = new OrderDto();
	
	if (opt.isPresent()) {
	    Order entity = opt.get();
	    
	    BeanUtils.copyProperties(entity, dto);
	    dto.setIsEdit(true);
	    model.addAttribute("order", dto);
	    List<OrderDetail> list = orderDetailService.findByOrderOrderId(entity.getOrderId());
	    model.addAttribute("details",list);
	    return new ModelAndView( "admin/orders/edit");
	}
	
	model.addAttribute("messgae", "order không tồn tại");
	return new ModelAndView( "redidect:/admin/orders");
	
    }

    @GetMapping("delete/{orderId}")
    public ModelAndView delete(ModelMap model, @PathVariable("orderId") Long orderId) {
    	Optional<Order> opt = orderService.findById(orderId);
    if (opt.isPresent()) {
    	orderService.deleteById(orderId);
    	model.addAttribute("message", "Order đã được xóa");
	}	
    model.addAttribute("message", "Order đã không được tìm thấy");
	
	return new ModelAndView("forward:/admin/orders", model);
    }

    @PostMapping("update")
    public String update(ModelMap model, @Valid @ModelAttribute("order") OrderDto dto,
	    BindingResult result) {

	if (result.hasErrors()) {
	    return "admin/orders/edit";
	}

	Order entity = new Order();
	BeanUtils.copyProperties(dto, entity);
	

	Customer customer = new Customer();
	customer.setCustomerId(dto.getCustomerId());
	entity.setCustomer(customer);
	orderService.save(entity);
	
	model.addAttribute("message", "order sửa thành công");

	return "/admin/orders/edit";
    }

    @GetMapping("")
    public String list(Model model) {
	List<Order> list = orderService.findAll(Sort.by(Direction.DESC,"orderDate"));

	model.addAttribute("orders", list);

	return "admin/orders/list";
    }

    @GetMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
	List<Order> list = null;
	if (StringUtils.hasText(name)) {
	    list = orderService.findByNameContaining(name);

	} else {
	    list = orderService.findAll();
	}

	model.addAttribute("orders", list);
	return "admin/orders/search";
    }
//
//    @GetMapping("pagniate")
//    public String page(ModelMap model,
//    		@RequestParam() 
//    		@RequestParam(defaultValue = "0") Integer PageNo,
//    		@RequestParam(defaultValue = "10") Integer pageSize,
//    		@RequestParam(defaultValue = "name") String sort) {
//	Pageable pageable = PageRequest.of(PageNo, pageSize,Sort.by(sort));
//
//	Page<order> pages = orderService.findAll(pageable);
//
//	model.addAttribute("pages", pages);
//
//	return "admin/orders/page";
//    }
}
