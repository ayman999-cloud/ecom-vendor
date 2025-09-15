package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.model.Home;
import com.aymane.ecom.multivendor.model.HomeCategory;

import java.util.List;

public interface HomeCategoryService {

    HomeCategory createHomeCategory(HomeCategory homeCategory);

    List<HomeCategory> createCategories(List<HomeCategory> homeCategories);

    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception;

    List<HomeCategory> getAllHomeCategories();
}
