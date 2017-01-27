package main;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.listener.SocketListener;
import com.jmr.wrapper.server.Server;

import packets.DisconnectNotice;
import packets.MapChunkPacket;
import packets.MapPacket;
import packets.PlayerPacket;

public class ServerStarter implements SocketListener {
	
	//Server UI
	static JFrame frame;
	static JPanel content;
	static JTextPane log;
	
	int nextID;
	
	ArrayList<Connection> connections = new ArrayList<Connection>();
	ArrayList<Integer> ids = new ArrayList<Integer>();
	HashMap<Integer, PlayerPacket> recentPlayerPackets = new HashMap<Integer, PlayerPacket>(); //The most updated player packets for each player
	
	public static main.Map map;
	
	public ServerStarter(){
		
		Database.populate();
		map = new main.Map(32, 16);
		
		try{
			Server server = new Server(6756, 6756);
			server.setListener((SocketListener) this);
			
			if(server.isConnected()){
				System.out.println("Server started...");
			}
			
		} catch(Exception e){
			
		}
	}
	
	public static void main(String[] args){
		startUI();
		new ServerStarter();
	}
	
	public static void log(String text){
		log.setText(log.getText() + "\n" + text);
	}
	
	public static void startUI(){
		
		log = new JTextPane();
		log.setBounds(0, 0, 800, 600);
		log.setEditable(false);
		
		content = new JPanel();
		content.setLayout(null);
		content.add(log);
		
		frame = new JFrame();
		frame.setBounds(0, 0, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(content);
		frame.setVisible(true);
	}

	//Called when a client is connected
	@Override
	public void connected(Connection con) {
		log("Client connected");
	}
	
	//Called when a client disconnects
	@Override
	public void disconnected(Connection con) {
		log("Client disconnected...");
		
		//Notifies all other clients that one has disconnected
		for(int i = 0; i < connections.size(); i++){
			if(connections.get(i) != con){
				if(connections.get(i) != null && connections.get(i).getSocket() != null && !connections.get(i).getSocket().isClosed())
					connections.get(i).sendTcp(new DisconnectNotice(ids.get(connections.indexOf(con))));
			}
		}
		ids.remove(connections.indexOf(con));
		connections.remove(con);
	}
	
	//Called when a packet is recieved from the client
	@Override
	public void received(Connection con, Object obj) {
		
		if(obj instanceof PlayerPacket){
			
			PlayerPacket pp = (PlayerPacket)obj; //Hahaha... pee pee
			if(pp.id == -1){ //This happens when the player first connects
				
				Integer ID = getNextID();
				con.sendTcp(ID); //Sends the client an id
				
				MapPacket mp = new MapPacket(map);
				con.sendTcp(mp); //Sends the map's width and height
				//This loop goes through the map and sends it in chunks
				for(int i = 0; i < map.tiles.length;){
					int length = 8;
					
					con.sendTcp(new MapChunkPacket(map, i, length));
					i += length;
				}
				
				//Sends all the other players to the player
				for(int i = 0; i < ids.size(); i++){
					con.sendTcp(recentPlayerPackets.get(ids.get(i)));
				}
				
				connections.add(con);
				ids.add(ID);
				
				recentPlayerPackets.put(ID, pp);
				
			} else{
				
				//Sends the player to all other players
				for(int i = 0; i < connections.size(); i++){	
					if(connections.get(i) != null && connections.get(i).getSocket() != null && !connections.get(i).getSocket().isClosed())
						connections.get(i).sendTcp(pp);
				}
				
				recentPlayerPackets.put(pp.id, pp);
			}                         
		}
		
		//If the server receives map data, it is sent too all clients
		if(obj instanceof MapChunkPacket){
			
			MapChunkPacket mcp = (MapChunkPacket)obj;
			
			for(int i = 0; i < mcp.length; i++){
				map.tiles[mcp.startIndex + i] = mcp.tiles[i];
			}
			
			for(int i = 0; i < connections.size(); i++){
				if(connections.get(i) != null && connections.get(i).getSocket() != null && connections.get(i).getSocket().isClosed() == false)
					connections.get(i).sendTcp((MapChunkPacket) obj);
			}
		}
	}
	
	public int getNextID(){
		nextID++;
		return nextID;
	}
}
