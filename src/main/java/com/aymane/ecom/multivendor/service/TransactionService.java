package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.model.Order;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);

    List<Transaction> getTransactionsBySellerId(Seller seller);

    List<Transaction> getAllTransactions();
}
