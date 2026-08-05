package roombooking.AIassistant;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import roombooking.strategy.CreditCardProcessorStrategy;
import roombooking.strategy.DebitCardProcessorStrategy;
import roombooking.strategy.InstitutionalBillingProcessorStrategy;
import roombooking.strategy.PaymentProcessorStrategy;

public class PaymentStrategyTest {


    @Test
    public void testCreditCardPaymentValid() {

        PaymentProcessorStrategy processor =
                new CreditCardProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("John Smith");
        details.add("1234567890123456");
        details.add("12/26");
        details.add("123");


        boolean result =
                processor.processPayment(details);


        assertTrue(result);
    }



    @Test
    public void testCreditCardPaymentWithSpacesInCardNumber() {

        PaymentProcessorStrategy processor =
                new CreditCardProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("John Smith");
        details.add("1234 5678 9012 3456");
        details.add("01/27");
        details.add("999");


        assertTrue(
                processor.processPayment(details)
        );
    }



    @Test
    public void testCreditCardInvalidCardNumber() {

        PaymentProcessorStrategy processor =
                new CreditCardProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("John Smith");
        details.add("123456789");
        details.add("12/26");
        details.add("123");


        assertFalse(
                processor.processPayment(details)
        );
    }



    @Test
    public void testCreditCardInvalidName() {

        PaymentProcessorStrategy processor =
                new CreditCardProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("John");
        details.add("1234567890123456");
        details.add("12/26");
        details.add("123");


        assertFalse(
                processor.processPayment(details)
        );
    }



    @Test
    public void testCreditCardInvalidExpiry() {

        PaymentProcessorStrategy processor =
                new CreditCardProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("John Smith");
        details.add("1234567890123456");
        details.add("15/26");
        details.add("123");


        assertFalse(
                processor.processPayment(details)
        );
    }



    @Test
    public void testDebitCardPaymentValid() {

        PaymentProcessorStrategy processor =
                new DebitCardProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("Jane Doe");
        details.add("1111222233334444");
        details.add("10/28");
        details.add("1234");


        assertTrue(
                processor.processPayment(details)
        );
    }



    @Test
    public void testDebitCardInvalidPin() {

        PaymentProcessorStrategy processor =
                new DebitCardProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("Jane Doe");
        details.add("1111222233334444");
        details.add("10/28");
        details.add("12");


        assertFalse(
                processor.processPayment(details)
        );
    }



    @Test
    public void testDebitCardInvalidCardNumber() {

        PaymentProcessorStrategy processor =
                new DebitCardProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("Jane Doe");
        details.add("12345");
        details.add("10/28");
        details.add("1234");


        assertFalse(
                processor.processPayment(details)
        );
    }



    @Test
    public void testInstitutionalBillingValid() {

        PaymentProcessorStrategy processor =
                new InstitutionalBillingProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("University");
        details.add("Account Number");
        details.add("Department");
        details.add("Reference");


        assertTrue(
                processor.processPayment(details)
        );
    }



    @Test
    public void testInstitutionalBillingInvalidSize() {

        PaymentProcessorStrategy processor =
                new InstitutionalBillingProcessorStrategy();


        ArrayList<String> details = new ArrayList<>();

        details.add("University");
        details.add("Account Number");


        assertFalse(
                processor.processPayment(details)
        );
    }
}