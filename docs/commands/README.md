# PopulationDensity Commands Reference

All commands are prefixed with `/`. Commands that require a permission show its node and default level; commands with no permission entry are available to everyone.

| Group | File | Summary |
|---|---|---|
| [Teleportation](#) | [teleportation.md](teleportation.md) | Move between regions |
| [Region Management](#) | [region-management.md](region-management.md) | Create, name, scan, and delete regions |
| [Social](#) | [social.md](social.md) | Invite players and force-move them |
| [Information](#) | [information.md](information.md) | Query region or server status |
| [Administration](#) | [administration.md](administration.md) | Login queue, entity control, lag simulation |

## Permission Overview

| Permission node | Default | Purpose |
|---|---|---|
| `populationdensity.*` | op | All administrative privileges |
| `populationdensity.teleportanywhere` | op | Teleport from any location (bypass post requirement) |
| `populationdensity.buildbreakanywhere` | op | Edit anywhere, including region posts |
| `populationdensity.addregion` | op | `/addregion`, `/scanregion`, `/addregionpost` |
| `populationdensity.nameregion` | op | `/nameregion` |
| `populationdensity.renameregion` | op | `/renameregion` |
| `populationdensity.deleteRegion` | op | `/deleteregion` |
| `populationdensity.sendregion` | op | `/sendregion` |
| `populationdensity.setloginpriority` | op | `/loginpriority` |
| `populationdensity.forcethinning` | op | `/thinentities` |
| `populationdensity.simulatelag` | op | `/simlag` |
| `populationdensity.newestregion` | op | `/newestregion` (only when `NewestRegionRequiresPermission: true`) |
| `populationdensity.adminlogin` | op | Fill admin-reserved login slots |
| `populationdensity.idle` | op | Stay online without being kicked for idling |
| `populationdensity.unlimitedhoppers` | op | Ignore the per-chunk hopper cap |
| `populationdensity.randomregion` | true | `/randomregion` |
| `populationdensity.listregions` | true | `/listregions` |
| `populationdensity.lag` | true | `/lag` |
| `populationdensity.prioritylogin` | false | Login priority level 25/100 |
| `populationdensity.elitelogin` | false | Login priority level 50/100 |
