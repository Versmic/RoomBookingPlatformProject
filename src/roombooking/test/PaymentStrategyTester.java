package roombooking.test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import roombooking.strategy.CreditCardProcessorStrategy;
import roombooking.strategy.DebitCardProcessorStrategy;
import roombooking.strategy.InstitutionalBillingProcessorStrategy;

public class PaymentStrategyTester {
	/*
	 * this test ensures that CreditCardProcessorStrategy
	 * accepts valid credit card information
	 */
	@Test
	public void validCreditCardPayment() {
		
		CreditCardProcessorStrategy paymentProcessor = new CreditCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test name");
		paymentDetails.add("1234 5678 9012 3456");
		paymentDetails.add("12/28");
		paymentDetails.add("123");
		
		assertEquals(true, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that CreditCardProcessorStrategy
	 * rejects a card holder name without a last name
	 */
	@Test
	public void invalidCreditCardName() {
		
		CreditCardProcessorStrategy paymentProcessor = new CreditCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test");
		paymentDetails.add("1234 5678 9012 3456");
		paymentDetails.add("12/28");
		paymentDetails.add("123");
		
		assertEquals(false, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that CreditCardProcessorStrategy
	 * rejects an invalid credit card number
	 */
	@Test
	public void invalidCreditCardNumber() {
		
		CreditCardProcessorStrategy paymentProcessor = new CreditCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test name");
		paymentDetails.add("1234 5678");
		paymentDetails.add("12/28");
		paymentDetails.add("123");
		
		assertEquals(false, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that CreditCardProcessorStrategy
	 * rejects an invalid expiry date
	 */
	@Test
	public void invalidCreditCardExpiry() {
		
		CreditCardProcessorStrategy paymentProcessor = new CreditCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test name");
		paymentDetails.add("1234 5678 9012 3456");
		paymentDetails.add("15/28");
		paymentDetails.add("123");
		
		assertEquals(false, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that CreditCardProcessorStrategy
	 * rejects an invalid CVV
	 */
	@Test
	public void invalidCreditCardCVV() {
		
		CreditCardProcessorStrategy paymentProcessor = new CreditCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test name");
		paymentDetails.add("1234 5678 9012 3456");
		paymentDetails.add("12/28");
		paymentDetails.add("12");
		
		assertEquals(false, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that DebitCardProcessorStrategy
	 * accepts valid debit card information
	 */
	@Test
	public void validDebitCardPayment() {
		
		DebitCardProcessorStrategy paymentProcessor = new DebitCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test name");
		paymentDetails.add("1234-5678-9012-3456");
		paymentDetails.add("12/28");
		paymentDetails.add("1234");
		
		assertEquals(true, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that DebitCardProcessorStrategy
	 * rejects a card holder name without a last name
	 */
	@Test
	public void invalidDebitCardName() {
		
		DebitCardProcessorStrategy paymentProcessor = new DebitCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test");
		paymentDetails.add("1234-5678-9012-3456");
		paymentDetails.add("12/28");
		paymentDetails.add("1234");
		
		assertEquals(false, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that DebitCardProcessorStrategy
	 * rejects an invalid debit card number
	 */
	@Test
	public void invalidDebitCardNumber() {
		
		DebitCardProcessorStrategy paymentProcessor = new DebitCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test name");
		paymentDetails.add("1234-5678");
		paymentDetails.add("12/28");
		paymentDetails.add("1234");
		
		assertEquals(false, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that DebitCardProcessorStrategy
	 * rejects an invalid expiry date
	 */
	@Test
	public void invalidDebitCardExpiry() {
		
		DebitCardProcessorStrategy paymentProcessor = new DebitCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test name");
		paymentDetails.add("1234-5678-9012-3456");
		paymentDetails.add("00/28");
		paymentDetails.add("1234");
		
		assertEquals(false, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that DebitCardProcessorStrategy
	 * rejects an invalid pin number
	 */
	@Test
	public void invalidDebitCardPin() {
		
		DebitCardProcessorStrategy paymentProcessor = new DebitCardProcessorStrategy();
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test name");
		paymentDetails.add("1234-5678-9012-3456");
		paymentDetails.add("12/28");
		paymentDetails.add("123");
		
		assertEquals(false, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that InstitutionalBillingProcessorStrategy
	 * accepts payment information containing four values
	 */
	@Test
	public void validInstitutionalBillingPayment() {
		
		InstitutionalBillingProcessorStrategy paymentProcessor = new InstitutionalBillingProcessorStrategy();
		
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test institution name");
		paymentDetails.add("test account number");
		paymentDetails.add("test purchase order");
		paymentDetails.add("test authorized by");
		
		assertEquals(true, paymentProcessor.processPayment(paymentDetails));
	}
	
	
	/*
	 * this test ensures that InstitutionalBillingProcessorStrategy
	 * rejects payment information with fewer than four values
	 */
	@Test
	public void invalidInstitutionalBillingDetails() {
		
		InstitutionalBillingProcessorStrategy paymentProcessor =
				new InstitutionalBillingProcessorStrategy();
		
		ArrayList<String> paymentDetails = new ArrayList<>();
		
		paymentDetails.add("test institution name");
		paymentDetails.add("test account number");
		paymentDetails.add("test purchase order");
		
		assertEquals(false, paymentProcessor.processPayment(paymentDetails));
	}
	

}
