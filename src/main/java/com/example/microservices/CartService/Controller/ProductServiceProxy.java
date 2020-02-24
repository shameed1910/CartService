package com.example.microservices.CartService.Controller;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.microservices.CartService.Controller.Product;

@FeignClient("ProductService")

public interface ProductServiceProxy {
	@GetMapping("/products")
	public List<Product> showProductDetails();
}
