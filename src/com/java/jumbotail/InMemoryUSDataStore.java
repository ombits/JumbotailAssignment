package com.java.jumbotail;
import java.util.HashMap;
import java.util.Map;


public class InMemoryUSDataStore {

	public static Map<String,Customer> usDataStore_customer = new HashMap<String, Customer>();
	
	public static Map<String,Integer> dirtyBit = new HashMap<String,Integer>();
	
	public static Map<String, HashMap<Long, Customer>> allData = new HashMap<String, HashMap<Long,Customer>>();
	
	
	
}
