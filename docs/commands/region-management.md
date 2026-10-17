# Region Management Commands

Commands for creating, naming, scanning, and deleting regions in the managed world.

---

## `/sethomeregion`

Set your home region to whichever region you are currently standing in.

| Field | Value |
|---|---|
| **Usage** | `/sethomeregion` |
| **Aliases** | `movein`, `sethomepost` |
| **Permission** | None (all players) |

**Notes:** Must be used while standing inside the managed world.

---

## `/nameregion`

Give an unnamed wilderness region a custom name. Names may only contain letters and digits (no spaces or symbols) and must not exceed the `Maximum Region Name Length` config value (default: 10).

| Field | Value |
|---|---|
| **Usage** | `/nameregion <name>` |
| **Aliases** | — |
| **Permission** | `populationdensity.nameregion` (default: op) |

**Notes:** Fails if the region already has a name — use `/renameregion` instead.

---

## `/renameregion`

Rename an already-named region. Follows the same naming rules as `/nameregion`.

| Field | Value |
|---|---|
| **Usage** | `/renameregion <name>` |
| **Aliases** | — |
| **Permission** | `populationdensity.renameregion` (default: op) |

---

## `/addregion`

Close the current open region and open the next one in the spiral grid. The new region is immediately scanned for resources.

| Field | Value |
|---|---|
| **Usage** | `/addregion` |
| **Aliases** | — |
| **Permission** | `populationdensity.addregion` (default: op) |

---

## `/addregionpost`

Regenerate the region post structure at the centre of the region you are standing in.

| Field | Value |
|---|---|
| **Usage** | `/addregionpost` |
| **Aliases** | — |
| **Permission** | `populationdensity.addregion` (default: op) |

---

## `/scanregion`

Scan the region you are standing in for wood and resource availability. The scan strategy (simple vs advanced) is controlled by `AdvancedScanStrategy` in `config.yml`. Runs asynchronously.

| Field | Value |
|---|---|
| **Usage** | `/scanregion` |
| **Aliases** | — |
| **Permission** | `populationdensity.addregion` (default: op) |

---

## `/scanregionoriginal`

Run the original (synchronous, main-thread) region resource scan. **Deprecated** — may block the server under heavy load. Use `/scanregion` instead.

| Field | Value |
|---|---|
| **Usage** | `/scanregionoriginal` |
| **Aliases** | — |
| **Permission** | `populationdensity.addregion` (default: op) |

---

## `/deleteregion`

Permanently delete a named region and its data files. This action is irreversible.

| Field | Value |
|---|---|
| **Usage** | `/deleteregion <region name>` |
| **Aliases** | — |
| **Permission** | `populationdensity.deleteRegion` (default: op) |

**Warning:** There is no confirmation prompt. The region data is deleted immediately.
