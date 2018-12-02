package com.jgcvi.revolut.dao;

public class Transfer {
    private int from, to;
    private double amount;
    public Transfer(int from, int to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public double getAmount() {
        return amount;
    }
}
