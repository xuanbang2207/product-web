package com.home.controller.site;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.home.domain.Category;
import com.home.domain.Customer;
import com.home.domain.Product;
import com.home.model.CategoryDto;
import com.home.model.CustomerDto;
import com.home.model.ProductDto;
import com.home.service.CategoryService;
import com.home.service.CookieService;
import com.home.service.CustomerService;
import com.home.service.ProductService;
import com.home.service.ShoppingCartService;
import com.home.service.StorageService;

@Controller
@RequestMapping("")
public class ProductSiteController {

	@Autowired
	HttpSession session;
	@Autowired
	ShoppingCartService cartService;

	@Autowired
	CookieService cookieService;

	@Autowired
	CustomerService customerService;

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	StorageService storageService;

	@ModelAttribute("categories")
	public List<CategoryDto> getCategories() {
		return categoryService.findAll().stream().map(item -> {
			CategoryDto dto = new CategoryDto();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).toList();
	}

	@GetMapping("by-category/{categoryId}")
	public String byCategoryId(Model model, @PathVariable("categoryId") Long categoryId) {

//	Optional<Category> entity  = categoryService.findById(categoryId);

		List<Product> list = productService.findByCategoryCategoryId(categoryId);
		model.addAttribute("products", list);

		// load shopping cart
		int numcart = cartService.getCount();
		model.addAttribute("numcart", numcart);

		return "/site/productSite/byCategory";
	}

	@GetMapping("detail/{id}")
	public String detail(@PathVariable("id") Long productId, Model model) {

		Optional<Product> product = productService.findById(productId);
		ProductDto dto = new ProductDto();
		if (product.isPresent()) {
			Product entity = product.get();

			// tăng lượt xem lên 1
			if (entity.getViewCount() == null) {
				entity.setViewCount(0);
			}
			entity.setViewCount(entity.getViewCount() + 1);
			productService.save(entity);

			BeanUtils.copyProperties(entity, dto);

			// xem chi tiết sản phẩm
			dto.setCategoryName(entity.getCategory().getName());
			model.addAttribute("detail", dto);

			// tìm sản phẩm cùng loại
			List<Product> list = productService.findByCategoryCategoryId(entity.getCategory().getCategoryId());
			model.addAttribute("list", list);

			// tìm sản phẩm đc đánh dấu thích
			Cookie cookie = cookieService.read("favo");
			if (cookie != null) {

				String stringIds = cookie.getValue();
				String[] names = stringIds.split(",");
				List nameList = Arrays.asList(names);

//				System.out.println(nameList);
				List<Product> prods = productService.findByNameIn(nameList);

				model.addAttribute("prods", prods);
			}

		}
		// load shopping cart
		int numcart = cartService.getCount();
		model.addAttribute("numcart", numcart);

		return "/site/productSite/detail";

	}

	@RequestMapping("add-to-favo/{name}")
	public ModelAndView addToFavorite(@PathVariable("name") String name, ModelMap model) {

		Cookie favo = cookieService.read("favo");
		String value = name;
		if (favo != null) {
			value = favo.getValue();

			if (!value.contains(name)) {
				value += "," + name;
			}
		}
		cookieService.create("favo", value, 2);

		return new ModelAndView("redirect:/");

	}

	@GetMapping("list-by-enteredDate")
	public String byEnteredDate(ModelMap model, @RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "9") Integer size,
			@RequestParam(defaultValue = "enteredDate") String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, sort));

		Page<Product> pages = productService.findAll(pageable);

		model.addAttribute("sort", sort);

		model.addAttribute("products", pages.getContent());
		model.addAttribute("TotalPages", pages.getTotalPages());

		// load shopping cart
		int numcart = cartService.getCount();
		model.addAttribute("numcart", numcart);

		return "site/productSite/list";
	}

	@GetMapping("list-by-viewCount")
	public String byviewCount(ModelMap model, @RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "9") Integer size,
			@RequestParam(defaultValue = "viewCount") String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, sort));

		Page<Product> pages = productService.findAll(pageable);

		model.addAttribute("sort", sort);

		model.addAttribute("products", pages.getContent());
		model.addAttribute("TotalPages", pages.getTotalPages());

		// load shopping cart
		int numcart = cartService.getCount();
		model.addAttribute("numcart", numcart);

		return "site/productSite/list";
	}

	@GetMapping("list-by-discount")
	public String bydiscount(ModelMap model, @RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "9") Integer size,
			@RequestParam(defaultValue = "discount") String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, sort));

		Page<Product> pages = productService.findAll(pageable);

		model.addAttribute("sort", sort);

		model.addAttribute("products", pages.getContent());
		model.addAttribute("TotalPages", pages.getTotalPages());

		// load shopping cart
		int numcart = cartService.getCount();
		model.addAttribute("numcart", numcart);

		return "site/productSite/list";
	}

	@GetMapping("/images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping("search")
	public String search(Model model, @RequestParam(name = "name", defaultValue = "") String name) {
		List<Product> list = null;

		list = productService.findByNameContaining(name);

		model.addAttribute("products", list);
		return "site/productSite/list";
	}

	@GetMapping("")
	public String pageHome(ModelMap model, 
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "9") Integer size,
			@RequestParam(defaultValue = "name") String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		Page<Product> pages = productService.findByNameContaining(name, pageable);

		// load shopping cart
		int numcart = cartService.getCount();
		model.addAttribute("numcart", numcart);

		// kiem tra xem co dang login ko
		Customer user = (Customer) session.getAttribute("user");
		if (user == null) {
			model.addAttribute("logout", true);
		} else {
			model.addAttribute("login", true);
		}

		model.addAttribute("name", name);
		model.addAttribute("sort", sort);

		model.addAttribute("products", pages.getContent());
		model.addAttribute("TotalPages", pages.getTotalPages());

		return "site/productSite/list";
	}

	@GetMapping("about-us")
	public String about() {
		return "site/home/about";
	}

	@GetMapping("contact-us")
	public String contact() {
		return "site/home/contact";
	}

	@GetMapping("FAQs")
	public String FAQs() {
		return "site/home/FAQs";
	}

	@GetMapping("feedback")
	public String feedback() {
		return "site/home/feedback";
	}
}
