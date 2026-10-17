# Social Commands

Commands for player-to-player interaction within the region system.

---

## `/invite`

Invite an online player to visit your home region. The invited player can then use `/visit <your name>` even if your region is unnamed.

| Field | Value |
|---|---|
| **Usage** | `/invite <player name>` |
| **Aliases** | `invitetoregion`, `regioninvite`, `inviteregion` |
| **Permission** | None (all players) |

**Notes:**
- Only works with online, visible players.
- The invite persists until the server restarts or a new invite overwrites it.
- The invited player receives an in-game notification with instructions.

---

## `/sendregion`

Teleport another player to their own home region, or to a specific named region. Useful for moderating players or redirecting griefers.

| Field | Value |
|---|---|
| **Usage** | `/sendregion <player> [region name]` |
| **Aliases** | `sendtoregion` |
| **Permission** | `populationdensity.sendregion` (default: op) |

**Notes:**
- If no region is specified, the target player is sent to their home region.
- Can be run from the console.
