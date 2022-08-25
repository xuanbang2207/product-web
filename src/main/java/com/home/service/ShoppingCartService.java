package com.home.service;

import java.util.Collection;

import com.home.model.CartItem;

public interface ShoppingCartService {

	double getAmount();

	int getCount();

	Collection<CartItem> getAllCartItem();

	void clear();

	CartItem update(Long productId, Integer qty);

	void remove(Long id);

	void add(CartItem item);

	
}
