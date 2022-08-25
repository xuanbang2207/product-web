package com.home.controller.site;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.home.domain.Customer;
import com.home.domain.Product;
import com.home.model.CartItem;
import com.home.service.ProductService;
import com.home.service.ShoppingCartService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("cart")
public class ShoppingCartController {
	
	@Autowired
	ShoppingCartService cartService;
	@Autowired
	ProductService productService;
	@Autowired
	HttpSession session;
	@GetMapping
	public String viewCart(Model model) {
		Collection<CartItem> items = cartService.getAllCartItem();
		double amonut = cartService.getAmount();
		model.addAttribute("amount", amonut);
		model.addAttribute("items", items);
		
		//kiem tra xem co dang login ko
		Customer user = (Customer) session.getAttribute("user");
		if (user == null) {
			model.addAttribute("logout", true);
		}else {
			model.addAttribute("login", true);
		}
		return "site/cart/viewCart";
	}
	
	@GetMapping("add/{id}")
	public String add(@PathVariable Long id, Model model) {
		CartItem item = new CartItem();
		Optional<Product> opt = productService.findById(id);
		if (opt != null) {
		Product	entity = opt.get();
		
		BeanUtils.copyProperties(entity, item);
		item.setQuantity(1);
		cartService.add(item);
			
		}
		
		return "redirect:/";
	}
	
	@GetMapping("clear")
	public String clear() {
		cartService.clear();
		
		return "redirect:/cart";
	}
	
	@GetMapping("delete/{id}")
	public String remove(@PathVariable("id") Long id, Model model) {
		cartService.remove(id);
		return "redirect:/cart";
	}

	@PostMapping("update")
	public String update(@RequestParam Long id, @RequestParam Integer qty) {
		cartService.update(id, qty);
		return "redirect:/cart";
	}
}
