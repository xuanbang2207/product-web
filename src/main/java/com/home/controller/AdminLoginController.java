package com.home.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.home.domain.Customer;
import com.home.model.AccountDto;
import com.home.model.CustomerDto;
import com.home.service.CookieService;
import com.home.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("accountAdmin")
public class AdminLoginController {

	@Autowired
	HttpSession session;
	@Autowired
	CookieService cookieService;
	@Autowired
	CustomerService customerService;

	@GetMapping("login")
	public String login(Model model) {
		CustomerDto dto = new CustomerDto();

		Cookie ckname = cookieService.read("adname");
		Cookie ckpassword = cookieService.read("adpassword");
		if (ckname != null) {
			String name = ckname.getValue();
			String password = ckpassword.getValue();

			dto.setName(name);
			dto.setPassword(password);

		}
		model.addAttribute("account", dto);

		// kiem tra xem co dang login ko
		Customer user = (Customer) session.getAttribute("user");
		if (user == null) {
			model.addAttribute("logout", true);

		} else {
			model.addAttribute("login", true);
		}

		return "admin/accounts/login";
	}

	@PostMapping("login")
	public ModelAndView login(ModelMap model,@Valid @ModelAttribute("account") CustomerDto dto, BindingResult result) {

//		if (result.hasErrors()) {
//			return new ModelAndView("/admin/accounts/login");
//		}
		Customer opt = customerService.findByNameAndPassword(dto.getName(), dto.getPassword());

		if (opt == null) {
			model.addAttribute("message", "Tên người dùng hoặc mật khẩu không đúng");

		} else if (opt.getActivated() == false) {
			model.addAttribute("message", "Tài khoản chưa được kích hoạt");

		} else if (opt.getAdmin() == false) {
			model.addAttribute("message", "Tài khoản không phải là admin");
		} else {

			model.addAttribute("message", "Đăng nhập thành công");
			session.setAttribute("admin", opt);

			// Ghi nhớ tài khoản bằng cookie
			if (dto.getRememberMe() == true) {
				cookieService.create("adname", opt.getName(), 10);
				cookieService.create("adpassword", opt.getPassword(), 10);
			} else {
				cookieService.delete("adname");
				cookieService.delete("adpassword");
			}

			// quay lại trang dc bảo vế nếu có
			String backUri = (String) session.getAttribute("back-uri");
			if (backUri != null) {
				session.removeAttribute("back-uri");
				return new ModelAndView("redirect:" + backUri);
			}

		}

		return new ModelAndView("/admin/accounts/login", model);
	}

	@RequestMapping("logout")
	public String logout(Model model) {
		session.removeAttribute("admin");
		System.out.println("Admin logout thanh cong");
		return "redirect:/accountAdmin/login";
	}
}
