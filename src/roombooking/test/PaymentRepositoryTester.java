package roombooking.test;

import org.junit.Test;

import roombooking.enums.PaymentMethod;
import roombooking.enums.PaymentType;
import roombooking.model.Payment;
import roombooking.repository.PaymentRepository;
import roombooking.strategy.CreditCardProcessorStrategy;
import roombooking.strategy.DebitCardProcessorStrategy;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

public class PaymentRepositoryTester {
	/*
	 * this test ensures that savePayment() saves a payment
	 * to our database and saves the correct information
	 */
	@Test
	public void savePayment() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		
		LocalDateTime paymentDate = LocalDateTime.of(2026, 12, 25, 9, 0);
		
		Payment payment = new Payment("testPayment", "testBooking", 20.00, paymentDate, PaymentType.DEPOSIT, PaymentMethod.CREDITCARD, new CreditCardProcessorStrategy());
		
		int initialPaymentsAmount = paymentRepo.getAllPayments().size();
		
		try {
			paymentRepo.savePayment(payment);
			
			List<Payment> currentPayments = paymentRepo.getAllPayments();
			Payment savedPayment = paymentRepo.findPaymentById(payment.getPaymentId());
			
			assertEquals(initialPaymentsAmount + 1, currentPayments.size());
			assertEquals("testPayment", savedPayment.getPaymentId());
			assertEquals("testBooking", savedPayment.getBookingId());
			assertEquals(20.00, savedPayment.getAmount(), 0.001);
			assertEquals(paymentDate, savedPayment.getPaymentDate());
			assertEquals(PaymentType.values()[0], savedPayment.getPaymentType());
			assertEquals(PaymentMethod.CREDITCARD, savedPayment.getPaymentMethod());
		}
		
		finally {
			paymentRepo.deletePayment(payment.getPaymentId());
		}
	}
	
	
	
	/*
	 * this test ensures that saving a null payment
	 * does not add anything to the payment database
	 */
	@Test
	public void saveNullPayment() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		Payment payment = null;
		
		int initialPaymentsAmount = paymentRepo.getAllPayments().size();
		
		paymentRepo.savePayment(payment);
		
		assertEquals(initialPaymentsAmount, paymentRepo.getAllPayments().size());
	}
	
	
	
	/*
	 * this test ensures that saving a duplicate payment
	 * throws an IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saveDuplicatePayment() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		
		LocalDateTime paymentDate = LocalDateTime.of(2026, 12, 25, 9, 0);
		
		Payment payment = new Payment("testPayment", "testBooking", 20.00, paymentDate, PaymentType.DEPOSIT, PaymentMethod.CREDITCARD, new CreditCardProcessorStrategy());
		
		try {
			paymentRepo.savePayment(payment);
			paymentRepo.savePayment(payment);
		}
		
		finally {
			paymentRepo.deletePayment(payment.getPaymentId());
		}
	}
	
	
	
	
	/*
	 * this test ensures that updatePayment()
	 * properly updates a payment in the database
	 */
	@Test
	public void updatePayment() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		
		LocalDateTime paymentDate = LocalDateTime.of(2026, 12, 25, 9, 0);
		
		Payment payment = new Payment("testPayment", "testBooking", 20.00, paymentDate, PaymentType.DEPOSIT, PaymentMethod.CREDITCARD, new CreditCardProcessorStrategy());
		
		try {
			paymentRepo.savePayment(payment);
			
			LocalDateTime updatedDate = LocalDateTime.of(2026, 12, 26, 10, 0);
			
			payment = new Payment("testPayment", "updatedBooking", 50.00, updatedDate, PaymentType.DEPOSIT, PaymentMethod.DEBITCARD, new DebitCardProcessorStrategy());
			
			paymentRepo.updatePayment(payment);
			
			Payment updatedPayment = paymentRepo.findPaymentById(payment.getPaymentId());
			
			assertEquals("testPayment", updatedPayment.getPaymentId());
			assertEquals("updatedBooking", updatedPayment.getBookingId());
			assertEquals(50.00, updatedPayment.getAmount(), 0.001);
			assertEquals(updatedDate, updatedPayment.getPaymentDate());
			assertEquals(PaymentType.values()[0], updatedPayment.getPaymentType());
			assertEquals(PaymentMethod.DEBITCARD, updatedPayment.getPaymentMethod());
		}
		
		finally {
			paymentRepo.deletePayment(payment.getPaymentId());
		}
	}
	
	
	/*
	 * this test ensures that findPaymentById()
	 * correctly finds the proper payment from our database
	 */
	@Test
	public void findPayment() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		
		LocalDateTime paymentDate = LocalDateTime.of(2026, 12, 25, 9, 0);
		
		Payment payment = new Payment("testPayment", "testBooking", 20.00, paymentDate, PaymentType.DEPOSIT, PaymentMethod.CREDITCARD, new CreditCardProcessorStrategy());
		
		try {
			paymentRepo.savePayment(payment);
			
			Payment foundPayment = paymentRepo.findPaymentById(payment.getPaymentId());
			
			assertEquals(payment.getPaymentId(), foundPayment.getPaymentId());
			assertEquals(payment.getBookingId(), foundPayment.getBookingId());
			assertEquals(payment.getAmount(), foundPayment.getAmount(), 0.001);
			assertEquals(payment.getPaymentDate(), foundPayment.getPaymentDate());
			assertEquals(payment.getPaymentType(), foundPayment.getPaymentType());
			assertEquals(payment.getPaymentMethod(), foundPayment.getPaymentMethod());
		}
		
		finally {
			paymentRepo.deletePayment(payment.getPaymentId());
		}
	}
	
	
	/*
	 * this test ensures that findPaymentById()
	 * returns null when we pass a null id
	 */
	@Test
	public void findNullPayment() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		
		assertEquals(null, paymentRepo.findPaymentById(null));
	}
	
	
	/*
	 * this test ensures that findPaymentById()
	 * returns null when we pass an id that DNE
	 */
	@Test
	public void findDNEPayment() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		
		assertEquals(null, paymentRepo.findPaymentById("!dawifma0292@#!"));
	}
	
	
	/*
	 * this test ensures that findPaymentsByBookingId()
	 * returns all payments belonging to the correct booking
	 */
	@Test
	public void findPaymentsByBookingId() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		
		LocalDateTime firstPaymentDate = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime secondPaymentDate = LocalDateTime.of(2026, 12, 25, 10, 0);
		
		Payment firstPayment = new Payment("testPaymentOne", "testBooking", 20.00, firstPaymentDate, PaymentType.DEPOSIT, PaymentMethod.CREDITCARD, new CreditCardProcessorStrategy());
		
		Payment secondPayment = new Payment("testPaymentTwo", "testBooking", 40.00, secondPaymentDate, PaymentType.DEPOSIT, PaymentMethod.DEBITCARD, new DebitCardProcessorStrategy());
		
		try {
			paymentRepo.savePayment(firstPayment);
			paymentRepo.savePayment(secondPayment);
			
			List<Payment> foundPayments = paymentRepo.findPaymentsByBookingId("testBooking");
			
			assertEquals(2, foundPayments.size());
			assertEquals("testBooking", foundPayments.get(0).getBookingId());
			assertEquals("testBooking", foundPayments.get(1).getBookingId());
		}
		
		finally {
			paymentRepo.deletePayment(firstPayment.getPaymentId());
			paymentRepo.deletePayment(secondPayment.getPaymentId());
		}
	}
	
	
	/*
	 * this test ensures that getAllPayments()
	 * returns a list of all payments from our database
	 */
	@Test
	public void getAllPayments() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		
		LocalDateTime paymentDate = LocalDateTime.of(2026, 12, 25, 9, 0);
		
		Payment payment = new Payment("testPayment", "testBooking", 20.00, paymentDate, PaymentType.values()[0], PaymentMethod.CREDITCARD, new CreditCardProcessorStrategy());
		
		int initialPaymentsAmount = paymentRepo.getAllPayments().size();
		
		try {
			paymentRepo.savePayment(payment);
			
			assertEquals(initialPaymentsAmount + 1, paymentRepo.getAllPayments().size());
		}
		
		finally {
			paymentRepo.deletePayment(payment.getPaymentId());
		}
	}
	
	
	
	/*
	 * this test ensures that deletePayment()
	 * correctly deletes the proper payment from our database
	 */
	@Test
	public void deletePayment() {
		
		PaymentRepository paymentRepo = new PaymentRepository();
		
		LocalDateTime paymentDate = LocalDateTime.of(2026, 12, 25, 9, 0);
		
		Payment payment = new Payment("testPayment", "testBooking", 20.00, paymentDate, PaymentType.DEPOSIT, PaymentMethod.CREDITCARD, new CreditCardProcessorStrategy());
		
		int initialPaymentsAmount = paymentRepo.getAllPayments().size();
		
		paymentRepo.savePayment(payment);
		paymentRepo.deletePayment(payment.getPaymentId());
		
		assertEquals(initialPaymentsAmount, paymentRepo.getAllPayments().size());
		assertEquals(null, paymentRepo.findPaymentById(payment.getPaymentId()));
	}
	
	
	

}
