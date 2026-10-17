# Administration Commands

Commands for server administrators managing login priority, entity counts, and lag simulation.

---

## `/loginpriority`

Check or set the login queue priority for a player. Priority ranges from 0 (lowest) to 100 (highest). Higher-priority players are admitted first when the server is full.

| Field | Value |
|---|---|
| **Usage** | `/loginpriority <player> [priority 0–100]` |
| **Aliases** | — |
| **Permission** | `populationdensity.setloginpriority` (default: op) |
| **Console** | Yes |

**Notes:**
- Running with only `<player>` (no priority value) prints that player's current priority without changing it.
- Priority persists across restarts (stored per-player in `PlayerData/`).
- Two permission-based tiers exist for players who do not need a custom value:
  - `populationdensity.prioritylogin` → priority 25
  - `populationdensity.elitelogin` → priority 50

---

## `/thinentities`

Force entity thinning to run immediately, removing excess animals and monsters in overcrowded chunks. Normally triggered automatically by `MonitorPerformanceTask`.

| Field | Value |
|---|---|
| **Usage** | `/thinentities` |
| **Aliases** | — |
| **Permission** | `populationdensity.forcethinning` (default: op) |
| **Console** | Yes |

---

## `/simlag`

Simulate a specific TPS value to test lag-response features (grinder shutdown, idle player booting). **For testing only — do not use on a live server.**

| Field | Value |
|---|---|
| **Usage** | `/simlag <ticksPerSecond>` |
| **Aliases** | — |
| **Permission** | `populationdensity.simulatelag` (default: op) |
| **Console** | **Console only** — the command is explicitly rejected when run by a player |

**Example:** `/simlag 10` simulates a heavily lagging server at 10 TPS.
