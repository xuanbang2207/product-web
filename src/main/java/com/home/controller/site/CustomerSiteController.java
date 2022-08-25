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
	
	@GetMapping("login")
	public String login(Model model) {
		AccountDto dto = new AccountDto();
		
		Cookie ckname = cookieService.read("name");
		Cookie ckpassword = cookieService.read("password");
		if (ckname != null) {
			String name = ckname.getValue();
			String password = ckpassword.getValue();
			
			dto.setName(name);
			dto.setPassword(password);
			
		}
		model.addAttribute("account", dto);
		
		//kiem tra xem co dang login ko
		Customer user = (Customer) session.getAttribute("user");
		if (user == null) {
			model.addAttribute("logout", true);
			System.out.println("lohout");
		}else {
			model.addAttribute("login", true);
			System.out.println("login");
		}
		
		return "site/account/login";
	}
	
	@PostMapping("login")
	public ModelAndView login(ModelMap model, @Valid @ModelAttribute("account") AccountDto dto,
		    BindingResult result) {

		if (result.hasErrors()) {
		    return new ModelAndView("/site/account/login");
		}
		 Customer opt = customerService.findByNameAndPassword(dto.getName(), dto.getPassword());
		 
		 if (opt == null) {
			model.addAttribute("message", "Tên người dùng hoặc mật khẩu không đúng");
		
			
		}else if ( opt.getActivated() == false) {
			model.addAttribute("message", "Tài khoản chưa được kích hoạt");
		}else {
			model.addAttribute("message", "Đăng nhập thành công");
			session.setAttribute("user",  opt);
			
			//Ghi nhớ tài khoản bằng cookie
			if (dto.getRememberMe() == true) {
				cookieService.create("name",  opt.getName(), 10);
				cookieService.create("password",  opt.getPassword(), 10);
			}else {
				cookieService.delete("name");
				cookieService.delete("password");
			}
			
			//quay lại trang dc bảo vế nếu có
			String backUri = (String) session.getAttribute("back-uri");
			if (backUri != null) {
				 session.removeAttribute("back-uri");
				  return new ModelAndView("redirect:" + backUri);
			}
			
		}
		 
		
		return new ModelAndView( "/site/account/login", model);
	}

	@RequestMapping("logout")
	public String logout(Model model) {
		session.removeAttribute("user");
		System.out.println("logout thanh cong");
			return "redirect:/";
	}
	
	@GetMapping("add")
    public String add(Model model) {
	CustomerDto dto = new CustomerDto();
	dto.setIsEdit(false);

	model.addAttribute("customer", dto);
	
	//kiem tra xem co dang login ko
		Customer user = (Customer) session.getAttribute("user");
		if (user == null) {
			model.addAttribute("logout", true);
		}else {
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
		 
		//kiem tra xem co dang login ko
			Customer user2 = (Customer) session.getAttribute("user");
			if (user2 == null) {
				model.addAttribute("logout", true);
			}else {
				model.addAttribute("login", true);
			}
			
		 return "site/account/edit";
	}
	
   @PostMapping("update")
    public String update(Model model, @ModelAttribute("customer") CustomerDto dto
    	    ) {

	   Customer user = (Customer) session.getAttribute("user");
	   
	    Customer entity = customerService.findByNameAndPassword(user.getName(), user.getPassword());
    	
    	entity.setName(dto.getName());
    	entity.setPhone(dto.getPhone());
    	entity.setEmail(dto.getEmail());

    	customerService.save(entity);   	
    	
    	model.addAttribute("message", "Cập nhật thành công");
    	session.setAttribute("user", entity);
    	
    	 
		//kiem tra xem co dang login ko
			Customer user2 = (Customer) session.getAttribute("user");
			if (user2 == null) {
				model.addAttribute("logout", true);
			}else {
				model.addAttribute("login", true);
			}
    	
    	return "site/account/edit";
        }
    
    @PostMapping("save")
    public ModelAndView save(ModelMap model, @Valid @ModelAttribute("customer") CustomerDto dto,
	    BindingResult result) {

	if (result.hasErrors()) {
	    return new ModelAndView("site/account/add");
	}

	Customer entity = new Customer();
	BeanUtils.copyProperties(dto, entity);

	entity.setActivated(true);
	entity.setAdmin(false);

	
    
	customerService.save(entity);

	model.addAttribute("message", "Done");
	
	

	return new ModelAndView("redirect:/account/login", model);
    }
    
    @GetMapping("change")
    public String formChange(Model model) {
    	FormChange form = new FormChange();
    	Customer user = (Customer) session.getAttribute("user");
    	form.setName(user.getName());
    	model.addAttribute("form", form);
    	
    	 
    	//kiem tra xem co dang login ko
    		Customer user2 = (Customer) session.getAttribute("user");
    		if (user2 == null) {
    			model.addAttribute("logout", true);
    		}else {
    			model.addAttribute("login", true);
    		}
    		
    	return "site/account/change";
    }
    
    @PostMapping("change")
    public String changPassword(Model model, @Valid @ModelAttribute("form") FormChange form,
    	    BindingResult result) {

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
    	
    	 
    	//kiem tra xem co dang login ko
    		Customer user2 = (Customer) session.getAttribute("user");
    		if (user2 == null) {
    			model.addAttribute("logout", true);
    		}else {
    			model.addAttribute("login", true);
    		}
    	
    	return "site/account/change";
    }
}
