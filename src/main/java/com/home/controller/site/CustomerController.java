package com.home.controller.site;



import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import com.home.service.CustomerService;
import com.home.service.ProductService;
import com.home.service.StorageService;

@Controller
@RequestMapping("site/customers")
public class CustomerController {

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
   
//    @GetMapping("add")
//
//    public String add(Model model) {
//	model.addAttribute("customer", new CustomerDto());
//	return "/site/customers/addOrEdit";
//
//    }
//
//    @GetMapping("edit/{customerId}")
//    public ModelAndView eidt(@PathVariable("customerId") Long customerId, Model model) {
//
//	Optional<Customer> opt = customerService.findById(customerId);
//	CustomerDto dto = new CustomerDto();
//
//	if (opt.isPresent()) {
//	    Customer entity = opt.get();
//
//	    BeanUtils.copyProperties(entity, dto);
//	    dto.setIsEdit(true);
//	    model.addAttribute("customer", dto);
//	    return new ModelAndView("site/customers/addOrEdit");
//	}
//
//	model.addAttribute("messgae", "Customer is not existed");
//	return new ModelAndView("redidect:/site/customers");
//
//    }
//
//    @GetMapping("delete/{customerId}")
//    public ModelAndView delete(ModelMap model, @PathVariable("customerId") Long customerId) {
//
//	customerService.deleteById(customerId);
//	model.addAttribute("message", "customer is deleted!");
//
//	return new ModelAndView("forward:/site/customers", model);
//    }
    
    @GetMapping("by-category/{categoryId}")
  public String byCategoryId(Model model, @PathVariable("categoryId") Long categoryId) {

//	Optional<Category> entity  = categoryService.findById(categoryId);
	
	List<Product> list = productService.findByCategoryId(categoryId);
	model.addAttribute("products", list);

	return "/site/customers/list";
  }
    @GetMapping("detail/{id}")
    public String detail(@PathVariable("id") Long productId, Model model ) {
    	
    	Optional<Product>  product = productService.findById(productId);
    	ProductDto dto = new ProductDto();
    	if (product.isPresent()) {
    		Product	entity = product.get();
    	
			BeanUtils.copyProperties(entity, dto);
			dto.setCategoryName(entity.getCategory().getName());
			
			List<Product> list = productService.findBycategoryId(entity.getCategory().getCategoryId());
			
			model.addAttribute("detail", dto);
			model.addAttribute("list", list);
		}
    	
    	return "site/customers/detail";
    	
    }
//
//    @PostMapping("saveOrUpdate")
//    public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("customer") CustomerDto dto,
//	    BindingResult result) {
//
//	if (result.hasErrors()) {
//	    return new ModelAndView("site/customers/addOrEdit");
//	}
//
//	Customer entity = new Customer();
//	BeanUtils.copyProperties(dto, entity);
//	customerService.save(entity);
//
//	model.addAttribute("message", "Customer is saved!");
//
//	return new ModelAndView("redirect:/site/customers", model);
//    }

    @GetMapping("")
    public ModelAndView list(ModelMap model) {
    	List<Product> list = productService.findAll();

    	model.addAttribute("products", list);


	return new ModelAndView("site/customers/list");
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
    public String search(Model model,
    						   @RequestParam(name = "name", defaultValue = "") String name) {
	List<Product> list = null;
	 
	    list = productService.findByNameContaining(name);
	    
	

	model.addAttribute("products", list);
	return "site/customers/list";
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
////
////    @GetMapping("page")
////    public String page(ModelMap model, @RequestParam("page") Optional<Integer> page) {
////	Pageable pageable = PageRequest.of(page.orElse(0), 7);
////
////	Page<customer> pages = customerService.findAll(pageable);
////
////	model.addAttribute("pages", pages);
////
////	return "site/customers/page";
////    }
//    @GetMapping("home")
//    public String list(ModelMap model) {
//	List<Product> list = productService.findAll();
//
//	model.addAttribute("products", list);
////	model.addAttribute("message", "Done!!!!");
//
//	return "site/customers/home";
//    }
//
//    @GetMapping("/images/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//	Resource file = storageService.loadAsResource(filename);
//
//
//	return ResponseEntity.ok()
//		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//		.body(file);
//    }
}
