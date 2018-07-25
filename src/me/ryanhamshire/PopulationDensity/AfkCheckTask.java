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

import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

public class AfkCheckTask implements Runnable 
{
	private Player player;
	private PlayerData playerData;
	
	public AfkCheckTask(Player player, PlayerData playerData)
	{
		this.player = player;
		this.playerData = playerData;
	}
	
	@Override
	public void run() 
	{
		if(!player.isOnline()) return;
		
		boolean kick = false;
		
		//if the player is and has been in a minecart, kick him
		if(player.getVehicle() instanceof Minecart)
		{
			 if(playerData.wasInMinecartLastRound)
			 {
				 kick = true;
			 }
			 
			 playerData.wasInMinecartLastRound = true;
		}
		
		else
		{
			playerData.wasInMinecartLastRound = false;
		}
		
		//if the player hasn't moved, kick him
		try
		{
			if(playerData.lastObservedLocation != null && (playerData.lastObservedLocation.distance(player.getLocation()) < 3))
			{
				kick = true;
			}
		}
		catch(IllegalArgumentException exception){}
		
		int playersOnline = PopulationDensity.instance.getServer().getOnlinePlayers().size();
		if(!player.hasPermission("populationdensity.idle") && kick &&
		   (PopulationDensity.bootingIdlePlayersForLag ||
		    PopulationDensity.instance.getServer().getMaxPlayers() - PopulationDensity.instance.reservedSlotsForAdmins - 3 <= playersOnline))
		{
			PopulationDensity.AddLogEntry("Kicked " + player.getName() + " for idling.");
			player.kickPlayer("Kicked for idling, to make room for active players.");
			return;
		}
		
		playerData.lastObservedLocation = player.getLocation();
		
		//otherwise, restart the timer for this task
		//20L ~ 1 second		
		playerData.afkCheckTaskID = PopulationDensity.instance.getServer().getScheduler().scheduleSyncDelayedTask(PopulationDensity.instance, this, 20L * 60 * PopulationDensity.instance.maxIdleMinutes);
	}
}
