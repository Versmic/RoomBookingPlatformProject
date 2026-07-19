package roombooking.strategy;

import java.util.ArrayList;

public interface PaymentProcessorStrategy {
	public boolean processPayment(ArrayList<String> paymentDetails);
}
