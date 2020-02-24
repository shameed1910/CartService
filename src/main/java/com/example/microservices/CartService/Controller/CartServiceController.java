package com.example.microservices.CartService.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class CartServiceController {
	private org.slf4j.Logger logger=LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductServiceProxy proxy;
	
	@Autowired
	private RestTemplate template;

	List<Cart> mycart = new ArrayList<Cart>();

	@GetMapping("/home")
	public String getMycart() {
		ResponseEntity<String> entity = template.getForEntity("http://ProductService/home", String.class);
		String response = entity.getBody();
		logger.info("Response::"+response);
		return response;
	}

	@HystrixCommand(fallbackMethod = "addMycart_Fallback")
	@GetMapping("/mycart/{productId}")
	public List<Cart> addMycart(@PathVariable("productId") String productId) {
		List<Product> l = proxy.showProductDetails();
		Cart cart = new Cart();
		l.forEach(product -> {
			if (productId.equals(product.getProductId())) {
				System.out.println("productId" + product.getProductId());
				cart.setDescription(product.getDescription());
				cart.setId(product.getId());
				cart.setPrice(product.getPrice());
				cart.setProductId(product.getProductId());
				cart.setPort(product.getPortNumber());
				cart.setQuantity(1);
				mycart.add(cart);
				System.out.println(product.getDescription());
			}
		});
		logger.info("Response::"+mycart);

		return mycart;
	}

	public List<Cart> addMycart_Fallback(String productId) 
	{
		System.out.println("Product Service is down!!! fallback route enabled...");
		List<Cart> mycart = new ArrayList<Cart>();
		Cart cart = new Cart(0, "00", "Dummy", 0000,1,"port");
		mycart.add(cart);
		return mycart;
	}
}
