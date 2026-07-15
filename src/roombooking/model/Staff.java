package roombooking.model;

public class Staff extends RegisteredUser{
	private String staffNumber;
	
	public Staff(String staffNumber) {
		this.staffNumber = staffNumber;
	}
	
	@Override
	public double getHRate() {
		return 40;
	}

	@Override
	public String getIDNumber() {
		return staffNumber;
	}

}
