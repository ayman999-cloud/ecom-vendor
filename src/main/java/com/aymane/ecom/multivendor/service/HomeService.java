package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.model.Home;
import com.aymane.ecom.multivendor.model.HomeCategory;

import java.util.List;

public interface HomeService {
    Home createHomePageData(List<HomeCategory> allCategories);
}
