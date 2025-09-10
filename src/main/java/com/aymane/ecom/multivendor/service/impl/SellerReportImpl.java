package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.model.SellerReport;
import com.aymane.ecom.multivendor.repository.SellerReportRepository;
import com.aymane.ecom.multivendor.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport sellerReport = this.sellerReportRepository.findBySellerId(seller.getId());
        if (sellerReport == null) {
            sellerReport = new SellerReport();
            sellerReport.setSeller(seller);
            return this.sellerReportRepository.save(sellerReport);
        }
        return sellerReport;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return this.sellerReportRepository.save(sellerReport);
    }
}
