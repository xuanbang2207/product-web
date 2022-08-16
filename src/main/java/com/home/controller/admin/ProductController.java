package com.home.controller.admin;



import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.home.domain.Product;
import com.home.model.CategoryDto;
import com.home.model.ProductDto;
import com.home.service.CategoryService;
import com.home.service.ProductService;
import com.home.service.StorageService;

@Controller
@RequestMapping("admin/products")
public class ProductController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

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

    @GetMapping("add")
    public String add(Model model) {
	ProductDto dto = new ProductDto();
	dto.setIsEdit(false);

	model.addAttribute("product", dto);

	return "admin/products/addOrEdit";
    }

    @GetMapping("edit/{productId}")
    public ModelAndView edit(@PathVariable("productId") Long productId, Model model) {
	
	Optional<Product> opt = productService.findById(productId);
	ProductDto dto = new ProductDto();
	
	if (opt.isPresent()) {
	    Product entity = opt.get();
	    
	    BeanUtils.copyProperties(entity, dto);

	    dto.setCategoryId(entity.getCategory().getCategoryId());
	    dto.setIsEdit(true);
	    model.addAttribute("product", dto);

	    return new ModelAndView("admin/products/addOrEdit");
	}
	
	model.addAttribute("messgae", "Product is not existed");
	return new ModelAndView("forward:/admin/products");
	
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
	Resource file = storageService.loadAsResource(filename);

	return ResponseEntity.ok()
		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
		.body(file);
    }

    @GetMapping("delete/{productId}")
    public ModelAndView delete(ModelMap model, @PathVariable("productId") Long productId) throws IOException {

	Optional<Product> opt = productService.findById(productId);

	if (opt.isPresent()) {
	    if (!StringUtils.isEmpty(opt.get().getImage())) {
		storageService.delete(opt.get().getImage());
	    }
	    productService.delete(opt.get());

	    model.addAttribute("message", "Product is deleted!");
	} else {
	    model.addAttribute("message", "Product  is not Found!");
	}

	return new ModelAndView("forward:/admin/products", model);
    }

    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("product") ProductDto dto,
	    BindingResult result) {

	if (result.hasErrors()) {
	    return new ModelAndView("admin/products/addOrEdit");
	}

	Product entity = new Product();
	BeanUtils.copyProperties(dto, entity);

	Category category = new Category();
	category.setCategoryId(dto.getCategoryId());
	entity.setCategory(category);
	entity.setEnteredDate(new Date());

	if (!dto.getImageFile().isEmpty()) {
	    UUID uuid = UUID.randomUUID();
	    String uuString = uuid.toString();

	    entity.setImage(storageService.getStoredFilename(dto.getImageFile(), uuString));
	    storageService.store(dto.getImageFile(), entity.getImage());
	}

	productService.save(entity);

	model.addAttribute("message", "Product is saved!");

	return new ModelAndView("redirect:/admin/products", model);
    }

    @GetMapping("")
    public String list(Model model) {
	List<Product> list = productService.findAll();

	model.addAttribute("products", list);
//	model.addAttribute("message", "Done!!!!");

	return "admin/products/list";
    }

//    @GetMapping("search")
//    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
//	List<Product> list = null;
//	if (StringUtils.hasText(name)) {
//	    list = productService.findByNameContaining(name);
//
//	} else {
//	    list = productService.findAll();
//	}
//
//	model.addAttribute("products", list);
//	return "admin/products/search";
//    }

    @GetMapping("page")
    public String page(ModelMap model, @RequestParam("page") Optional<Integer> page) {
	Pageable pageable = PageRequest.of(page.orElse(0), 7);

	Page<Category> pages = categoryService.findAll(pageable);

	model.addAttribute("pages", pages);

	return "admin/products/page";
    }


}
