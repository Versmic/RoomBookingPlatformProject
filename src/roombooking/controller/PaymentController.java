package roombooking.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import roombooking.enums.PaymentMethod;
import roombooking.enums.PaymentType;
import roombooking.model.Payment;
import roombooking.repository.PaymentRepository;
import roombooking.strategy.CreditCardProcessorStrategy;
import roombooking.strategy.DebitCardProcessorStrategy;
import roombooking.strategy.InstitutionalBillingProcessorStrategy;
import roombooking.strategy.PaymentProcessorStrategy;

// handles payment creation and processing
public class PaymentController {

    private final PaymentRepository paymentRepository = new PaymentRepository();                                 
    
    public boolean processPayment(double amount, PaymentMethod paymentMethod, ArrayList<String> paymentDetails) {
    	PaymentProcessorStrategy paymentProcessor = createPaymentProcessor(paymentMethod);
    	return paymentProcessor.processPayment(paymentDetails);   	
    }

    // creates, processes and saves a payment
    public Payment storePayment(String bookingId, double amount, PaymentType paymentType, PaymentMethod paymentMethod) {
        PaymentProcessorStrategy paymentProcessor = createPaymentProcessor(paymentMethod);

        Payment payment = new Payment(paymentRepository.generatePaymentId(), bookingId, amount,
                LocalDateTime.now(), paymentType, paymentMethod, paymentProcessor
        );
        paymentRepository.savePayment(payment);
        return payment;
    }

    // creates the correct payment processor
    private PaymentProcessorStrategy createPaymentProcessor(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case CREDITCARD -> new CreditCardProcessorStrategy();
            case DEBITCARD -> new DebitCardProcessorStrategy();
            case INSTITUTIONALBILLING -> new InstitutionalBillingProcessorStrategy();
        };
    }
}