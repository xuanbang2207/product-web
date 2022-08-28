package com.home.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.home.model.CartItem;
import com.home.service.ShoppingCartService;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service() // Name:  scopedTarget.shoppingCartService
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService {
	Map<Long, CartItem> maps = new HashMap<>(); // danh sach gio hang
	
	@Override
	public void add(CartItem item) {   //them vao gio hang
		CartItem cartItem = maps.get(item.getProductId());
		if (cartItem == null) {
			maps.put(item.getProductId(), item);
		}else {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
		}
	}
	
	@Override
	public void remove(Long id) {
		maps.remove(id);
	}
	
	public CartItem update(Long productId, Integer qty) {
		CartItem cartItem = maps.get(productId);
		cartItem.setQuantity(qty);
		return cartItem;
	}
	
	@Override
	public void clear() {
		maps.clear();
	}
	
	@Override
	public Collection<CartItem> getAllCartItem(){
		return maps.values();
	}
	
	@Override
	public int getCount() {
//		Collection<CartItem> ps = this.getAllCartItem();
//		int count = 0;
//		for (CartItem item : ps) {
//			count += item.getQuantity();
//		}
//		return count;
		
		return maps.values().stream().mapToInt(item -> item.getQuantity()).sum();
	}
	
	@Override
	public double getAmount () {
		return maps.values().stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice() * (100-item.getDiscount()) / 100).sum();
	}
}
