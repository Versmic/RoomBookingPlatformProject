package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import roombooking.enums.PaymentMethod;
import roombooking.enums.PaymentType;
import roombooking.model.Payment;
import roombooking.strategy.*;

public class PaymentTester {
	
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores the payment ID
	 */
	@Test
	public void testPaymentId() {
		Payment payment = new Payment("testPayment", null, 0, null, null, null, null);

		assertEquals("testPayment", payment.getPaymentId());
	}
	
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores the booking ID
	 */
	@Test
	public void testBookingId() {
		Payment payment = new Payment(null, "testBooking", 0, null, null, null, null);

		assertEquals("testBooking", payment.getBookingId());
	}
	
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores the payment amount
	 */
	@Test
	public void testPaymentAmount() {
		Payment payment = new Payment(null, null, 20, null, null, null, null);

		assertEquals(20.00, payment.getAmount(), 0.001);
	}
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores the payment date
	 */
	@Test
	public void testPaymentDate() {
		Payment payment = new Payment(null, null, 0, LocalDateTime.of(2026, 12, 1, 9, 30), null, null, null);

		assertEquals(LocalDateTime.of(2026, 12, 1, 9, 30), payment.getPaymentDate());
	}
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores the payment type
	 */
	@Test
	public void testPaymentType() {
		Payment payment = new Payment(null, null, 0, null, PaymentType.DEPOSIT, null, null);

		assertEquals(PaymentType.DEPOSIT, payment.getPaymentType());
	}
	
	
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores a creditcard payment method
	 */
	@Test
	public void testCreditCardPaymentMethod() {
		Payment payment = new Payment(null, null, 0, null, null, PaymentMethod.CREDITCARD, null);

		assertEquals(PaymentMethod.CREDITCARD, payment.getPaymentMethod());
	}
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores a creditcard payment method
	 */
	@Test
	public void testDebitCardPaymentMethod() {
		Payment payment = new Payment(null, null, 0, null, null, PaymentMethod.DEBITCARD, null);

		assertEquals(PaymentMethod.DEBITCARD, payment.getPaymentMethod());
	}
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores a institutional billing payment method
	 */
	@Test
	public void testInstitutionalBillingPaymentMethod() {
		Payment payment = new Payment(null, null, 0, null, null, PaymentMethod.INSTITUTIONALBILLING, null);

		assertEquals(PaymentMethod.INSTITUTIONALBILLING, payment.getPaymentMethod());
	}
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores the payment processor strategy
	 * as credit card processor
	 */
	@Test
	public void testCreditCardProcessorStrategy() {

		Payment payment = new Payment(null, null, 0, null, null, null, new CreditCardProcessorStrategy());

		assertEquals(true, payment.getPaymentProcessorStrategy() instanceof CreditCardProcessorStrategy);
	}
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores the payment processor strategy
	 * as debit card processor
	 */
	@Test
	public void testDebitCardProcessorStrategy() {

		Payment payment = new Payment(null, null, 0, null, null, null, new DebitCardProcessorStrategy());

		assertEquals(true, payment.getPaymentProcessorStrategy() instanceof DebitCardProcessorStrategy);
	}
	
	
	
	/*
	 * this test ensures that the Payment constructor
	 * correctly stores the payment processor strategy
	 * as debit card processor
	 */
	@Test
	public void testInstitutionalBillingProcessorStrategy() {

		Payment payment = new Payment(null, null, 0, null, null, null, new InstitutionalBillingProcessorStrategy());

		assertEquals(true, payment.getPaymentProcessorStrategy() instanceof InstitutionalBillingProcessorStrategy);
	}
	
	
	


}
