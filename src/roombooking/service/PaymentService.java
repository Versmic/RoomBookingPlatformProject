package roombooking.service;

import users.Payment;
import users.PaymentMethod;
import users.PaymentStatus;
import users.PaymentType;
import users.PaymentRepository;
import roombooking.strategy.PaymentProcessor;
import roombooking.strategy.CreditCardStrategy;
import roombooking.strategy.DebitCardStrategy;
import roombooking.strategy.InstitutionalBillingStrategy;

public class PaymentService {
    private PaymentRepository paymentRepo;
    private PaymentProcessor processor;
    
    public PaymentService() {
        this.paymentRepo = new PaymentRepository();
        this.processor = new PaymentProcessor();
    }
    
    public boolean processPayment(int userId, double amount) {
        processor.setStrategy(new CreditCardStrategy("1234-5678-9012-3456", "12/25", "123"));
        return processor.processPayment(amount, userId);
    }
    
    public boolean processRefund(int userId, double amount) {
        return true;
    }
    
    public void savePayment(Payment payment) {
        paymentRepo.savePayment(payment);
    }
}
