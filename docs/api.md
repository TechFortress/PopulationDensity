# Developer API

Public methods that other plugins can call to read PopulationDensity's region data.

Get the plugin instance from the plugin manager:

```java
PopulationDensity populationDensity =
    (PopulationDensity) Bukkit.getPluginManager().getPlugin("PopulationDensity");
```

---

## Region post locations

All of these methods must be called from the main server thread. They read plugin data that isn't thread-safe.

These methods return the same coordinates that players see with `/whereregion <region name>` and `/myregion`.

```java
// Post location of a named region, or null if no region has that name
Location forest = populationDensity.getRegionPostLocation("Forest");

// Post location of a player's assigned region, or null if they don't have one
Location home = populationDensity.getAssignedRegionPostLocation(player);

// Grid coordinates of a player's assigned region, or null if they don't have one
RegionCoordinates region = populationDensity.getAssignedRegion(player);
```

| Method | Returns |
|---|---|
| `getRegionPostLocation(String regionName)` | The region post's `Location`, or `null` if the name is unknown. Region names are case-insensitive. |
| `getAssignedRegionPostLocation(OfflinePlayer player)` | The `Location` of the post in the player's assigned region, or `null` if they don't have one. |
| `getAssignedRegion(OfflinePlayer player)` | The `RegionCoordinates` of the player's assigned region, or `null` if they don't have one. |

The returned location is at the region post's X and Z. Y is not the height of the post: it's the `MinimumRegionPostY` value from the config, so the methods don't have to load the region's chunk. Use X and Z only, for example as an untracked lodestone compass target:

```java
CompassMeta meta = (CompassMeta) compass.getItemMeta();
meta.setLodestone(home);
meta.setLodestoneTracked(false); // point at the post without needing a lodestone block there
compass.setItemMeta(meta);
```
