package com.jgcvi.revolut.service;

import com.jgcvi.revolut.Main;
import com.jgcvi.revolut.dao.Customer;
import com.jgcvi.revolut.dao.InMemoryDB;
import com.jgcvi.revolut.dao.Transfer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.json.JSONObject;
import org.junit.*;

import static org.junit.Assert.*;

public class ServiceTest {
	private Customer johnLennon;
	private Customer ringoStarr;
	private Customer georgeHarrison;
	private Customer paulMcCartney;

	private HttpMethod get;
	private HttpMethod put1;
	private HttpMethod put2;
	private HttpMethod successfulTransfer;
	private HttpMethod failedTransfer;

	@Before
	public  void initialize() {
		johnLennon = MainService.createUser("John Lennon", 0);
		ringoStarr = MainService.createUser("Ringo Starr", 10);
		georgeHarrison = MainService.createUser("George Harrison", 20);
		paulMcCartney = MainService.createUser("Paul McCartney", 30);

		get = new GetMethod("http://localhost:7000/heartbeat");
		put1 = new PutMethod("http://localhost:7000/createuser/Yoko%20Ono/100");
		put2 = new PutMethod("http://localhost:7000/createuser/Brian%20Epstein/100");
		successfulTransfer = new PostMethod("http://localhost:7000/transfer/13/12/10");
		failedTransfer = new PostMethod("http://localhost:7000/transfer/13/12/100");
	}


	private HttpClient client = new HttpClient();

	private String constructJSON(byte[] response) {
		String msg = "";
		for(int i = 0; i <  response.length; i ++) {
			msg += (char) response[i];
		}

		return new JSONObject(msg).toString();
	}

	@Test
	public void testCreate() {
		assertEquals(johnLennon, InMemoryDB.get(0));
		assertEquals(ringoStarr, InMemoryDB.get(1));
		assertEquals(georgeHarrison, InMemoryDB.get(2));
		assertEquals(paulMcCartney, InMemoryDB.get(3));
	}

	@Test
	public void testTransfer() {
		Customer ringo = new Customer(1, "Ringo Starr", 5);
		Customer paul = new Customer(3,"Paul McCartney", 25);
		Customer george = new Customer(2, "George Harrison", 20);


		Transfer t1 = new Transfer(1, 0, 5.0);
		Transfer t2 = new Transfer(3, 0, 5.0);
		Transfer t3 = new Transfer(2, 3, 100);

		assertEquals(ringo.getBalance(), MainService.transfer(t1).getBalance(), 0.1);
		assertEquals(paul.getBalance(), MainService.transfer(t2).getBalance(), 0.1);

		assertEquals(10, InMemoryDB.get(0).getBalance(), 0.0001);
		assertEquals(5, InMemoryDB.get(1).getBalance(), 0.0001);
		assertEquals(25, InMemoryDB.get(3).getBalance(), 0.0001);

		assertEquals(george.getBalance(), MainService.transfer(t3).getBalance(), 0.1);

		assertEquals(20, InMemoryDB.get(2).getBalance(), 0.0001);
		assertEquals(25, InMemoryDB.get(3).getBalance(), 0.0001);
	}


	@Test
	public void testEndpoints() {
		Main.main(null);

		String heartbeat = new JSONObject().put("msg", "beat").toString();
		String yoko = new JSONObject().put("id", 12)
				.put("name", "Yoko Ono").put("balance", 100.0).toString();
		String brian = new JSONObject().put("id", 13)
				.put("name", "Brian Epstein").put("balance", 90.0).toString();
		try {
			assertEquals(200, client.executeMethod(get));
			assertEquals(heartbeat, constructJSON(get.getResponseBody()));

			assertEquals(200, client.executeMethod(put1));
			assertEquals(yoko, constructJSON(put1.getResponseBody()));
			client.executeMethod(put2);

			assertEquals(200, client.executeMethod(successfulTransfer));
			assertEquals(brian, constructJSON(successfulTransfer.getResponseBody()));

			assertEquals(200, client.executeMethod(failedTransfer));
			assertEquals(brian, constructJSON(failedTransfer.getResponseBody()));

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
