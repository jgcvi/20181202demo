package com.jgcvi.revolut.dao;

import java.util.HashMap;

public class InMemoryDB {
    private static int sequenceId = 0;
    private static HashMap<Integer, Customer> db = new HashMap<>();

    public static Customer get(int id) {
        return db.get(id);
    }

    public static Customer createUser(String name, double balance) {
        int id = sequenceId ++;
        db.put(id, new Customer(id, name, balance));
        return db.get(id);
    }
}
