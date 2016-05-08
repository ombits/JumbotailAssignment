package com.java.jumbotail;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryUSDataStore {

	public static Map<String,Customer> usDataStore_customer = new ConcurrentHashMap<String, Customer>();
	
	public static Map<String,Integer> dirtyBit = new ConcurrentHashMap<String,Integer>();
	
	public static Map<String, HashMap<Long, Customer>> allData = new ConcurrentHashMap<String, HashMap<Long,Customer>>();
	
	
	
}
