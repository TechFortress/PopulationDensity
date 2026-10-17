# Teleportation Commands

Commands that move the player between regions. Most require the player to be standing near a region post unless `TeleportFromAnywhere: true` is set in `config.yml`, or the player has `populationdensity.teleportanywhere`.

---

## `/visit`

Teleport to another player's home region or to a named region.

| Field | Value |
|---|---|
| **Usage** | `/visit <player name>` or `/visit <region name>` |
| **Aliases** | `visitregion`, `vr`, `visitpost` |
| **Permission** | None (all players) |

**Notes:**
- Visiting an **unnamed region** via player name requires the target player to have sent you an `/invite` first.
- Visiting a **named region** by name works without an invitation.
- Tab-completion is available for region names.

---

## `/homeregion`

Teleport back to your own home region.

| Field | Value |
|---|---|
| **Usage** | `/homeregion` |
| **Aliases** | `gohome`, `homepost` |
| **Permission** | None (all players) |

---

## `/cityregion`

Teleport to the server spawn (city world). Falls back to `/homeregion` behaviour if no city world is configured.

| Field | Value |
|---|---|
| **Usage** | `/cityregion` |
| **Aliases** | `spawn`, `spawnregion` |
| **Permission** | None (all players) |

---

## `/newestregion`

Teleport to the currently open (newest) region — useful for exploring fresh land.

| Field | Value |
|---|---|
| **Usage** | `/newestregion` |
| **Aliases** | — |
| **Permission** | `populationdensity.newestregion` (default: op) **only** when `NewestRegionRequiresPermission: true` in config; otherwise no permission required |

---

## `/randomregion`

Teleport to a randomly selected named region.

| Field | Value |
|---|---|
| **Usage** | `/randomregion` |
| **Aliases** | — |
| **Permission** | `populationdensity.randomregion` (default: **true** — all players) |
