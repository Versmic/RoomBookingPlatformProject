package roombooking.model;

public class Partner extends RegisteredUser{

	String organizationID;
	
	public Partner(String organizationID) {
		this.organizationID = organizationID;
	}
	
	@Override
	public double getHRate() {
		return 50;
	}

	@Override
	public String getIDNumber() {
		return organizationID;
	}

}
