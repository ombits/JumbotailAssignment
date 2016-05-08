package com.java.jumbotail;

import java.io.BufferedReader; 
import java.io.DataInputStream;
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.io.PrintWriter; 
import java.net.ServerSocket;
import java.net.Socket; 
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor; 
import java.util.concurrent.Executors;   

public class Server {
	private static final int fNumberOfThreads = 100; 
	private static final Executor fThreadPool = Executors.newFixedThreadPool(fNumberOfThreads);  
	
	public static void main(String[] args) throws IOException { 
		ServerSocket socket = new ServerSocket(444);
		
		// Sync data storage at every 2 min
		SyncDataStorage syncStorage = new SyncDataStorage();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(syncStorage, 0, 120*1000);
        
       
		while (true) {
			final Socket connection = socket.accept(); 
			Runnable task = new Runnable() {
				@Override
				public void run()
				{
					HandleRequest(connection);
				}
			}; 
			
		fThreadPool.execute(task); 
		} 
	}   
	
	private static void HandleRequest(Socket s)
	{ 
		BufferedReader in; 
		PrintWriter out;
		String request;  
		
		try 
		{ 
			String webServerAddress = s.getInetAddress().toString();
			System.out.println("New Connection:" + webServerAddress); 
			
			in = new BufferedReader(new InputStreamReader(s.getInputStream(),"UTF-8"));
			request = in.readLine();
			
			
			StringBuilder sb = new StringBuilder();
			int charsRead;
			
			if(request != null){
				if(request.startsWith("GET"))
				{ 
					if(request.contains("/in/")){
						if(request.contains("/createCustomer?")){
							CreateCustomerRequestHandler.request(s, request,"in");
						}
						if(request.contains("/getCustomer?")){
							GetCustomerRequestHandler.request(s, request,"in");
						}
						if(request.contains("/updateCustomer?")){
							UpdateCustomerRequestHandler.request(s, request, "in");
						}
					}else if(request.contains("/us/")){
						if(request.contains("/createCustomer?")){
							CreateCustomerRequestHandler.request(s, request,"us");
						}
						if(request.contains("/getCustomer?")){
							GetCustomerRequestHandler.request(s, request,"us");
						}
						if(request.contains("/updateCustomer?")){
							UpdateCustomerRequestHandler.request(s, request, "us");
						}
					}
						//SleepRequestHandler.request(s, request);
					
				}
				
			}
			else{
				out = new PrintWriter(s.getOutputStream(), true);
				String response ="BAD REQUEST";
				out.println("HTTP/1.0 400"); 
				out.println("Content-type: text/html"); 
				out.println("Server-name: myserver"); 
				out.println("Content-length: " + response.length()); 
				out.println(""); 
				out.println(response);
				out.flush(); 
				out.close();
			}
			
			s.close(); 
		} catch (IOException e) 
			{ 
				System.out.println("Failed respond to client request: " + e.getMessage()); 
			} 
			finally {
				if (s != null)
				{
					try { 
						s.close(); 
						} 
					catch (IOException e) 
					{ 
						e.printStackTrace(); 
					} 
				} 
			} 
		return; 
		}   
	} 
		
