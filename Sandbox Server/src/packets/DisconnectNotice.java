package packets;

import java.io.Serializable;

public class DisconnectNotice implements Serializable {
	
	private static final long serialVersionUID = -7418390173719941438L;
	
	public int id; //The id of the player that disconnected
	
	public DisconnectNotice(int id){
		this.id = id;
	}
}
