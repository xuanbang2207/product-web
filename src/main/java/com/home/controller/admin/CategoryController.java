package com.home.controller.admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.servlet.ModelAndView;

import com.home.domain.Category;
import com.home.domain.Product;
import com.home.model.CategoryDto;
import com.home.service.CategoryService;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@GetMapping("add")
	public String add(Model model) {
		model.addAttribute("category", new CategoryDto());
		return "admin/categories/addOrEdit";
	}

	@GetMapping("edit/{categoryId}")
	public ModelAndView eidt(@PathVariable("categoryId") Long categoryId, Model model) {

		Optional<Category> opt = categoryService.findById(categoryId);
		CategoryDto dto = new CategoryDto();

		if (opt.isPresent()) {
			Category entity = opt.get();

			BeanUtils.copyProperties(entity, dto);
			dto.setIsEdit(true);
			model.addAttribute("category", dto);
			return new ModelAndView("admin/categories/addOrEdit");
		}

		model.addAttribute("messgae", "Category is not existed");
		return new ModelAndView("redidect:/admin/categories");

	}

	@GetMapping("delete/{categoryId}")
	public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {

		categoryService.deleteById(categoryId);
		model.addAttribute("message", "Category đã được xóa");

		return new ModelAndView("forward:/admin/categories", model);
	}

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryDto dto,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ModelAndView("admin/categories/addOrEdit");
		}

		Category entity = new Category();
		BeanUtils.copyProperties(dto, entity);
		categoryService.save(entity);

		model.addAttribute("message", "Category is saved!");

		return new ModelAndView("redirect:/admin/categories", model);
	}

	@GetMapping("")
	public String list(ModelMap model, 
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "9") Integer size,
			@RequestParam(defaultValue = "name") String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

		Page<Category> pages = categoryService.findByNameContaining(name, pageable);

		model.addAttribute("categories", pages.getContent());
		model.addAttribute("TotalPages", pages.getTotalPages());

		return "admin/categories/list";
	}

}
