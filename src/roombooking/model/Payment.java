package roombooking.model;

import java.time.LocalDateTime;

import roombooking.enums.PaymentMethod;
import roombooking.enums.PaymentType;
import roombooking.strategy.PaymentProcessorStrategy;

// stores one payment made for a booking
public class Payment {

    private String paymentId;
    private String bookingId;
    private double amount;
    private LocalDateTime paymentDate;
    private PaymentType paymentType;
    private PaymentMethod paymentMethod;
    private PaymentProcessorStrategy paymentProcessor;

    public Payment(String paymentId, String bookingId, double amount, LocalDateTime paymentDate, PaymentType paymentType, 
    		PaymentMethod paymentMethod, PaymentProcessorStrategy paymentProcessor) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.paymentMethod = paymentMethod;
        this.paymentProcessor = paymentProcessor;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public PaymentProcessorStrategy getPaymentProcessorStrategy() {
    	return paymentProcessor;
    }
}