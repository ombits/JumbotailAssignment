package com.java.jumbotail;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class CreateCustomerRequestHandler {

	
	public static void request(Socket s, String request,String datacentre) throws IOException{
		
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		//GET /createCustomer?name=omsingh&emailid=om.bits@gmail.com&phno=919742048795 HTTP/1.1
		if(request == null || request.isEmpty()) //base case
			return;
		
		String[] url = request.split("\\s+");
		String[] queryParameter = url[1].split("\\?");
		String[] query = queryParameter[1].split("&");
		
		String response ="";
		Customer customer =new Customer();
		try {
			
			String emailid = Utility.findfield(query,"emailid");
			//check whethere this user exist in another cluster or not ?
			boolean checkflag = Utility.isUserCreated(emailid, datacentre);
			if(checkflag)
				 response = "[{status:user already exist}]";
			else{
			if(query.length >1) {
			
				String[] tmp0 = query[0].split("=");
				if(tmp0[0].equalsIgnoreCase("name")){
					customer.setName(tmp0[1]);
				}
			
				String[] tmp1 = query[1].split("=");
				if(tmp1[0].equalsIgnoreCase("emailid")){
					customer.setEmailid(tmp1[1]);
				}
			
				String[] tmp2 = query[2].split("=");
				if(tmp2[0].equalsIgnoreCase("phno")){
					customer.setPhno(tmp2[1]);
				}
			}
		
		
			//customer.setId(GenerateID.execute(customer.emailid));
			customer.setId(customer.getEmailid());
			long ts = System.currentTimeMillis();
			if(datacentre.equalsIgnoreCase("in")){
				InMemoryINDataStore.inDataStore_customer.put(customer.getId(), customer);
				InMemoryINDataStore.dirtyBit.put(emailid, 7);
				HashMap<Long, Customer> mm = new HashMap<Long, Customer>();
				mm.put(ts,customer);
				InMemoryINDataStore.allData.put(customer.getId(), mm);
			}
				
			else if(datacentre.equalsIgnoreCase("us")){
				InMemoryUSDataStore.usDataStore_customer.put(customer.getId(), customer);
				InMemoryUSDataStore.dirtyBit.put(emailid, 7);
				HashMap<Long, Customer> mm = new HashMap<Long, Customer>();
				mm.put(ts,customer);
				InMemoryUSDataStore.allData.put(customer.getId(), mm);
			}
				
			response = "[{id:"+customer.getId() +", status:ok}]";
			
			}
			} catch (Exception e) {
				e.printStackTrace();
				response = "[{status:failed}]";
			}	
		
		
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
