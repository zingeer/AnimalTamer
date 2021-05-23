package com.github.zingeer.animaltamer

import com.github.zingeer.animaltamer.AnimaManager.getArgument
import com.github.zingeer.animaltamer.menu.AnimalControlMenu
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerLeashEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerUnleashEntityEvent
import org.bukkit.event.vehicle.VehicleEnterEvent

object EventListener : Listener {

    @EventHandler
    fun onVehicleEvent(event: VehicleEnterEvent) {
        val horse = event.vehicle
        val player = event.entered
        if (horse is Tameable && player is Player) {
            if (!horse.getArgument(SADDLE_KEY).toBoolean()) {
                if ((horse.owner?.name ?: return) != player.name) {
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onInteractEntityEvent(event: PlayerInteractEntityEvent) {
        val player = event.player
        val entity = event.rightClicked

        if (entity is Tameable) {
            if (player.isSneaking) {
                if ((entity.owner?.name ?: return) == player.name) {
                    event.isCancelled = true
                    AnimalControlMenu(entity).open(player)
                } else {
                    if (!entity.getArgument(TRUNK_KEY).toBoolean()) {
                        event.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler
    fun unleashEvent(event: PlayerUnleashEntityEvent) {
        val entity = event.entity
        val player = event.player
        if (entity is Tameable) {
            if (!entity.getArgument(SADDLE_KEY).toBoolean()) {
                if (entity.owner != player) {
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun unleashEvent(event: PlayerLeashEntityEvent) {
        val entity = event.entity
        val player = event.player
        if (entity is Tameable) {
            if (!entity.getArgument(SADDLE_KEY).toBoolean()) {
                if (entity.owner != player) {
                    event.isCancelled = true
                }
            }
        }
    }
}