package roombooking.strategy;

import java.util.ArrayList;

public class CreditCardProcessorStrategy implements PaymentProcessorStrategy{
	
	
	// paymentDetails[0] = card holder name
	// paymentDetails[1] = card number
	// paymentDetails[2] = expiry
	// paymentDetails[3] = CVV
	
	@Override
	public boolean processPayment(ArrayList<String> paymentDetails) {
		boolean name = paymentDetails.get(0).matches("\\S+ \\S+");
		String cardNumbers = paymentDetails.get(1).replaceAll("[ -]", "");
		boolean cardNumber = cardNumbers.matches("\\d{16}");	
		boolean expiry = paymentDetails.get(2).matches("^(0[1-9]|1[0-2])/\\d{2}");
		
		
		boolean pinNumber = paymentDetails.get(3).length() == 3;
		
		if(name && cardNumber && expiry && pinNumber) {
			return true; 
		}
		
		return false;
	}
}
