package com.jgcvi.revolut.service;

import com.jgcvi.revolut.dao.Customer;
import com.jgcvi.revolut.dao.InMemoryDB;
import com.jgcvi.revolut.dao.Transfer;

public class MainService {
    // need to implement a queue to that'll block until a lock takes place on user
    public static Customer createUser(String name, double balance) {
        return InMemoryDB.createUser(name, balance);
    }

    public static Customer transfer(Transfer transfer) {
        Customer fromCustomer = InMemoryDB.get(transfer.getFrom());
        Customer toCustomer = InMemoryDB.get(transfer.getTo());

        fromCustomer.lock();
        toCustomer.lock();

        if(fromCustomer.getBalance() - transfer.getAmount() >= 0) {
            InMemoryDB.get(transfer.getFrom()).setBalance(-transfer.getAmount());
            InMemoryDB.get(transfer.getTo()).setBalance(transfer.getAmount());
        }

        fromCustomer.unlock();
        toCustomer.unlock();

        return fromCustomer;
    }

}
