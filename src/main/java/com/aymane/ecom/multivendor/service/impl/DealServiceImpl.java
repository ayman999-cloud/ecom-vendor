package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.model.Deal;
import com.aymane.ecom.multivendor.model.HomeCategory;
import com.aymane.ecom.multivendor.repository.DealRepository;
import com.aymane.ecom.multivendor.repository.HomeCategoryRepository;
import com.aymane.ecom.multivendor.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getDeals() {
        return this.dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        final HomeCategory homeCategory = this.homeCategoryRepository.findById(deal.getHomeCategory().getId()).orElse(null);

        final Deal newDeal = dealRepository.save(deal);
        newDeal.setHomeCategory(homeCategory);
        newDeal.setDiscount(deal.getDiscount());
        return this.dealRepository.save(deal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) throws Exception {
        final Deal existingDeal = this.dealRepository.findById(id).orElseThrow(() -> new Exception("Deal not exist"));
        final HomeCategory homeCategory = this.homeCategoryRepository.findById(deal.getHomeCategory().getId()).orElse(null);

        if (existingDeal != null) {
            if (deal.getDiscount() != null) {
                existingDeal.setDiscount(deal.getDiscount());
            }
            if (homeCategory != null) {
                existingDeal.setHomeCategory(homeCategory);
            }

            return dealRepository.save(existingDeal);
        }
        throw new Exception("Deal not found");
    }

    @Override
    public void deleteDeal(Long id) throws Exception {
        final Deal dealToDelete = this.dealRepository.findById(id).orElseThrow(() -> new Exception("Deal not found"));
        dealRepository.delete(dealToDelete);
    }
}
