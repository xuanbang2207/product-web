package com.home.controller.admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
import com.home.model.CustomerDto;
import com.home.service.CustomerService;
import com.home.model.CustomerDto;

@Controller
@RequestMapping("admin/accounts")
public class AccountController {

	@Autowired
	CustomerService customerService;

	@GetMapping("add")
	public String add(Model model) {
		model.addAttribute("account", new CustomerDto());
		return "admin/accounts/addOrEdit";
	}

	@PostMapping("saveOrUpdate")
	public String saveOrUpdate(ModelMap model, @Valid @ModelAttribute("account") CustomerDto dto,
			BindingResult result) {

		if (result.hasErrors()) {
			return "admin/accounts/addOrEdit";
		}

		Customer entity = new Customer();

		BeanUtils.copyProperties(dto, entity);
		System.out.println(dto.getIsEdit());

		if (dto.getIsEdit() == true) {

			model.addAttribute("message", "Đã cập nhật người dùng");
		} else {
			model.addAttribute("message", "Đã lưu mới người dùng");
		}
		customerService.save(entity);
		return "admin/accounts/addOrEdit";
	}

	@GetMapping("edit/{id}")
	public String eidt(@PathVariable("id") Long id, ModelMap model) {

		Optional<Customer> opt = customerService.findById(id);
		CustomerDto dto = new CustomerDto();

		if (opt.isPresent()) {
			Customer entity = opt.get();
			BeanUtils.copyProperties(entity, dto);
			dto.setIsEdit(true);
//	    dto.setPassword("");
			model.addAttribute("account", dto);
			model.addAttribute("messgae", "Cập nhật người dùng thành công");
		}

		model.addAttribute("messgae", "Người dùng không tồn tại");
		return "/admin/accounts/addOrEdit";

	}

	@GetMapping("delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long id) {
		Optional<Customer> opt = customerService.findById(id);

		if (opt.isPresent()) {
			customerService.deleteById(id);
			model.addAttribute("message", "Đã xóa thành công người dùng");

		} else {
			model.addAttribute("message", "Người dùng tìm kiếm không tồn tại");
		}

		return new ModelAndView("forward:/admin/accounts/all", model);
	}

	@GetMapping("ad")
	public String listAdmin(Model model) {
		List<Customer> list = customerService.findByAdmin(true);

		model.addAttribute("accounts", list);

		return "admin/accounts/list";
	}

	@GetMapping("all")
	public String list(ModelMap model, @RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size, @RequestParam(defaultValue = "name") String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		Page<Customer> pages = customerService.findAll(pageable);

		model.addAttribute("accounts", pages.getContent());
		model.addAttribute("TotalPages", pages.getTotalPages());

		return "admin/accounts/listAll";
	}

	@GetMapping("search")
	public String search(ModelMap model, @RequestParam(name = "name", defaultValue = "") String name) {

		List<Customer> list = customerService.findByNameContaining(name);

		model.addAttribute("accounts", list);
		return "admin/accounts/list";
	}

}
