package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.model.HomeCategory;
import com.aymane.ecom.multivendor.repository.HomeCategoryRepository;
import com.aymane.ecom.multivendor.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return this.homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
        if (this.homeCategoryRepository.findAll().isEmpty()) {
            return this.homeCategoryRepository.saveAll(homeCategories);
        }
        return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
        final HomeCategory homeCategoryExist = this.homeCategoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Home category not found"));

        if (homeCategoryExist.getImage() != null) {
            homeCategoryExist.setImage(homeCategory.getImage());
        }

        if (homeCategory.getCategoryId() != null) {
            homeCategoryExist.setCategoryId(homeCategory.getCategoryId());
        }

        return this.homeCategoryRepository.save(homeCategoryExist);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return this.homeCategoryRepository.findAll();
    }
}
