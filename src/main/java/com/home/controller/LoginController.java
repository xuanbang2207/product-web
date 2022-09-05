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
@RequestMapping("account")
public class LoginController {

	@Autowired
	HttpSession session;
	@Autowired
	CookieService cookieService;
	@Autowired
	CustomerService customerService;

	@GetMapping("login")
	public String login(Model model) {
		CustomerDto dto = new CustomerDto();

		Cookie ckname = cookieService.read("name");
		Cookie ckpassword = cookieService.read("password");
		if (ckname != null) {
			String name = ckname.getValue();
			String password = ckpassword.getValue();

			dto.setName(name);
			dto.setPassword(password);

		}
		model.addAttribute("account", dto);

		
		return "site/account/login";
	}

	@PostMapping("login")
	public ModelAndView login(ModelMap model, @Valid @ModelAttribute("account") CustomerDto dto, BindingResult result) {


		Customer opt = customerService.findByNameAndPassword(dto.getName(), dto.getPassword());

		if (opt == null) {
			model.addAttribute("message", "Tên người dùng hoặc mật khẩu không đúng");

		} else if (opt.getActivated() == false) {
			model.addAttribute("message", "Tài khoản chưa được kích hoạt");

		} else {

			model.addAttribute("message", "Đăng nhập thành công");
			session.setAttribute("user", opt);
			System.out.println("user dang nhap thanh cong");

			// Ghi nhớ tài khoản bằng cookie
			if (dto.getRememberMe() == true) {
				cookieService.create("name", opt.getName(), 10);
				cookieService.create("password", opt.getPassword(), 10);
			} else {
				cookieService.delete("name");
				cookieService.delete("password");
			}

			// quay lại trang dc bảo vế nếu có
			String backUri = (String) session.getAttribute("back-uri");
			if (backUri != null) {
				session.removeAttribute("back-uri");
				return new ModelAndView("redirect:" + backUri);
			}
			
			
		}

		return new ModelAndView("/site/account/login", model);
	}

	@RequestMapping("logout")
	public String logout(Model model) {
		session.removeAttribute("user");
		System.out.println("user logout thanh cong");
		return "redirect:/";
	}
}
