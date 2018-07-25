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

import java.util.Calendar;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerData 
{
	public RegionCoordinates homeRegion = null;	
	public Player inviter = null;
	
	//afk-related variables
	public Location lastObservedLocation = null;
	public boolean hasTakenActionThisRound = true;
	public boolean wasInMinecartLastRound = false;
	public int afkCheckTaskID = -1;
	public int loginPriority = 0;
	public boolean advertisedMoveInThisSession = false;
	
	//queue-related variables
	public Date lastDisconnect;
	
	//initialization (includes some new player defaults)
	public PlayerData()
	{
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		this.lastDisconnect = yesterday.getTime();
	}
}