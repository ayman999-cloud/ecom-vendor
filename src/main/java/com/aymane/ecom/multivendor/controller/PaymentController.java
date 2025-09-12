package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.controller.response.ApiResponse;
import com.aymane.ecom.multivendor.controller.response.PaymentLinkResponse;
import com.aymane.ecom.multivendor.model.*;
import com.aymane.ecom.multivendor.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;

    @GetMapping("/api/payment/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);

        final PaymentLinkResponse paymentLinkResponse;

        final PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        final boolean paymentSuccess = this.paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);

        if (paymentSuccess) {
            for (Order order : paymentOrder.getOrders()) {
                this.transactionService.createTransaction(order);
                final Seller seller = this.sellerService.getSellerById(order.getSellerId());
                final SellerReport sellerReport = this.sellerReportService.getSellerReport(seller);
                sellerReport.setTotalOrders(sellerReport.getTotalOrders() + 1);
                sellerReport.setTotalEarnings(sellerReport.getTotalEarnings() + order.getTotalSellingPrice());
                sellerReport.setTotalSales(sellerReport.getTotalSales() + order.getOrderItems().size());
                this.sellerReportService.updateSellerReport(sellerReport);
            }
        }

        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Payment Successful");

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
