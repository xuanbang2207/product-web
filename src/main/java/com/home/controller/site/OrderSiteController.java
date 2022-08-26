package com.home.controller.site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.home.domain.Category;
import com.home.domain.Customer;
import com.home.domain.Order;
import com.home.domain.OrderDetail;
import com.home.domain.Product;
import com.home.model.CartItem;
import com.home.model.CategoryDto;
import com.home.model.OrderDetailDto;
import com.home.model.OrderDto;
import com.home.service.OrderDetailService;
import com.home.service.OrderService;
import com.home.service.ProductService;
import com.home.service.ShoppingCartService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("order")
public class OrderSiteController {
	@Autowired
	HttpSession session;
	@Autowired
	ShoppingCartService cartService;

	@Autowired
	ProductService productService;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderDetailService orderDetailService;

	@GetMapping("checkout")
	public String viewCheck(Model model) {
		Customer user = (Customer) session.getAttribute("user");
		OrderDto dto = new OrderDto();
		dto.setName(user.getName());
		dto.setAmount(cartService.getAmount());
		dto.setOrderDate(new Date());
		dto.setCustomerId(user.getCustomerId());

		Collection<CartItem> items = cartService.getAllCartItem();
		double amount = cartService.getAmount();
		if (amount <= 0) {
			model.addAttribute("amount", true);
		}

		model.addAttribute("items", items);
		model.addAttribute("order", dto);

		return "site/order/checkout";
	}

	@PostMapping("checkout")
	public String viewCheck(@ModelAttribute("order") OrderDto dto, Model model) {
		Collection<CartItem> list = cartService.getAllCartItem();
		List<OrderDetail> details = new ArrayList<>();
		if (cartService.getAmount() <= 0) {
			model.addAttribute("message", "bạn chưa chọn mặt hàng cần mua");
			return "redirect:/cart";
		}
		Order order = new Order();
		BeanUtils.copyProperties(dto, order);

		Customer customer = new Customer();
		customer.setCustomerId(dto.getCustomerId());
		order.setCustomer(customer);

		orderService.save(order);

		for (CartItem item : list) {

			Optional<Product> product = productService.findById(item.getProductId());
			OrderDetail detail = new OrderDetail();
			detail.setOrder(order);
			detail.setProduct(product.get());
			detail.setUnitPrice(item.getUnitPrice());
			detail.setDiscount(item.getDiscount());
			detail.setQuantity(item.getQuantity());
			orderDetailService.save(detail);

			// trừ đi quatity của product
			product.get().setQuantity(product.get().getQuantity() - item.getQuantity());
			productService.save(product.get());
		}
		model.addAttribute("message", "Đặt hàng thành công");
		cartService.clear();
		return "site/order/checkout";
	}

	@GetMapping("/list")
	public String list(Model model) {
		Customer user = (Customer) session.getAttribute("user");

		List<Order> list = orderService.findByCustomerCustomerId(user.getCustomerId(),
				Sort.by(Direction.DESC, "orderDate"));

		// List<Order> list =
		// orderService.findByCustomerCustomerId(user.getCustomerId());
		model.addAttribute("list", list);

		return "site/order/list";
	}

	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable Long id) {

		List<OrderDetail> entity = orderDetailService.findByOrderOrderId(id);
//	
//		List<OrderDetailDto> list = new ArrayList<>();
//		 list = entity.stream().map(item -> {
//			OrderDetailDto dto = new OrderDetailDto();
//			BeanUtils.copyProperties(item, dto);
//			dto.setProductName(item.getProduct().getName());
//			return dto;
//			
//		}).toList();
//		model.addAttribute("details", list);

		Optional<Order> opt = orderService.findById(id);

		model.addAttribute("order", opt.get());
		model.addAttribute("details", entity);

		return "site/order/detail";
	}
}
