package me.ryanhamshire.PopulationDensity;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;

public class ScanRegionTask extends Thread 
{
	private ChunkSnapshot[][] chunks;
	private boolean openNewRegions;
	
	private final int CHUNK_SIZE = 16;

	public ScanRegionTask(ChunkSnapshot chunks[][], boolean openNewRegions)
	{
		this.chunks = chunks;
		this.openNewRegions = openNewRegions;
	}
	
	private class Position
	{
		public int x;
		public int y;
		public int z;
		
		public Position(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public String toString()
		{
			return this.x + " " + this.y + " " + this.z;					
		}
	}
	
	@Override
	public void run() 
	{
		ArrayList<String> logEntries = new ArrayList<String>();
		
		//initialize report content
		int woodCount = 0;
		int coalCount = 0;
		int ironCount = 0;
		int goldCount = 0;
		int redstoneCount = 0;
		int diamondCount = 0;
		int playerBlocks = 0;

		//initialize a new array to track where we've been
		int maxHeight = PopulationDensity.ManagedWorld.getMaxHeight();
		int x, y, z;
		x = y = z = 0;
		boolean [][][] examined = new boolean [this.chunks.length * CHUNK_SIZE][maxHeight][this.chunks.length * CHUNK_SIZE];
		for(x = 0; x < examined.length; x++)
			for(y = 0; y < examined[0].length; y++)
				for(z = 0; z < examined[0][0].length; z++)
					examined[x][y][z] = false;
		
		//find a reasonable start position
		Position currentPosition = null;
		for(x = 0; x < examined.length && currentPosition == null; x++)
		{
			for(z = 0; z < examined[0][0].length && currentPosition == null; z++)
			{
				Position position = new Position(x, maxHeight - 1, z);
				if(this.getMaterialAt(position) == Material.AIR)
				{
					currentPosition = position;
				}
			}
		}
		
		//set depth boundary
		//if a player has to brave cavernous depths, those resources aren't "easily attainable"
		int min_y = PopulationDensity.instance.minimumRegionPostY - 20;
		
		//instantiate empty queue
		ConcurrentLinkedQueue<Position> unexaminedQueue = new ConcurrentLinkedQueue<Position>();
		
		//mark start position as examined
		try
		{
			examined[currentPosition.x][currentPosition.y][currentPosition.z] = true;				
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			logEntries.add("Unexpected Exception: " + e.toString());
		}
		
		//enqueue that start position
		unexaminedQueue.add(currentPosition);	
				
		//as long as there are positions in the queue, keep going
		while(!unexaminedQueue.isEmpty())
		{
			//dequeue a block
			currentPosition = unexaminedQueue.remove();
			
			//get material
			Material material = this.getMaterialAt(currentPosition);
			
			//material == null indicates the data is out of bounds (not in the snapshots)
			//in that case, just move on to the next item in the queue
			if(material == null || currentPosition.y < min_y) continue;
			
			//if it's not a pass-through block
			if(		material != Material.AIR && 
					material != Material.OAK_DOOR && 
					material != Material.SPRUCE_DOOR && 
					material != Material.BIRCH_DOOR && 
					material != Material.JUNGLE_DOOR && 
					material != Material.ACACIA_DOOR && 
					material != Material.DARK_OAK_DOOR && 
					material != Material.IRON_DOOR && 
					material != Material.OAK_TRAPDOOR && 
					material != Material.SPRUCE_TRAPDOOR && 
					material != Material.BIRCH_TRAPDOOR && 
					material != Material.JUNGLE_TRAPDOOR && 
					material != Material.ACACIA_TRAPDOOR && 
					material != Material.DARK_OAK_TRAPDOOR && 
					material != Material.LADDER
					)
			{
				//if it's a valuable resource, count it
				if      (material == Material.OAK_LOG) woodCount++;
				else if (material == Material.SPRUCE_LOG) woodCount++;
				else if (material == Material.BIRCH_LOG) woodCount++;
				else if (material == Material.JUNGLE_LOG) woodCount++;
				else if (material == Material.ACACIA_LOG) woodCount++;
				else if (material == Material.DARK_OAK_LOG) woodCount++;
				else if (material == Material.COAL_ORE) coalCount++;
				else if (material == Material.IRON_ORE) ironCount++;
				else if (material == Material.GOLD_ORE) goldCount++;
				else if (material == Material.REDSTONE_ORE) redstoneCount++;
				else if (material == Material.DIAMOND_ORE) diamondCount++;	
				
				//if it's a player block, count it
				else if (
						material != Material.WATER && 
						material != Material.LAVA &&
						material != Material.BROWN_MUSHROOM && 
						material != Material.CACTUS &&
						material != Material.DEAD_BUSH && 
						material != Material.DIRT &&
						material != Material.GRAVEL &&
						material != Material.GRASS &&
                                                material != Material.GRASS_BLOCK &&
						material != Material.RED_MUSHROOM_BLOCK &&
						material != Material.BROWN_MUSHROOM_BLOCK &&
						material != Material.ICE &&
						material != Material.LAPIS_ORE &&
						material != Material.OBSIDIAN &&
						material != Material.RED_MUSHROOM &&
						material != Material.DANDELION &&
						material != Material.POPPY &&
						material != Material.BLUE_ORCHID &&
						material != Material.ALLIUM &&
						material != Material.AZURE_BLUET &&
						material != Material.RED_TULIP &&
						material != Material.ORANGE_TULIP &&
						material != Material.WHITE_TULIP &&
						material != Material.PINK_TULIP &&
						material != Material.OXEYE_DAISY &&
						material != Material.SUNFLOWER &&
						material != Material.LILAC &&
						material != Material.ROSE_BUSH &&
						material != Material.PEONY &&
						material != Material.TALL_GRASS &&
						material != Material.LARGE_FERN &&
						material != Material.OAK_LEAVES &&
                                                material != Material.SPRUCE_LEAVES &&
                                                material != Material.BIRCH_LEAVES &&
                                                material != Material.JUNGLE_LEAVES &&
                                                material != Material.ACACIA_LEAVES &&
                                                material != Material.DARK_OAK_LEAVES &&
                                                material != Material.OAK_LOG &&
						material != Material.SPRUCE_LOG &&
						material != Material.BIRCH_LOG &&
						material != Material.JUNGLE_LOG &&
						material != Material.ACACIA_LOG &&
						material != Material.DARK_OAK_LOG &&
						material != Material.SAND &&
						material != Material.SANDSTONE &&
						material != Material.SNOW &&
						material != Material.STONE &&
						material != Material.VINE &&
						material != Material.LILY_PAD &&
						material != Material.MOSSY_COBBLESTONE && 
						material != Material.CLAY &&
						material != Material.WHITE_TERRACOTTA &&
						material != Material.ORANGE_TERRACOTTA &&
						material != Material.MAGENTA_TERRACOTTA &&
						material != Material.LIGHT_BLUE_TERRACOTTA &&
						material != Material.YELLOW_TERRACOTTA &&
						material != Material.LIME_TERRACOTTA &&
						material != Material.PINK_TERRACOTTA &&
						material != Material.GRAY_TERRACOTTA &&
						material != Material.CYAN_TERRACOTTA &&
						material != Material.PURPLE_TERRACOTTA &&
						material != Material.BLUE_TERRACOTTA &&
						material != Material.BROWN_TERRACOTTA &&
						material != Material.GREEN_TERRACOTTA &&
						material != Material.RED_TERRACOTTA &&
						material != Material.BLACK_TERRACOTTA &&
						material != Material.TERRACOTTA && // ??
						material != Material.SUGAR_CANE &&
						material != Material.PACKED_ICE &&
                                                material != Material.SEAGRASS &&
                                                material != Material.TALL_SEAGRASS &&
                                                material != Material.SEA_PICKLE &&
                                                material != Material.TUBE_CORAL &&
                                                material != Material.BRAIN_CORAL &&
                                                material != Material.BUBBLE_CORAL &&
                                                material != Material.FIRE_CORAL &&
                                                material != Material.HORN_CORAL &&
                                                material != Material.TUBE_CORAL_BLOCK &&
                                                material != Material.BRAIN_CORAL_BLOCK &&
                                                material != Material.BUBBLE_CORAL_BLOCK &&
                                                material != Material.FIRE_CORAL_BLOCK &&
                                                material != Material.HORN_CORAL_BLOCK &&
                                                material != Material.TUBE_CORAL_FAN &&
                                                material != Material.BRAIN_CORAL_FAN &&
                                                material != Material.BUBBLE_CORAL_FAN &&
                                                material != Material.FIRE_CORAL_FAN &&
                                                material != Material.HORN_CORAL_FAN)
				{
					playerBlocks++;
				}
			}
			
			//otherwise for pass-through blocks, enqueue the blocks around them for examination
			else
			{
				//make a list of adjacent blocks
				ConcurrentLinkedQueue<Position> adjacentPositionQueue = new ConcurrentLinkedQueue<Position>();
									
				//x + 1
				adjacentPositionQueue.add(new Position(currentPosition.x + 1, currentPosition.y, currentPosition.z));
				
				//x - 1
				adjacentPositionQueue.add(new Position(currentPosition.x - 1, currentPosition.y, currentPosition.z));
				
				//z + 1
				adjacentPositionQueue.add(new Position(currentPosition.x, currentPosition.y, currentPosition.z + 1));
				
				//z - 1
				adjacentPositionQueue.add(new Position(currentPosition.x, currentPosition.y, currentPosition.z - 1));
				
				//y + 1
				adjacentPositionQueue.add(new Position(currentPosition.x, currentPosition.y + 1, currentPosition.z));
				
				//y - 1
				adjacentPositionQueue.add(new Position(currentPosition.x, currentPosition.y - 1, currentPosition.z));
										
				//for each adjacent block
				while(!adjacentPositionQueue.isEmpty())
				{
					Position adjacentPosition = adjacentPositionQueue.remove();
					
					try
					{
						//if it hasn't been examined yet
						if(!examined[adjacentPosition.x][adjacentPosition.y][adjacentPosition.z])
						{					
							//mark it as examined
							examined[adjacentPosition.x][adjacentPosition.y][adjacentPosition.z] = true;
							
							//shove it in the queue for processing
							unexaminedQueue.add(adjacentPosition);
						}
					}
					
					//ignore any adjacent blocks which are outside the snapshots
					catch(ArrayIndexOutOfBoundsException e){ }
				}					
			}
		}			
		
		//compute a resource score
		int resourceScore = coalCount * 2 + ironCount * 3 + goldCount * 3 + redstoneCount * 3 + diamondCount * 4;
		
		//due to a race condition, bukkit might say a chunk is loaded when it really isn't.
		//in that case, bukkit will incorrectly report that all of the blocks in the chunk are air
		//strategy: if resource score and wood count are flat zero, the result is suspicious, so wait 5 seconds for chunks to load and start over
		//to avoid an infinite loop in a resource-bare region, maximum ONE repetition
		
		//deliver report
		logEntries.add("");								
		logEntries.add("Region Scan Results :");
		logEntries.add("");				
		logEntries.add("         Wood :" + woodCount + "  (Minimum: " + PopulationDensity.instance.woodMinimum + ")");
		logEntries.add("         Coal :" + coalCount);
		logEntries.add("         Iron :" + ironCount);
		logEntries.add("         Gold :" + goldCount);
		logEntries.add("     Redstone :" + redstoneCount);
		logEntries.add("      Diamond :" + diamondCount);
		logEntries.add("Player Blocks :" + playerBlocks + "  (Maximum: " + (PopulationDensity.instance.densityRatio * 40000) + ")");
		logEntries.add("");
		logEntries.add(" Resource Score : " + resourceScore + "  (Minimum: " + PopulationDensity.instance.resourceMinimum + ")");
		logEntries.add("");								
		
		//if NOT sufficient resources for a good start
		if(resourceScore < PopulationDensity.instance.resourceMinimum || woodCount < PopulationDensity.instance.woodMinimum || playerBlocks > 40000 * PopulationDensity.instance.densityRatio)
		{					
			if(resourceScore < PopulationDensity.instance.resourceMinimum || woodCount < PopulationDensity.instance.woodMinimum)
			{
				logEntries.add("Summary: Insufficient near-surface resources to support new players.");			
			}
			else if(playerBlocks > 40000 * PopulationDensity.instance.densityRatio)
			{
				logEntries.add("Summary: Region seems overcrowded.");			
			}
		}
		
		//otherwise
		else
		{
			logEntries.add("Summary: Looks good!  This region is suitable for new players.");
			openNewRegions = false;
		}
		
		//now that we're done, notify the main thread
		ScanResultsTask resultsTask = new ScanResultsTask(logEntries, openNewRegions);
		PopulationDensity.instance.getServer().getScheduler().scheduleSyncDelayedTask(PopulationDensity.instance, resultsTask, 5L);
	}
	
	@SuppressWarnings("deprecation")
        private Material getMaterialAt(Position position)
	{
		Material material = null;
		
		int chunkx = position.x / 16;
		int chunkz = position.z / 16;
		
		try
		{
			ChunkSnapshot snapshot = this.chunks[chunkx][chunkz];
                        material = snapshot.getBlockType(position.x % 16, position.y, position.z % 16);
		}
		catch(IndexOutOfBoundsException e) { }
		
		return material;
	}	
}
