package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);

    SellerReport updateSellerReport(SellerReport sellerReport);
}
