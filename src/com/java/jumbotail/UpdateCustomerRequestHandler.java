package com.java.jumbotail;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpdateCustomerRequestHandler {

	
	public static void request(Socket s, String request,String datacentre) throws IOException{
		
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		//GET /updateCustomer?id=omsingh.bits@gmail.com&name=omsingh&emailid=om.bits@gmail.com&phno=919742048795 HTTP/1.1
		if(request == null || request.isEmpty()) //base case
			return;
		
		String[] url = request.split("\\s+");
		String[] queryParameter = url[1].split("\\?");
		String[] query = queryParameter[1].split("&");
		
		String response ="";
		Customer customer = null ;
		Map<String, Integer> dirtyMap = null;
	    Map<String,HashMap<Long, Customer>> md = null;
		try {
			
			String id = Utility.findfield(query,"id");
			System.out.println("id ="+id);
			if(datacentre.equalsIgnoreCase("in")){
				customer = InMemoryINDataStore.inDataStore_customer.get(id);
			    dirtyMap = InMemoryINDataStore.dirtyBit;
			    md = InMemoryINDataStore.allData;
			}  
			else if(datacentre.equalsIgnoreCase("us")){
				customer = InMemoryUSDataStore.usDataStore_customer.get(id);
				dirtyMap = InMemoryUSDataStore.dirtyBit;
				md = InMemoryUSDataStore.allData;
			}		
			if(customer == null){
				response = "[{mssg: id does not exist}]";
			}
			else{
				Long ts = System.currentTimeMillis();
				Map<Long,Customer> mm = new HashMap<Long, Customer>();
				mm.put(ts,customer);
				md.put(id, (HashMap<Long, Customer>) mm);
				for(int i=0 ; i < query.length ; i++){
				
			    	String[] tmp1 = query[i].split("=");
			    	if(!tmp1[0].equals("emailid") && !tmp1[0].equalsIgnoreCase("id"))
			    		Utility.updatefield(id,tmp1[0], tmp1[1], customer,dirtyMap);
					
			    }
				response = "[{mssg:updated successfully}]";
			}
		
			
			} catch (Exception e) {
				e.printStackTrace();
				response = "[{status: failed}]";
				
			}	
		
		Utility.printDityMap(dirtyMap); // print dirty map
		
		
		out.println("HTTP/1.0 200"); 
		out.println("Content-type: text/html"); 
		out.println("Server-name: myserver"); 
		out.println("Content-length: " + response.length()); 
		out.println(""); 
		out.println(response);
		out.flush(); 
		out.close();
		
		return;
		
	}
	
	
	
	
	
	
}
