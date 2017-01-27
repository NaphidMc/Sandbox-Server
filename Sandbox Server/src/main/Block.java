package main;

import java.io.Serializable;

public class Block implements Serializable {
	
	private static final long serialVersionUID = -4698775568353685790L;
	
	public int texture;
	public String Name;
	public int[][] itemDropIDs; //Format: { <ItemID>, <ItemID> }, {<Chance to drop item 1>, <Chance to drop item 2>
	public boolean solid;
	public float health;
	
	public Block(String name, int texture, int[][] itemDropIDs, boolean solid, float health) {
		
		this.texture = texture;
		Name = name;
		
		if(itemDropIDs[0].length != itemDropIDs[1].length){
			System.out.println("Hey...we have a problem...not all block drop ids have a correspoding drop chance. \n There will be a crash in your near future :( ");    
		}
		
		this.itemDropIDs = new int[itemDropIDs.length][itemDropIDs[0].length];
		for(int i = 0; i < itemDropIDs[0].length; i++){
			this.itemDropIDs[0][i] = itemDropIDs[0][i];
			this.itemDropIDs[1][i] = itemDropIDs[1][i];
		}
		
		this.health = health;
		this.solid = solid;
	}
	
}
