package roombooking.AIassistant;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import roombooking.controller.PaymentController;
import roombooking.enums.PaymentMethod;
import roombooking.enums.PaymentType;
import roombooking.model.Payment;
import roombooking.repository.PaymentRepository;


public class PaymentControllerTest {


    private PaymentController controller;
    private PaymentRepository repository;



    @Before
    public void setUp() {

        controller = new PaymentController();

        repository = new PaymentRepository();

    }



    @Test
    public void testProcessCreditCardPaymentSuccessful() {


        ArrayList<String> details =
                new ArrayList<>();

        details.add("4111111111111111");
        details.add("123");
        details.add("12/30");



        boolean result =
                controller.processPayment(
                        100,
                        PaymentMethod.CREDITCARD,
                        details
                );



        assertTrue(result);

    }




    @Test
    public void testProcessDebitCardPaymentSuccessful() {


        ArrayList<String> details =
                new ArrayList<>();

        details.add("5555555555554444");
        details.add("123");



        boolean result =
                controller.processPayment(
                        50,
                        PaymentMethod.DEBITCARD,
                        details
                );



        assertTrue(result);

    }





    @Test
    public void testProcessInstitutionalBillingPaymentSuccessful() {


        ArrayList<String> details =
                new ArrayList<>();

        details.add("University Account");



        boolean result =
                controller.processPayment(
                        200,
                        PaymentMethod.INSTITUTIONALBILLING,
                        details
                );



        assertTrue(result);

    }





    @Test
    public void testNegativeAmountPaymentFails() {


        ArrayList<String> details =
                new ArrayList<>();


        boolean result =
                controller.processPayment(
                        -50,
                        PaymentMethod.CREDITCARD,
                        details
                );



        assertFalse(result);

    }





    @Test
    public void testZeroAmountPayment() {


        ArrayList<String> details =
                new ArrayList<>();



        boolean result =
                controller.processPayment(
                        0,
                        PaymentMethod.CREDITCARD,
                        details
                );



        assertTrue(result);

    }





    @Test
    public void testStorePayment() {


        Payment payment =
                controller.storePayment(
                        "B001",
                        100,
                        PaymentType.DEPOSIT,
                        PaymentMethod.CREDITCARD
                );



        assertNotNull(payment);



        assertEquals(
                "B001",
                payment.getBookingId()
        );



        assertEquals(
                100,
                payment.getAmount(),
                0.01
        );



        assertEquals(
                PaymentType.DEPOSIT,
                payment.getPaymentType()
        );



        assertEquals(
                PaymentMethod.CREDITCARD,
                payment.getPaymentMethod()
        );

    }





    @Test
    public void testStoreFinalPayment() {


        Payment payment =
                controller.storePayment(
                        "B002",
                        300,
                        PaymentType.FINALPAYMENT,
                        PaymentMethod.DEBITCARD
                );



        assertEquals(
                PaymentType.FINALPAYMENT,
                payment.getPaymentType()
        );



        assertEquals(
                PaymentMethod.DEBITCARD,
                payment.getPaymentMethod()
        );

    }





    @Test
    public void testPaymentProcessorIsCreated() {


        Payment payment =
                controller.storePayment(
                        "B003",
                        150,
                        PaymentType.DEPOSIT,
                        PaymentMethod.INSTITUTIONALBILLING
                );



        assertNotNull(
                payment.getPaymentProcessorStrategy()
        );

    }





    @Test
    public void testGeneratedPaymentIdFormat() {


        Payment payment =
                controller.storePayment(
                        "B004",
                        50,
                        PaymentType.DEPOSIT,
                        PaymentMethod.CREDITCARD
                );



        assertTrue(
                payment.getPaymentId()
                .matches("P\\d{3}")
        );

    }

}