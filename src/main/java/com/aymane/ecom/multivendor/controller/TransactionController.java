package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.exception.SellerException;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.model.Transaction;
import com.aymane.ecom.multivendor.service.SellerService;
import com.aymane.ecom.multivendor.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final SellerService sellerService;

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionBySeller(@RequestHeader("Authorization") String jwt) throws SellerException {
        final Seller seller = this.sellerService.getSellerProfile(jwt);
        final List<Transaction> transactions = this.transactionService.getTransactionsBySellerId(seller);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        final List<Transaction> transactions = this.transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
