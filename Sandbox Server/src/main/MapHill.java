package main;
import java.util.ArrayList;

public class MapHill {
	
	public int height;
	public int width;
	public int peakPositionX;
	public int groundLevel;
	public ArrayList<MapHillTile> hillTiles = new ArrayList<MapHillTile>();
	
	public MapHill(int height, int width, int peakPositionX, int groundLevel){
		this.height = height;
		this.width = width;
		this.groundLevel = groundLevel;
		this.peakPositionX = peakPositionX;
		generateHillTiles();
	}
	
	public void generateHillTiles(){
		int startX = peakPositionX - width;
		int endX = peakPositionX + width;
		int step = (height)/(peakPositionX - startX);
		
		int currentHeight = 0;
		boolean ascending = true;
		for(int i = 0; i <= endX - startX; i++){
			
			if(i == 0){
				hillTiles.add(new MapHillTile(startX, groundLevel - 1, true));
			}
			else if(i == ((endX - startX) - 1)){
				hillTiles.add(new MapHillTile(endX, groundLevel - 1, true));
			}
			else{
				for(int k = 0; k < currentHeight; k++) {
					if(k == currentHeight - 1){
						hillTiles.add(new MapHillTile(startX + i, groundLevel - k - 1, true));
					}
					else{
						hillTiles.add(new MapHillTile(startX + i, groundLevel - k - 1, false));
					}
					
					if(k == height - 1){
						ascending = false;
					}
				}
			}
			
			if(ascending){
				
				if(peakPositionX - i - startX != 0)
					step = (height - currentHeight)/((peakPositionX - i) - startX);
				else if(peakPositionX - i - startX == 0){
					step = 1;
				}
			} else {
				step = (currentHeight - groundLevel)/(endX - (startX - i) + 1);
				
				if(step >= 0) {
					step = -1;
				}
			}
			currentHeight += step;

		}
	}
}
