package roombooking.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import roombooking.controller.PaymentController;
import roombooking.enums.*;
import roombooking.model.*;
import roombooking.repository.*;

public class PaymentControllerTester {
	/*
	 * this test checks payment controllers processPayment() method
	 * and ensures it returns false when receiving a negative amount to pay
	 */
	@Test
	public void testProcessPaymentRejectsNegativeAmount() {
		PaymentController paymentController = new PaymentController();
		
		// empty array list with no payment details
		ArrayList<String> paymentDetails = new ArrayList<>();
		// add valid credit card payment details
		paymentDetails.add("firstname lastname");
		paymentDetails.add("1111111111111111");
		paymentDetails.add("12/30");
		paymentDetails.add("123");
		// call processPayment on a negative amount
		boolean result = paymentController.processPayment(-20.00, PaymentMethod.CREDITCARD, paymentDetails);
		// expect the method to return false
		assertEquals(false, result);
	}
	
	
	/*
	 * this test checks payment controllers processPayment() method
	 * and ensures a valid credit card payment is processed
	 */
	@Test
	public void testProcessValidCreditCardPayment() {
		PaymentController paymentController = new PaymentController();

		// add valid credit card payment details
		ArrayList<String> paymentDetails = new ArrayList<>();
		paymentDetails.add("firstname lastname");
		paymentDetails.add("1111111111111111");
		paymentDetails.add("12/30");
		paymentDetails.add("123");
		// call processPayment() on valid credit card
		boolean result = paymentController.processPayment(20.00,PaymentMethod.CREDITCARD,paymentDetails);
		
		assertEquals(true, result);
	}
	
	
	/*
	 * this test checks payment controllers processPayment() method
	 * and ensures invalid credit card details are rejected
	 */
	@Test
	public void testProcessInvalidCreditCardPayment() {
		PaymentController paymentController = new PaymentController();

		// invalid credit card payment details
		ArrayList<String> paymentDetails = new ArrayList<>();
		paymentDetails.add("firstname lastname");
		paymentDetails.add("1234");
		paymentDetails.add("invalid date");
		paymentDetails.add("1");
		// call process payment with invalid credit card
		boolean result = paymentController.processPayment(20.00, PaymentMethod.CREDITCARD, paymentDetails);

		assertEquals(false, result);
	}
	
	
	
	
	/*
	 * this test checks payment controllers processPayment() method
	 * and ensures a valid debit card payment is processed
	 */
	@Test
	public void testProcessValidDebitCardPayment() {
		PaymentController paymentController = new PaymentController();
		
		//add valid debit card payment details
		ArrayList<String> paymentDetails = new ArrayList<>();
		paymentDetails.add("firstname lastname");
		paymentDetails.add("1111111111111111");
		paymentDetails.add("12/30");
		paymentDetails.add("1234");
		// call process payment on valid debit card
		boolean result = paymentController.processPayment(20.00, PaymentMethod.DEBITCARD, paymentDetails);

		assertEquals(true, result);
	}
	
	
	/*
	 * this test checks payment controllers processPayment() method
	 * and ensures invalid debit card details are rejected
	 */
	@Test
	public void testProcessInvalidDebitCardPayment() {
		PaymentController paymentController = new PaymentController();

		// invalid debit card payment details
		ArrayList<String> paymentDetails = new ArrayList<>();
		paymentDetails.add("firstname");
		paymentDetails.add("1234");
		paymentDetails.add("invalid date");
		paymentDetails.add("30/20");
		// call process payment on invalid debit card
		boolean result = paymentController.processPayment(20.00, PaymentMethod.DEBITCARD, paymentDetails);

		assertEquals(false, result);
	}
	
	
	/*
	 * this test checks payment controllers processPayment() method
	 * and ensures a valid institutional billing payment is processed
	 */
	@Test
	public void testProcessValidInstitutionalBillingPayment() {
		PaymentController paymentController = new PaymentController();
		
		//add valid institutional billing payment details
		ArrayList<String> paymentDetails = new ArrayList<>();
		paymentDetails.add("institution name");
		paymentDetails.add("12345");
		paymentDetails.add("PO-12345");
		paymentDetails.add("boss@gmail.com");
		// call process payment on valid institutional billing
		boolean result = paymentController.processPayment(20.00, PaymentMethod.INSTITUTIONALBILLING, paymentDetails);

		assertEquals(true, result);
	}
	
	
	/*
	 * this test checks payment controllers processPayment() method
	 * and ensures invalid institutional billing payment are rejected
	 */
	@Test
	public void testProcessInvalidInstitutionalBillingPayment() {
		PaymentController paymentController = new PaymentController();
		
		//add valid institutional billing payment details
		ArrayList<String> paymentDetails = new ArrayList<>();
		paymentDetails.add("institution name");
		paymentDetails.add("12345");
		paymentDetails.add("PO-12345");
		paymentDetails.add("boss@gmail.com");
		paymentDetails.add("extra invalid info");
		// call process payment on invalid institutional billing
		boolean result = paymentController.processPayment(20.00, PaymentMethod.INSTITUTIONALBILLING, paymentDetails);

		assertEquals(false, result);
	}
	
	
	
	

	/*
	 * this test checks payment controllers storePayment() method
	 * and ensures it returns and stores the expected credit card payment
	 */
	@Test
	public void testStoreCreditCardPayment() {
		PaymentController paymentController = new PaymentController();
		// paymentRepo to access payment database
		PaymentRepository paymentRepo = new PaymentRepository();

		Payment payment = null;

		try {
			
			String actualPaymentId = paymentRepo.generatePaymentId();
			// store the payment
			payment = paymentController.storePayment(
					"testBookingId", 20.00, PaymentType.DEPOSIT, PaymentMethod.CREDITCARD);

			// check the returned payment information
			assertEquals(actualPaymentId, payment.getPaymentId());
			assertEquals("testBookingId", payment.getBookingId());
			assertEquals(20.00, payment.getAmount(), 0.001);
			assertEquals(PaymentType.DEPOSIT, payment.getPaymentType());
			assertEquals(PaymentMethod.CREDITCARD, payment.getPaymentMethod());

			// get all payments currently stored in the repository
			List<Payment> payments = paymentRepo.getAllPayments();

			// check if the new payment was actually stored
			assertEquals(actualPaymentId, payments.get(payments.size() - 1).getPaymentId());

		} 
		finally {
			// delete the test payment if it was created
			paymentRepo.deletePayment(payment.getPaymentId());
		}
	}
	
	/*
	 * this test checks payment controllers storePayment() method
	 * and ensures it returns and stores the expected debit card payment
	 */
	@Test
	public void testStoreDebitCardPayment() {
		PaymentController paymentController = new PaymentController();
		// paymentRepo to access payment database
		PaymentRepository paymentRepo = new PaymentRepository();

		Payment payment = null;

		try {
			
			String actualPaymentId = paymentRepo.generatePaymentId();
			// store the payment
			payment = paymentController.storePayment(
					"testBookingId", 20.00, PaymentType.DEPOSIT, PaymentMethod.DEBITCARD);

			// check the returned payment information
			assertEquals(actualPaymentId, payment.getPaymentId());
			assertEquals("testBookingId", payment.getBookingId());
			assertEquals(20.00, payment.getAmount(), 0.001);
			assertEquals(PaymentType.DEPOSIT, payment.getPaymentType());
			assertEquals(PaymentMethod.DEBITCARD, payment.getPaymentMethod());

			// get all payments currently stored in the repository
			List<Payment> payments = paymentRepo.getAllPayments();

			// check if the new payment was actually stored
			assertEquals(actualPaymentId, payments.get(payments.size() - 1).getPaymentId());

		} 
		finally {
			// delete the test payment if it was created
			paymentRepo.deletePayment(payment.getPaymentId());
		}
	}
	
	
	/*
	 * this test checks payment controllers storePayment() method
	 * and ensures it returns and stores the expected institutional billing payment
	 */
	@Test
	public void testStoreInstitutionalBillingPayment() {
		PaymentController paymentController = new PaymentController();
		// paymentRepo to access payment database
		PaymentRepository paymentRepo = new PaymentRepository();

		Payment payment = null;

		try {
			
			String actualPaymentId = paymentRepo.generatePaymentId();
			// store the payment
			payment = paymentController.storePayment(
					"testBookingId", 20.00, PaymentType.DEPOSIT, PaymentMethod.INSTITUTIONALBILLING);

			// check the returned payment information
			assertEquals(actualPaymentId, payment.getPaymentId());
			assertEquals("testBookingId", payment.getBookingId());
			assertEquals(20.00, payment.getAmount(), 0.001);
			assertEquals(PaymentType.DEPOSIT, payment.getPaymentType());
			assertEquals(PaymentMethod.INSTITUTIONALBILLING, payment.getPaymentMethod());

			// get all payments currently stored in the repository
			List<Payment> payments = paymentRepo.getAllPayments();

			// check if the new payment was actually stored
			assertEquals(actualPaymentId, payments.get(payments.size() - 1).getPaymentId());

		} 
		finally {
			// delete the test payment if it was created
			paymentRepo.deletePayment(payment.getPaymentId());
		}
	}
	
	

}
