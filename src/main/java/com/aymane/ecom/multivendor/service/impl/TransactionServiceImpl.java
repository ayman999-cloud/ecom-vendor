package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.model.Order;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.model.Transaction;
import com.aymane.ecom.multivendor.repository.SellerRepository;
import com.aymane.ecom.multivendor.repository.TransactionRepository;
import com.aymane.ecom.multivendor.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {
        final Seller seller = sellerRepository.findById(order.getSellerId()).get();

        final Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);

        return this.transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
        return this.transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return this.transactionRepository.findAll();
    }
}
