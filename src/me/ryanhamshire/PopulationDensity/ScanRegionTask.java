/*
    PopulationDensity Server Plugin for Minecraft
    Copyright (C) 2011 Ryan Hamshire

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.ryanhamshire.PopulationDensity;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ScanRegionTask extends AbstractScanRegionTask
{
    private final ChunkSnapshot[][] chunks;
    private boolean openNewRegions;

    private static final int CHUNK_SIZE = 16;

    private static final String WOOD = "WOOD";
    private static final String COAL_ORE = "COAL_ORE";
    private static final String LAPIS_ORE = "LAPIS_ORE";
    private static final String IRON_ORE = "IRON_ORE";
    private static final String GOLD_ORE = "GOLD_ORE";
    private static final String REDSTONE_ORE = "REDSTONE_ORE";
    private static final String EMERALD_ORE = "EMERALD_ORE";
    private static final String DIAMOND_ORE = "DIAMOND_ORE";
    private static final String PLAYER_BLOCK = "PLAYER_BLOCK";

    public ScanRegionTask(ChunkSnapshot[][] chunks, boolean openNewRegions)
    {
        this.chunks = chunks;
        this.openNewRegions = openNewRegions;
    }

    @Override
    public void execute() {
        this.setPriority(Thread.MIN_PRIORITY);
        this.start();
    }

    @Override
    public void run()
    {
        ConcurrentHashMap<String, Integer> blockCounts = new ConcurrentHashMap<>();

        //set depth and height boundaries
        //if a player has to brave cavernous depths, those resources aren't "easily attainable"
        int maxHeight = PopulationDensity.ManagedWorld.getMaxHeight();
        int minY = PopulationDensity.instance.minimumRegionPostY - 20;
        for (ChunkSnapshot[] chunk : chunks) {
            for (int z = 0; z < chunks[0].length; z++) {
                if (chunk[z] != null) {
                    countBlocksInSnapshotAndFillMap(chunk[z], blockCounts, minY, maxHeight);
                }
            }
        }

        // initialize report content
        int woodCount = blockCounts.getOrDefault(WOOD, 0);
        int coalCount = blockCounts.getOrDefault(COAL_ORE, 0);
        int lapisCount = blockCounts.getOrDefault(LAPIS_ORE, 0);
        int ironCount = blockCounts.getOrDefault(IRON_ORE, 0);
        int goldCount = blockCounts.getOrDefault(GOLD_ORE, 0);
        int redstoneCount = blockCounts.getOrDefault(REDSTONE_ORE, 0);
        int emeraldCount = blockCounts.getOrDefault(EMERALD_ORE, 0);
        int diamondCount = blockCounts.getOrDefault(DIAMOND_ORE, 0);
        int playerBlocks = blockCounts.getOrDefault(PLAYER_BLOCK, 0);

        // compute a resource score
        int resourceScore = Math.min(coalCount, 200) * 2 + ironCount * 3 + goldCount * 3
                + Math.min(redstoneCount, 50) + emeraldCount * 3 + diamondCount * 4;

        //deliver report
        ArrayList<String> logEntries = new ArrayList<>();
        logEntries.add(" ");
        logEntries.add("Region Scan Results:");
        logEntries.add(" ");
        logEntries.add("Wood: " + woodCount + "  (Minimum: " + PopulationDensity.instance.woodMinimum + ")");
        logEntries.add("Coal: " + coalCount);
        logEntries.add("Iron: " + ironCount);
        logEntries.add("Gold: " + goldCount);
        logEntries.add("Lapis: " + lapisCount);
        logEntries.add("Redstone: " + redstoneCount);
        logEntries.add("Emerald: " + emeraldCount);
        logEntries.add("Diamond: " + diamondCount);
        logEntries.add("Player Blocks: " + playerBlocks + "  (Maximum: " + (PopulationDensity.instance.densityRatio * 40000) + ")");
        logEntries.add(" ");
        logEntries.add("Resource Score: " + resourceScore + "  (Minimum: " + PopulationDensity.instance.resourceMinimum + ")");
        logEntries.add(" ");

        //if NOT sufficient resources for a good start
        if (resourceScore < PopulationDensity.instance.resourceMinimum || woodCount < PopulationDensity.instance.woodMinimum || playerBlocks > 40000 * PopulationDensity.instance.densityRatio)
        {
            if (resourceScore < PopulationDensity.instance.resourceMinimum || woodCount < PopulationDensity.instance.woodMinimum)
            {
                logEntries.add("Summary: Insufficient near-surface resources to support new players.");
            } else if (playerBlocks > 40000 * PopulationDensity.instance.densityRatio)
            {
                logEntries.add("Summary: Region seems overcrowded.");
            }
        }

        //otherwise
        else
        {
            logEntries.add("Summary: Looks good! This region is suitable for new players.");
            openNewRegions = false;
        }

        //now that we're done, notify the main thread
        ScanResultsTask resultsTask = new ScanResultsTask(logEntries, openNewRegions);
        PopulationDensity.instance.getServer().getScheduler().scheduleSyncDelayedTask(PopulationDensity.instance, resultsTask, 5L);
    }

    private void countBlocksInSnapshotAndFillMap(ChunkSnapshot chunkSnapshot, ConcurrentHashMap<String, Integer> blockCounts,
                                                 int minY, int maxHeight)
    {
        int woodCount =0;
        int coalCount = 0;
        int lapisCount = 0;
        int ironCount = 0;
        int goldCount = 0;
        int redstoneCount = 0;
        int emeraldCount = 0;
        int diamondCount = 0;
        int playerBlocks = 0;

        for (int x = 0; x < CHUNK_SIZE; x++)
        {
            for (int y = minY; y < maxHeight; y++)
            {
                for (int z = 0; z < CHUNK_SIZE; z++)
                {
                    String blockType = chunkSnapshot.getBlockType(x, y, z).name();

                    // most of the blocks are air or water, so we can skip them
                    if (blockType.equals("AIR") || blockType.equals("WATER") || blockType.equals("CAVE_AIR")) continue;

                    Material material = Material.valueOf(blockType);

                    if (!Not_Placed_By_Player_Material.contains(material))
                    {
                        // count only blocks which have an air/cave-air block near them
                        // this means they have high chance of being visible and accessible to the players
                        if (!isUncoveredBlock(chunkSnapshot, x, y, z, minY, maxHeight)) continue;

                        switch (material)
                        {
                            case OAK_LOG:
                            case SPRUCE_LOG:
                            case BIRCH_LOG:
                            case JUNGLE_LOG:
                            case ACACIA_LOG:
                            case DARK_OAK_LOG:
                            case CHERRY_LOG:
                            case MANGROVE_LOG:
                            {
                                woodCount++;
                                break;
                            }
                            case COAL_ORE:
                            case DEEPSLATE_COAL_ORE:
                            {
                                coalCount++;
                                break;
                            }
                            case IRON_ORE:
                            case DEEPSLATE_IRON_ORE:
                            {
                                ironCount++;
                                break;
                            }
                            case GOLD_ORE:
                            case DEEPSLATE_GOLD_ORE:
                            {
                                goldCount++;
                                break;
                            }
                            case REDSTONE_ORE:
                            case DEEPSLATE_REDSTONE_ORE:
                            {
                                redstoneCount++;
                                break;
                            }
                            case LAPIS_ORE:
                            case DEEPSLATE_LAPIS_ORE:
                            {
                                lapisCount++;
                                break;
                            }
                            case EMERALD_ORE:
                            case DEEPSLATE_EMERALD_ORE:
                            {
                                emeraldCount++;
                                break;
                            }
                            case DIAMOND_ORE:
                            case DEEPSLATE_DIAMOND_ORE:
                            {
                                diamondCount++;
                                break;
                            }
                            // Last but not least: if it's a player block
                            default:
                            {
                                playerBlocks++;
                                break;
                            }
                        }
                    }
                }
            }
        }

        blockCounts.put(
                WOOD,
                blockCounts.getOrDefault(WOOD, 0) + woodCount);

        blockCounts.put(
                COAL_ORE,
                blockCounts.getOrDefault(COAL_ORE, 0) + coalCount);

        blockCounts.put(
                LAPIS_ORE,
                blockCounts.getOrDefault(LAPIS_ORE, 0) + lapisCount);

        blockCounts.put(
                IRON_ORE,
                blockCounts.getOrDefault(IRON_ORE, 0) + ironCount);

        blockCounts.put(
                GOLD_ORE,
                blockCounts.getOrDefault(GOLD_ORE, 0) + goldCount);

        blockCounts.put(
                REDSTONE_ORE,
                blockCounts.getOrDefault(REDSTONE_ORE, 0) + redstoneCount);

        blockCounts.put(
                EMERALD_ORE,
                blockCounts.getOrDefault(EMERALD_ORE, 0) + emeraldCount);

        blockCounts.put(
                DIAMOND_ORE,
                blockCounts.getOrDefault(DIAMOND_ORE, 0) + diamondCount);

        blockCounts.put(
                PLAYER_BLOCK,
                blockCounts.getOrDefault(PLAYER_BLOCK, 0) + playerBlocks);
    }

    private boolean isUncoveredBlock(ChunkSnapshot snapshot, int x, int y, int z, int minY, int maxHeight)
    {
        return (x > 0 && isAir(snapshot.getBlockType(x - 1, y, z)))
                || (x < 15 && isAir(snapshot.getBlockType(x + 1, y, z)))
                || (y > minY && isAir(snapshot.getBlockType(x, y - 1, z)))
                || (y < maxHeight - 1 && isAir(snapshot.getBlockType(x, y + 1, z)))
                || (z > 0 && isAir(snapshot.getBlockType(x, y, z - 1)))
                || (z < 15 && isAir(snapshot.getBlockType(x, y, z + 1)));
    }

    private boolean isAir(Material material)
    {
        return material == Material.AIR || material == Material.CAVE_AIR;
    }
}
