package com.example;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReceiver {

	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(String message) {
		
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			SimpleObject a = mapper.readValue(message, SimpleObject.class);
			System.out.println("Received JSON <" + message + ">");
			System.out.println("EMAIL: " + a.getEmail());
			System.out.println("NAME: " + a.getName());
			System.out.println("AGE: " + a.getAge());
			System.out.println("DATE: " + a.getDate().toString());
			System.out.println("PRICE: " + a.getPrice());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

}
