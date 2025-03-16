package com.classes.classesService.model;


public class Payment {
    private double amount; // The amount of the payment

    // Constructor to initialize a Payment object with a specific amount
    public Payment(double amount) {
        this.amount = amount;
    }

    // Getter method to retrieve the payment amount
    public double getAmount() {
        return amount;
    }

    // Setter method to update the payment amount
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Override the toString method to provide a string representation of the Payment object
    @Override
    public String toString() {
        return "Payment{" +
                "amount=" + amount +
                '}';
    }
}
