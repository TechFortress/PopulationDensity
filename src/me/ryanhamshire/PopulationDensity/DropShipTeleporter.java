package me.ryanhamshire.PopulationDensity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by RoboMWM on 12/22/2016.
 * All the events and logic to perform this stuff is all encapsulated here,
 * in case users want to disable the entirety of this thing
 */
public class DropShipTeleporter implements Listener {
    PopulationDensity instance;

    public DropShipTeleporter(PopulationDensity populationDensity)
    {
        this.instance = populationDensity;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event)
    {
        if(isFallDamageImmune(event.getPlayer()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityToggleFlight(EntityToggleGlideEvent event)
    {
        if(event.getEntityType() != EntityType.PLAYER) return;

        if(isFallDamageImmune((Player)event.getEntity()))
        {
            event.setCancelled(true);
        }
    }
   
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(isFallDamageImmune(player) && player.isOnGround()) {
            removeFallDamageImmunity(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event)
    {
        Entity entity = event.getEntity();
        //when an entity has fall damage immunity, it lasts for only ONE fall damage check
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL)
        {
            if(isFallDamageImmune(entity))
            {
                event.setCancelled(true);
                removeFallDamageImmunity(entity);
                if(entity.getType() == EntityType.PLAYER)
                {
                    Player player = (Player)entity;
                    if(!player.hasPermission("populationdensity.teleportanywhere"))
                    {
                        player.getWorld().createExplosion(player.getLocation(), 0);
                    }
                }
            }
        }
    }

    HashSet<UUID> fallImmunityList = new HashSet<UUID>();
    void makeEntityFallDamageImmune(LivingEntity entity)
    {
        if(entity.getType() == EntityType.PLAYER)
        {
            Player player = (Player) entity;
            if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
            player.setGliding(false);
        }
        entity.setGliding(false);
        entity.setMetadata("PD_NOFALLDMG", new FixedMetadataValue(instance, instance));
        fallImmunityList.add(entity.getUniqueId());
    }

    boolean isFallDamageImmune(Entity entity)
    {
        return entity.hasMetadata("PD_NOFALLDMG") || fallImmunityList.contains(entity.getUniqueId());
    }

    void removeFallDamageImmunity(Entity entity)
    {
        entity.removeMetadata("PD_NOFALLDMG", instance);
        fallImmunityList.remove(entity.getUniqueId());
    }
}
