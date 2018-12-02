package com.jgcvi.revolut.dao;

public class Customer {
    int id;
    private String name;
    private double balance;

    public Customer(int id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public int getId() { return id; }

    public double getBalance() {
        return balance;
    }

    public boolean setBalance(double delta) {
        synchronized (this) {
            if(balance + delta < 0) {
                return false;
            }
        }

        this.balance += delta;

        return true;
    }

    public String getName() {
        return name;
    }
}
