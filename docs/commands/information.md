# Information Commands

Commands for querying region and server status.

---

## `/whichregion`

Display the name of the region you are currently standing in, and the XYZ coordinates of its region post.

| Field | Value |
|---|---|
| **Usage** | `/whichregion` |
| **Aliases** | — |
| **Permission** | None (all players) |

---

## `/myregion`

Display your assigned (home) region by default, including its name and the X and Z coordinates of its region post. Specify a region name to locate another named region, for example `/whereregion Forest`.

| Field | Value |
|---|---|
| **Usage** | `/myregion [region name]` |
| **Aliases** | `whereregion`, `assignedregion` |
| **Permission** | None for `/myregion`; `populationdensity.regionlookup` (default: **true** — all players) when a region name is given |

---

## `/listregions`

List all currently named regions on the server.

| Field | Value |
|---|---|
| **Usage** | `/listregions` |
| **Aliases** | `regions`, `regionlist`, `regionslist`, `allregions` |
| **Permission** | `populationdensity.listregions` (default: **true** — all players) |

---

## `/lag`

Report the server's current TPS (ticks per second) as a performance percentage. 100 % = 20 TPS (no lag).

| Field | Value |
|---|---|
| **Usage** | `/lag` |
| **Aliases** | — |
| **Permission** | `populationdensity.lag` (default: **true** — all players) |

**Notes:** Can also be run from the console; the result is printed to the server log.
