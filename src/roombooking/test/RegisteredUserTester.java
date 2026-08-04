package roombooking.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import roombooking.model.*;

public class RegisteredUserTester {
	
	/*
	 * this test ensures that the Staff constructor
	 * correctly stores the staff ID
	 */
	@Test
	public void testStaffIdNumber() {
		Staff staff = new Staff("staffTest");

		assertEquals("staffTest", staff.getIDNumber());
	}

	/*
	 * this test ensures that Staff users
	 * have the correct hourly rate
	 */
	@Test
	public void testStaffHourlyRate() {
		Staff staff = new Staff("staffTest");

		assertEquals(40.00, staff.getHRate(), 0.001);
	}
	
	/*
	 * this test ensures that the Faculty constructor
	 * correctly stores the faculty ID
	 */
	@Test
	public void testFacultyIdNumber() {
		Faculty faculty = new Faculty("facultyTest");

		assertEquals("facultyTest", faculty.getIDNumber());
	}

	/*
	 * this test ensures that Faculty users
	 * have the correct hourly rate
	 */
	@Test
	public void testFacultyHourlyRate() {
		Faculty faculty = new Faculty("facultyTest");

		assertEquals(30.00, faculty.getHRate(), 0.001);
	}
	
	
	/*
	 * this test ensures that the Student constructor
	 * correctly stores the student ID
	 */
	@Test
	public void testStudentIdNumber() {
		Student student = new Student("studentTest");

		assertEquals("studentTest", student.getIDNumber());
	}

	/*
	 * this test ensures that Student users
	 * have the correct hourly rate
	 */
	@Test
	public void testStudentHourlyRate() {
		Student student = new Student("studentTest");

		assertEquals(20.00, student.getHRate(), 0.001);
	}
	
	
	/*
	 * this test ensures that the Partner constructor
	 * correctly stores the organization ID
	 */
	@Test
	public void testPartnerOrganizationId() {
		Partner partner = new Partner("partnerTest");

		assertEquals("partnerTest", partner.getIDNumber());
	}

	/*
	 * this test ensures that Partner users
	 * have the correct hourly rate
	 */
	@Test
	public void testPartnerHourlyRate() {
		Partner partner = new Partner("partnerTest");

		assertEquals(50.00, partner.getHRate(), 0.001);
	}

}
