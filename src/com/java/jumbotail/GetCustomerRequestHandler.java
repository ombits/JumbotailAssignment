package com.java.jumbotail;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GetCustomerRequestHandler {

	
	public static void request(Socket s, String request, String datacentre) throws IOException{
		
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		//GET /getCustomer?id=om.bits@gmail.com
		if(request == null || request.isEmpty()) //base case
			return;
		
		String[] url = request.split("\\s+");
		System.out.println(url);
		String[] queryParameter = url[1].split("\\?");
		String[] query = queryParameter[1].split("&");
		
		System.out.println(query[0]);
		String id ="";
		String response ="";
		try {
			
			if(query.length >0) {
			
				String[] tmp0 = query[0].split("=");
				if(tmp0[0].equalsIgnoreCase("id")){
					id = tmp0[1];
				}
				System.out.println("id"+ id);
			}
			 Customer customer = null;
			 
			 customer = Utility.getCustomer(id,datacentre);
			 
			 
		   if(customer == null){
			   response = "[{mssg:customer does not exist}]";
		   }else{
			   response = "[{name:"+ customer.getName()+" , emailid:" +customer.getEmailid()+","
			   		+ "phno:"+customer.getPhno()+"}]";
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
