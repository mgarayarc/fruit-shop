package com.asiatec.fruitshop;

import com.asiatec.fruitshop.controller.ShoppingController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FruitShopApplication implements CommandLineRunner {

	@Autowired
	private ShoppingController shoppingController;

	public static void main(String[] args) {
		SpringApplication.run(FruitShopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(args.length == 2 ){
			shoppingController.preLoadedConsole(args);
		}else{
			shoppingController.interactiveConsole();
		}
	}
}
