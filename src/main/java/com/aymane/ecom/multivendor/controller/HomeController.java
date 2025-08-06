package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.controller.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse HomeControllerHandler() {
        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Hello World");
        return apiResponse;
    }
}
