package com.home.controller.site;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.home.domain.Category;
import com.home.domain.Customer;
import com.home.domain.Product;
import com.home.model.AccountDto;
import com.home.model.CustomerDto;
import com.home.model.FormChange;
import com.home.model.ProductDto;
import com.home.service.CookieService;
import com.home.service.CustomerService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.expr.NewArray;

@Controller
@RequestMapping("account")
public class CustomerSiteController {
	@Autowired
	CustomerService customerService;

	@Autowired
	HttpSession session;

	@Autowired
	CookieService cookieService;

	@GetMapping("add")
	public String register(Model model) {
		CustomerDto dto = new CustomerDto();
		
		dto.setIsEdit(false);

		model.addAttribute("customer", dto);

		// kiem tra xem co dang login ko
		Customer user = (Customer) session.getAttribute("user");
		if (user == null) {
			model.addAttribute("logout", true);
		} else {
			model.addAttribute("login", true);
		}

		return "site/account/add";
	}

	@GetMapping("edit")
	public String formEdit(Model model) {
		CustomerDto dto = new CustomerDto();
		Customer user = (Customer) session.getAttribute("user");
		if (user == null) {
			return "redirect:/account/login";
		}

		Customer entity = customerService.findByNameAndPassword(user.getName(), user.getPassword());

		BeanUtils.copyProperties(entity, dto);
		dto.setIsEdit(true);
		model.addAttribute("customer", dto);

		// kiem tra xem co dang login ko
		Customer user2 = (Customer) session.getAttribute("user");
		if (user2 == null) {
			model.addAttribute("logout", true);
		} else {
			model.addAttribute("login", true);
		}

		return "site/account/edit";
	}

	@PostMapping("update")
	public String update(ModelMap model, 
			@Valid @ModelAttribute("customer") CustomerDto dto,
			BindingResult result) {


		if (result.hasErrors()) {
			return "site/account/edit";
		}
		Customer user = (Customer) session.getAttribute("user");

		Customer entity = customerService.findByNameAndPassword(user.getName(), user.getPassword());

		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());

		customerService.save(entity);

		model.addAttribute("message", "Cập nhật thành công");
		session.setAttribute("user", entity);

		// kiem tra xem co dang login ko
		Customer user2 = (Customer) session.getAttribute("user");
		if (user2 == null) {
			model.addAttribute("logout", true);
		} else {
			model.addAttribute("login", true);
		}

		return "site/account/edit";
	}

	@PostMapping("save")
	public String save(Model model, 
			@Valid @ModelAttribute("customer") CustomerDto  dto, BindingResult result, RedirectAttributes param) {

		if (result.hasErrors()) {
			return "site/account/add";
		}

		Customer entity = new Customer();
		BeanUtils.copyProperties(dto, entity);

		entity.setActivated(true);
		entity.setAdmin(false);

		customerService.save(entity);

		param.addAttribute("message", "Đăng ký thàng công, vui lòng login");

		return "redirect:/account/login";
	}

	@GetMapping("change")
	public String formChange(Model model) {
		FormChange form = new FormChange();
		Customer user = (Customer) session.getAttribute("user");
		form.setName(user.getName());
		model.addAttribute("form", form);

		// kiem tra xem co dang login ko
		Customer user2 = (Customer) session.getAttribute("user");
		if (user2 == null) {
			model.addAttribute("logout", true);
		} else {
			model.addAttribute("login", true);
		}

		
		// kiem tra xem co dang login ko
				
				if (user == null) {
					model.addAttribute("logout", true);
				} else {
					model.addAttribute("login", true);
				}
				
		return "site/account/change";
	}

	@PostMapping("change")
	public String changPassword(Model model, @Valid @ModelAttribute("form") FormChange form, BindingResult result) {

		if (result.hasErrors()) {
			return "site/account/change";
		}
		Customer user = (Customer) session.getAttribute("user");

		Customer entity = customerService.findByNameAndPassword(user.getName(), user.getPassword());
		if (!entity.getPassword().equals(form.getPassword())) {
			model.addAttribute("message", "mật khẩu cũ nhập chưa đúng");
			return "site/account/change";
		}
		if (!form.getPassword1().equals(form.getPassword2())) {
			model.addAttribute("message", "mật khẩu mới và mật khẩu xác thực không giống nhau");
			return "site/account/change";
		}
		entity.setPassword(form.getPassword1());
		customerService.save(entity);
		session.setAttribute("user", entity);

		System.out.println(entity.getName());
		System.out.println(entity.getPassword());
		model.addAttribute("message", "Đổi mật khẩu thành công");

		// kiem tra xem co dang login ko
		Customer user2 = (Customer) session.getAttribute("user");
		if (user2 == null) {
			model.addAttribute("logout", true);
		} else {
			model.addAttribute("login", true);
		}

		return "site/account/change";
	}
}
