package packets;

import java.io.Serializable;

import main.Map;

public class MapPacket implements Serializable {
	
	private static final long serialVersionUID = 1496087825181574563L;
	
	public int mapWidth, mapHeight;
	
	public MapPacket(Map map){
		mapHeight = map.getHeight();
		mapWidth = map.getWidth();
	}
}
