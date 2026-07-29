package roombooking.model;

public class Student extends RegisteredUser{
	private String studentNumber;
	@Override
	public double getHRate() {
		return 20;
	}
	
	public Student(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	@Override
	public String getIDNumber() {
		return studentNumber;
	}

}
