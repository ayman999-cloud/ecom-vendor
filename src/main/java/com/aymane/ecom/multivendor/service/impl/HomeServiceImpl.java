package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.domain.HomeCategoryItem;
import com.aymane.ecom.multivendor.model.Deal;
import com.aymane.ecom.multivendor.model.Home;
import com.aymane.ecom.multivendor.model.HomeCategory;
import com.aymane.ecom.multivendor.repository.DealRepository;
import com.aymane.ecom.multivendor.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {

        final List<HomeCategory> gridCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategoryItem.GRID
                ).toList();

        final List<HomeCategory> shopByCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategoryItem.SHOP_BY_CATEGORIES
                ).toList();

        final List<HomeCategory> electricCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategoryItem.ELECTRIC_CATEGORIES
                ).toList();

        final List<HomeCategory> dealCategories = allCategories.stream()
                .filter(category ->
                        category.getSection() == HomeCategoryItem.DEALS
                ).toList();

        List<Deal> createdDeals;

        if (dealRepository.findAll().isEmpty()) {
            final List<Deal> deals = allCategories.stream()
                    .filter(category -> category.getSection() == HomeCategoryItem.DEALS)
                    .map(category -> new Deal(null, 10, category)).toList();
            createdDeals = dealRepository.saveAll(deals);
        } else {
            createdDeals = dealRepository.findAll();
        }

        final Home home = new Home();

        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setElectricCategories(electricCategories);
        home.setDeals(createdDeals);
        home.setDealCategories(dealCategories);

        return home;
    }
}
