package roombooking.model;

public class Faculty extends RegisteredUser{
	private String facultyNumber;
	
	public Faculty(String facultyNumber) {
		this.facultyNumber = facultyNumber;
	}
	@Override
	public double getHRate() {
		return 30;
	}

	@Override
	public String getIDNumber() {
		return facultyNumber;
	}

}
