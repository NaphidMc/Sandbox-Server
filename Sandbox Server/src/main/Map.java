package main;

import java.util.concurrent.ThreadLocalRandom;

public class Map {
	
	public Tile[] tiles;
	public int mapEndCoordinate;
	public int mapBottonCoordinate;
	private int mapWidth, mapHeight;
	
	public Map() { }
	
	public Map(int mapWidth, int mapHeight){
		
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;

		tiles = new Tile[mapWidth * mapHeight];
		
		generateTiles(7, 13, 2, 5, 2, 4, 12, 1, 13, .5f); //This confusing mess does most of the generation
		fixGrassBlocks(); //This function makes it so grass blocks can't have blocks on top of them
		generateTrees(2f); //Generates the trees; the parameter is tree density
		growTrees(); //Adds leaves and stems to the trees
	}
	
	/**
	 * Returns the width in tiles of the map
	 */
	public int getWidth(){
		return mapWidth;
	}
	
	/**
	 * Returns the height in tiles of the map
	 */
	public int getHeight(){
		return mapHeight;
	}
	
	/**
	 * 
	 * @param groundLevel - How many blocks of air there are above the base ground height
	 * @param hills - How many hills there are
	 * @param minHillHeight - How low hills can be? I think...
	 * @param maxHillHeight - How high the hills can become
	 * @param minHillWidth - The minimum width of hills
	 * @param maxHillWidth - The widest hills can get
	 * @param stoneDepth - How deep you have to go to see stone
	 * @param stoneTransition - How deep until the stone/dirt layer appears
	 * @param ironDepth - How deep you have to go to see iron
	 * @param ironFrequencyMultiplier - The frequency of iron deposits
	 */
	public void generateTiles(int groundLevel, int hills, int minHillHeight, int maxHillHeight, int minHillWidth, int maxHillWidth, int stoneDepth, int stoneTransition, int ironDepth, float ironFrequencyMultiplier){
		int height = 0;
		int width = 0;
		
		MapHill[] mapHills = new MapHill[hills];
		
		for(int i = 0; i < mapHills.length; i++){
			int currentWidth = ThreadLocalRandom.current().nextInt(minHillWidth, maxHillWidth + 1);
			int currentHeight = ThreadLocalRandom.current().nextInt(minHillHeight, maxHillHeight + 1);
			int peakPosition = ThreadLocalRandom.current().nextInt(0, mapWidth - currentWidth + 3);
			
			mapHills[i] = new MapHill(currentWidth, currentHeight, peakPosition + 1, groundLevel);
		}
		
		
		A: for(int i = 0; i < mapWidth*mapHeight; i++){
			
			int currentX = Tile.tileSize * width;
			int currentY = Tile.tileSize * height;
			
			//Checks if the current tile is part of a hill
			for(int k = 0; k < mapHills.length; k++){
				
				for(int j = 0; j < mapHills[k].hillTiles.size(); j++){
					
					if(mapHills[k].hillTiles.get(j).x == width && mapHills[k].hillTiles.get(j).y == height){

						if(mapHills[k].hillTiles.get(j).topTile){
							tiles[i] = new Tile(currentX, currentY, Database.BLOCK_GRASS);
						}else{
							tiles[i] = new Tile(currentX, currentY, Database.BLOCK_DIRT);
						}
						
						width++;
						if((i + 1)%mapWidth == 0){
							height++;
							width = 0;
						}
						
						continue A;
					}
				}
			}
			
			
			if(height < groundLevel){
				tiles[i] = new Tile(currentX, currentY, Database.BLOCK_AIR);
			} else if(height == groundLevel){
				//Top grass layer
				tiles[i] = new Tile(currentX, currentY, Database.BLOCK_GRASS);
			} else{
				if(height != mapHeight - 1){
					//Most generation stuff goes here
					int random = ThreadLocalRandom.current().nextInt(1, 101);
					
					if(!(height <= stoneDepth) && !(height >= stoneDepth - stoneTransition)) {
						//Dirt layer
						
						tiles[i] = new Tile(currentX, currentY, Database.BLOCK_DIRT);
					} else if(height <= stoneDepth && height >= stoneDepth - stoneTransition) {    
						//Stone transitional layer
						
						if(random <= 55){
							tiles[i] = new Tile(currentX, currentY, Database.BLOCK_STONE);
						}
						else{
							tiles[i] = new Tile(currentX, currentY, Database.BLOCK_DIRT);
						}
					} else if(height > stoneDepth){
						//Stone layer
						
						random = ThreadLocalRandom.current().nextInt(0, 101);
						
						float ironChance = 0.0f;
						if(height > ironDepth){
							ironChance = (height - ironDepth)*ironFrequencyMultiplier*10;
							
							if(ironChance > 10){
								ironChance = 10;
							}
						}
						
						if(random < ironChance){
							tiles[i] = new Tile(currentX, currentY, Database.BLOCK_IRONORE);
						}else{
						tiles[i] = new Tile(currentX, currentY, Database.BLOCK_STONE);
						}
					}
				}
				else{
					tiles[i] = new Tile(currentX, currentY, Database.BLOCK_BEDROCK);
				}
			}
			
			width++;
			if((i + 1)%mapWidth == 0){
				height++;
				width = 0;
			}
			
			if(tiles[i] == null)
			{
				tiles[i] = new Tile(currentX, currentY, Database.BLOCK_DIRT);
			}
		}
	}
	
	/**
	 * Checks if any grass blocks have a block above them,
	 * and if they do, the grass block is changed to dirt
	 */
	public void fixGrassBlocks() {
		for(int i = 0; i < tiles.length; i++){
			if(tiles[i].block == Database.BLOCK_GRASS) {
				Block block = getTileAtCoordinates(tiles[i].x, tiles[i].y - Tile.tileSize).block;
				if(block != null){
					if(block != Database.BLOCK_AIR && block.solid == true) {     
						tiles[i].setBlock(Database.BLOCK_DIRT);
					}
				}
			}
		}
	}
	
	/**
	 * Places saplings all over the map
	 * @param treeDensity - The frequency of trees
	 */
	public void generateTrees(float treeDensity){
		
		float treeChance = (5*treeDensity);
		for(int i = 0; i < tiles.length; i++){
			if(tiles[i].block == Database.BLOCK_GRASS){
				int random = ThreadLocalRandom.current().nextInt(1, 101);
				
				if(random < treeChance){
					//Make a tree
					getTileAtCoordinates(tiles[i].x, tiles[i].y - Tile.tileSize).setBlock(Database.BLOCK_SAPLING);
				} else{
					//treeChance += (5*treeDensity);
				}
			}
		}
	}
	
	/**
	 * Replaces all saplings with fully grown trees
	 */
	public void growTrees(){
		int treeGrowChance = 101;
		TileLoop: for(int i = 0; i < tiles.length; i++){
			if(tiles[i].block == Database.BLOCK_SAPLING){
				int rand = ThreadLocalRandom.current().nextInt(1, 101);
				if(rand <= treeGrowChance){
					int stemHeight = ThreadLocalRandom.current().nextInt(1, 5);
					
					//Grow tree
					tiles[i].setBlock(Database.BLOCK_WOOD);
					
					int maxStemHeight = 0;
					for(int k = 1; k < stemHeight; k++){
						Tile tile = getTileAtCoordinates(tiles[i].x, tiles[i].y - Tile.tileSize*k);
						if(tile != null){
							if(tile.block == Database.BLOCK_AIR){
								tile.setBlock(Database.BLOCK_WOOD);
							} else {
								continue TileLoop;
							}
						}
						
						maxStemHeight = k;
					}
					
					//Leaves
					Tile temp;
					
					temp = getTileAtCoordinates(tiles[i].x - Tile.tileSize, tiles[i].y - maxStemHeight * Tile.tileSize);
					if(temp != null){
						if(temp.block == Database.BLOCK_AIR){
							temp.setBlock(Database.BLOCK_LEAVES);
						}
					}
					
					temp = getTileAtCoordinates(tiles[i].x, tiles[i].y - maxStemHeight * Tile.tileSize);
					if(temp != null){
						if(temp.block == Database.BLOCK_AIR){
							temp.setBlock(Database.BLOCK_LEAVES);
						}
					}
					
					temp = getTileAtCoordinates(tiles[i].x + Tile.tileSize, tiles[i].y - maxStemHeight * Tile.tileSize);
					if(temp != null){
						if(temp.block == Database.BLOCK_AIR){
							temp.setBlock(Database.BLOCK_LEAVES);
						}
					}
					
					temp = getTileAtCoordinates(tiles[i].x, tiles[i].y - maxStemHeight * Tile.tileSize - Tile.tileSize);
					if(temp != null){
						if(temp.block == Database.BLOCK_AIR){
							temp.setBlock(Database.BLOCK_LEAVES);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return The tile at the coordinates, returns null if it does not exist
	 */
	public Tile getTileAtCoordinates(int x, int y){
		for(int i = 0; i < tiles.length; i++){
			java.awt.Rectangle tileRect = new java.awt.Rectangle(tiles[i].x, tiles[i].y, Tile.tileSize, Tile.tileSize);
			
			if(tileRect.contains(x, y)){
				return tiles[i];
			}
		}
		
		return null;
	}

	
}
