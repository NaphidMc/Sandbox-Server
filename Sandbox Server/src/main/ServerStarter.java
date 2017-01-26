package main;

import java.util.ArrayList;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.listener.SocketListener;
import com.jmr.wrapper.server.Server;

import packets.PlayerPacket;

public class ServerStarter implements SocketListener {
	
	int nextID;
	
	ArrayList<Connection> connections = new ArrayList<Connection>();
	
	public ServerStarter(){
		try{
			Server server = new Server(2667, 2667);
			server.setListener((SocketListener) this);
			
			if(server.isConnected()){
				System.out.println("Server started...");
			}
			
		} catch(Exception e){
			
		}
	}
	
	public static void main(String[] args){
		new ServerStarter();
	}

	//Called when a client is connected
	@Override
	public void connected(Connection con) {
		System.out.println("Client connected...");
		connections.add(con);
	}
	
	//Called when a client disconnects
	@Override
	public void disconnected(Connection con) {
		System.out.println("Client disconnected...");
	}
	
	//Called when a packet is recieved from the client
	@Override
	public void received(Connection con, Object obj) {
		
		if(obj instanceof PlayerPacket){
			
			PlayerPacket pp = (PlayerPacket)obj; //Hahaha... pee pee
			if(pp.id == -1){
				con.sendTcp(new Integer(getNextID()));
			}
			System.out.println("Recieved player with ID: " + pp.id);                           
		}
	}
	
	public int getNextID(){
		nextID++;
		return nextID;
	}
}
