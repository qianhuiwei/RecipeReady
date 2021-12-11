package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import db.MySQLConnection;

public class TestUsersTable {
	@Test
	public void test() {
		MySQLConnection connection = new MySQLConnection();
		connection.testMySQLConnection();
		
		String user1_id = "user1";
		String user1_password = "abcd";
		String user1_first_name = "Micky";
		String user1_last_name = "Mouse";
		
		// test addUser method
		connection.addUser(user1_id, user1_password, user1_first_name, user1_last_name);
		
		List<String> user1_info = connection.getUserInfo(user1_id);
		assertEquals(user1_id, user1_info.get(0));
		assertEquals(user1_password, user1_info.get(1));
		assertEquals(user1_first_name, user1_info.get(2));
		assertEquals(user1_last_name, user1_info.get(3));
		
		System.out.println("addUser() db operation checked");
		
		// test verifyLogin method
		boolean hasUser = connection.verifyLogin(user1_id, user1_password);
		assertTrue(hasUser);
		System.out.println("verifyLogin() db operation checked");
		
		// test getFullname method
		String user1_fullname = connection.getFullname(user1_id);
		assertEquals("Micky Mouse", user1_fullname);
		System.out.println("getFullname() db operation checked");
		
		connection.close();
	}
}