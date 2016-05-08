package com.java.jumbotail;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SyncDataStorage extends TimerTask{

	public void run() {
        System.out.println("Data Storage task started at:"+new Date());
        dataStorageTask();
        System.out.println("Data Storage task finished at:"+new Date());
    }
 /**
  * Algorithm:
  * Iterate over the each dirty map.
  * Sync data.
  * 
  */
    private void dataStorageTask() {
        try {
           
        	IterateINDatastore();
            IterateUSDatastore();
         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void IterateINDatastore(){
    	Iterator it = InMemoryINDataStore.dirtyBit.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry dirty = (Map.Entry)it.next();
            Integer changedbits = (Integer) dirty.getValue();
            Customer c_in = InMemoryINDataStore.inDataStore_customer.get(dirty.getKey());
            if(InMemoryUSDataStore.usDataStore_customer.containsKey(dirty.getKey())){
            	Customer c_us = InMemoryUSDataStore.usDataStore_customer.get(dirty.getKey());
            	
            	if((changedbits & 1) ==1){
            		c_us.setName(c_in.getName());
            	}
            	
            	if((changedbits & 4) == 4){
            		c_us.setPhno(c_in.getPhno());
            	}
            }else{
            	InMemoryUSDataStore.usDataStore_customer.put((String) dirty.getKey(), c_in);
            }
            it.remove();
        }
        
        System.out.println(InMemoryINDataStore.dirtyBit.size());
    }
    
    public void IterateUSDatastore(){
    	Iterator it = InMemoryUSDataStore.dirtyBit.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry dirty = (Map.Entry)it.next();
            Integer changedbits = (Integer) dirty.getValue();
            Customer c_us = InMemoryUSDataStore.usDataStore_customer.get(dirty.getKey());
            if(InMemoryINDataStore.inDataStore_customer.containsKey(dirty.getKey())){
            	Customer c_in = InMemoryINDataStore.inDataStore_customer.get(dirty.getKey());
            	
            	if((changedbits & 1) ==1){
            		c_in.setName(c_us.getName());
            	}
            	
            	if((changedbits & 4) == 4){
            		c_in.setPhno(c_us.getPhno());
            	}
            }else{
            	InMemoryINDataStore.inDataStore_customer.put((String) dirty.getKey(), c_us);
            }
            it.remove();
        }
       
        
        System.out.println(InMemoryUSDataStore.dirtyBit.size());
    }
}
