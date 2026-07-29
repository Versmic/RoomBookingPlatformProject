package roombooking.strategy;

import java.util.ArrayList;

public class InstitutionalBillingProcessorStrategy implements PaymentProcessorStrategy{
	
	@Override
	public boolean processPayment(ArrayList<String> paymentDetails) {
		if (paymentDetails.size() != 4) return false;
		else return true;
	}
}
