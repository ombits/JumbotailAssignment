package com.java.jumbotail;
import java.util.Iterator;
import java.util.Map;


public class Utility {

	
	public static void printDityMap(Map<String, Integer> dirtymap) {
	    Iterator it = dirtymap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	    }
	}
	
	public static boolean isUserCreated(String emailid,String datacentre){
		if(datacentre.equalsIgnoreCase("in")){
			if(InMemoryINDataStore.inDataStore_customer.containsKey(emailid))
				return true;
			
			if(!InMemoryINDataStore.inDataStore_customer.containsKey(emailid) && 
					InMemoryUSDataStore.dirtyBit.containsKey(emailid)){
				InMemoryINDataStore.inDataStore_customer.put(emailid, InMemoryUSDataStore.usDataStore_customer.get(emailid));
				return true;
			}
			
		}else if(datacentre.equalsIgnoreCase("us")){
			
			if(InMemoryUSDataStore.usDataStore_customer.containsKey(emailid))
				return true;
			
			if(!InMemoryUSDataStore.usDataStore_customer.containsKey(emailid) &&
					InMemoryINDataStore.dirtyBit.containsKey(emailid)){
				InMemoryUSDataStore.usDataStore_customer.put(emailid, InMemoryINDataStore.inDataStore_customer.get(emailid));
				return true;
			}		
		}
		
		return false;
	}
	
	public static Customer getCustomer(String id,String datacentre){
		Customer customer= null;
		if(datacentre.equalsIgnoreCase("in")){
			customer = InMemoryINDataStore.inDataStore_customer.get(id);
			if(customer == null && InMemoryUSDataStore.dirtyBit.containsKey(id)){
				customer = InMemoryUSDataStore.usDataStore_customer.get(id);
				InMemoryUSDataStore.usDataStore_customer.put(id, customer);
			}
		}
			 
		else if(datacentre.equalsIgnoreCase("us")){
			 customer = InMemoryUSDataStore.usDataStore_customer.get(id);
			 if(customer == null && InMemoryINDataStore.inDataStore_customer.containsKey(id)){
				 customer= InMemoryINDataStore.inDataStore_customer.get(id);
				 InMemoryINDataStore.inDataStore_customer.put(id, customer);
			 }
		 }
			
		return customer;
	}
	
	
	public static String findfield(String[] query,String id){
		for(int i=0; i<query.length;i++){
			String[] tmp = query[i].split("=");
			if(tmp[0].equalsIgnoreCase(id)){
				return tmp[1];
			}
		}
		
		return null;
	}
	
public static void updatefield(String id,String field, String value,Customer customer,Map<String, Integer> dirtymap){
		
		if(field.equalsIgnoreCase("emailid")){
			customer.setEmailid(value);
			
			
			if(dirtymap.containsKey(id)){
				int tmp = dirtymap.get(id) | 2 ;
				System.out.println("email id ::::::");
				InMemoryINDataStore.dirtyBit.put(id, tmp);
			}else{
				System.out.println("email id  ::::::");
				dirtymap.put(id, 2);
			}
			
		}else if(field.equalsIgnoreCase("phno")){
			customer.setPhno(value);
			if(dirtymap.containsKey(id)){
				
				int tmp = InMemoryINDataStore.dirtyBit.get(id) | 4 ;
				System.out.println("phno ::::::");
				dirtymap.put(id,tmp);
			}else{
				System.out.println("phno ::::::");
				dirtymap.put(id, 4);
			}
		}else if(field.equalsIgnoreCase("name")){
			customer.setName(value);
			if(dirtymap.containsKey(id)){
				int tmp = InMemoryINDataStore.dirtyBit.get(id) | 1 ;
				System.out.println("name ::::::");
				dirtymap.put(id, tmp);
			}else{
				System.out.println("name ::::::");
				dirtymap.put(id, 1);
			}
		}
		
	}    
}
