package com.aymane.ecom.multivendor.controller;


import com.aymane.ecom.multivendor.model.Home;
import com.aymane.ecom.multivendor.model.HomeCategory;
import com.aymane.ecom.multivendor.service.HomeCategoryService;
import com.aymane.ecom.multivendor.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories) {
        final List<HomeCategory> homeCategoriesCreated = this.homeCategoryService.createCategories(homeCategories);
        final Home home = this.homeService.createHomePageData(homeCategoriesCreated);
        return new ResponseEntity<>(home, HttpStatus.CREATED);
    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategory() {
        final List<HomeCategory> categories = this.homeCategoryService.getAllHomeCategories();
        return ResponseEntity.ok(categories);
    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable Long id, @RequestBody HomeCategory homeCategory) throws Exception {
        final HomeCategory homeCategoryUpdate = this.homeCategoryService.updateHomeCategory(homeCategory, id);
        return ResponseEntity.ok(homeCategoryUpdate);
    }

}
