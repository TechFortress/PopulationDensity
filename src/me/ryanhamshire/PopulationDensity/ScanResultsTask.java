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

import java.util.ArrayList;

public class ScanResultsTask implements Runnable 
{
	private ArrayList<String> logEntries;
	private boolean openNewRegion;
	
	public ScanResultsTask(ArrayList<String> logEntries, boolean openNewRegion)
	{
		
		this.logEntries = logEntries;
		this.openNewRegion = openNewRegion;
	}
	
	@Override
	public void run() 
	{
		for (String entry : this.logEntries) {
			PopulationDensity.AddLogEntry(entry);
		}

		if(this.openNewRegion)
		{
			RegionCoordinates newRegion = PopulationDensity.instance.dataStore.addRegion();
			PopulationDensity.instance.scanRegion(newRegion, true);
		}		
	}
}
