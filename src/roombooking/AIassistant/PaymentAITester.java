package roombooking.AIassistant;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import roombooking.enums.PaymentMethod;
import roombooking.enums.PaymentType;
import roombooking.model.Payment;
import roombooking.strategy.CreditCardProcessorStrategy;
import roombooking.strategy.PaymentProcessorStrategy;


public class PaymentAITester {


    private Payment payment;
    private LocalDateTime paymentDate;
    private PaymentProcessorStrategy processor;


    @Before
    public void setUp() {

        paymentDate = LocalDateTime.of(
                2026,
                8,
                10,
                12,
                0
        );


        processor = new CreditCardProcessorStrategy();


        payment = new Payment(
                "P001",
                "B001",
                100.00,
                paymentDate,
                PaymentType.DEPOSIT,
                PaymentMethod.CREDITCARD,
                processor
        );
    }



    @Test
    public void testPaymentConstructor() {

        assertEquals("P001", payment.getPaymentId());
        assertEquals("B001", payment.getBookingId());
        assertEquals(100.00, payment.getAmount(), 0.01);
        assertEquals(paymentDate, payment.getPaymentDate());
        assertEquals(PaymentType.DEPOSIT, payment.getPaymentType());
        assertEquals(PaymentMethod.CREDITCARD, payment.getPaymentMethod());
        assertEquals(processor, payment.getPaymentProcessorStrategy());
    }



    @Test
    public void testGetPaymentId() {

        assertEquals(
                "P001",
                payment.getPaymentId()
        );
    }



    @Test
    public void testGetBookingId() {

        assertEquals(
                "B001",
                payment.getBookingId()
        );
    }



    @Test
    public void testGetAmount() {

        assertEquals(
                100.00,
                payment.getAmount(),
                0.01
        );
    }



    @Test
    public void testGetPaymentType() {

        assertEquals(
                PaymentType.DEPOSIT,
                payment.getPaymentType()
        );
    }



    @Test
    public void testGetPaymentMethod() {

        assertEquals(
                PaymentMethod.CREDITCARD,
                payment.getPaymentMethod()
        );
    }



    @Test
    public void testGetProcessor() {

        assertNotNull(
                payment.getPaymentProcessorStrategy()
        );
    }

}