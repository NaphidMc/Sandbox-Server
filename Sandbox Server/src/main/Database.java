package main;

public class Database {
	
	//Blocks
	public static Block BLOCK_GRASS;
	public static Block BLOCK_DIRT;
	public static Block BLOCK_AIR;
	public static Block BLOCK_STONE;
	public static Block BLOCK_IRONORE;
	public static Block BLOCK_BEDROCK;
	public static Block BLOCK_SAPLING;
	public static Block BLOCK_WOOD;
	public static Block BLOCK_LEAVES;
	public static Block TEST;
	
	public static void populate(){
		
		//Blocks
		BLOCK_GRASS = new Block("Grass", 0, new int[][] { {1, 3, 3, 3}, {100, 50, 50, 50} }, true, 20);
		BLOCK_DIRT = new Block("Dirt", 3, new int[][] { {1}, {100} }, true, 20);
		BLOCK_AIR = new Block("Air", 2, new int[][] {{-1}, {-1} }, true, 0);
		BLOCK_STONE = new Block("Stone", 11, new int[][] { {4}, {100} }, true, 50);
		BLOCK_IRONORE = new Block("Iron Ore", 6, new int[][] { {7, 7, 7, 7}, {100, 50, 50, 50} }, true, 80);
		BLOCK_BEDROCK = new Block("Bedrock", 1, new int[][] {{-1},{-1}}, true, 0);
		BLOCK_SAPLING = new Block("Sapling", 13, new int[][] {{8}, {100}}, false, 5);
		BLOCK_WOOD = new Block("Wood", 14, new int[][] { {6}, {100}}, false, 30);
		BLOCK_LEAVES = new Block("Leaves", 7, new int[][] { {8, 8}, {75, 50}}, false, 5);
		
	}
}
