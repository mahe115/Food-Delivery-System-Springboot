package com.example.fooddelivery.food_delivery_backend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class welcomecontroller {
    @GetMapping("/welcome")    
    public String welcome(){
        return "Welcome to Food Delivery Application";
    }       
}
