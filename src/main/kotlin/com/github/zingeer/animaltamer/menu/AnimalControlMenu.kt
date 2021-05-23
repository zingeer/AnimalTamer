package com.github.zingeer.animaltamer.menu

import com.github.rqbik.bukkt.extensions.item.displayName
import com.github.rqbik.bukkt.extensions.scheduler
import com.github.zingeer.animaltamer.AnimaManager.getArgument
import com.github.zingeer.animaltamer.AnimaManager.setArgument
import com.github.zingeer.animaltamer.SADDLE_KEY
import com.github.zingeer.animaltamer.TRUNK_KEY
import com.github.zingeer.animaltamer.localization
import com.github.zingeer.animaltamer.plugin
import com.github.zingeer.inventory.InventoryBuilder
import com.github.zingeer.inventory.InventoryBuilderManager.player
import com.zingeer.sign.SignBuilder
import com.zingeer.sign.SignTexture
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable
import org.bukkit.entity.Vehicle
import org.bukkit.inventory.ItemStack

class AnimalControlMenu(
    val animal: Tameable,
) : InventoryBuilder(plugin, if (animal is Vehicle) 5 else 3, localization.get("control_pet.name"), {

    addButton(11, ItemStack(Material.PLAYER_HEAD).apply {
        displayName = "${ChatColor.LIGHT_PURPLE}" + localization.get("transfer_pet.name")
        lore(
            listOf(
                Component.text(" "),
                Component.text(localization.get("transfer_pet.description_one")).color(NamedTextColor.GRAY),
                Component.text(localization.get("transfer_pet.description_two")).color(NamedTextColor.GRAY),
                Component.text(" ")
            )
        )
    }, false) {
        AnimalTransferMenu(animal).open(player)
    }

    addButton(13, ItemStack(Material.BOOK).apply {
        displayName = "${ChatColor.BLUE}" + localization.get("information.name")
        lore(
            listOf(
                Component.text(localization.get("information.description")).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false).append((animal.owner as Player).displayName())
            )
        )
    }, false) {}

    addButton(15, ItemStack(Material.NAME_TAG).apply {
        displayName = "${ChatColor.YELLOW}" + localization.get("set_name_pet.name")
        lore(
            listOf(
                Component.text(" "),
                Component.text(localization.get("set_name_pet.description_one")).color(NamedTextColor.GRAY),
                Component.text(localization.get("set_name_pet.description_two")).color(NamedTextColor.GRAY),
                Component.text(" ")
            )
        )
    }, false) {
        player.closeInventory()
        scheduler.runTaskAsynchronously(plugin, Runnable {
            SignBuilder(
                arrayOf("", "---------------", localization.get("set_name_pet.sign"), "---------------"), SignTexture.BIRCH
            ) {
                val name = lines[0]
                animal.customName = name
                if (name.isNotEmpty()) {
                    player.sendMessage(localization.get("set_name_pet.message") + name)
                } else {
                    player.sendMessage(localization.get("set_name_pet.message_dropping") + name)
                }
            }.open(player)
        })
    }

    if (animal is Vehicle) {
        addButton(30,
            if (animal.getArgument(SADDLE_KEY).toBoolean()) ItemStack(Material.GREEN_WOOL).apply {
                displayName = "${ChatColor.YELLOW}" + localization.get("horse_key.name")
                lore(listOf(
                    Component.text(" "),
                    Component.text(localization.get("horse_key.description_available")).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                    Component.text(" ")
                ))
            } else ItemStack(Material.RED_WOOL).apply {
                displayName = "${ChatColor.YELLOW}" + localization.get("horse_key.name")
                lore(listOf(
                    Component.text(" "),
                    Component.text(localization.get("horse_key.description_not_available")).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false),
                    Component.text(" ")
                ))
            },
            false
        ) {
            animal.setArgument(SADDLE_KEY, (!(animal.getArgument(SADDLE_KEY)?.toBoolean() ?: false)).toString())
            open(player)

        }
        addButton(32,
            if (animal.getArgument(TRUNK_KEY).toBoolean()) ItemStack(Material.GREEN_WOOL).apply {
                displayName = "${ChatColor.YELLOW}" + localization.get("trunk_key.name")
                lore(listOf(
                    Component.text(" "),
                    Component.text(localization.get("trunk_key.description_available")).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                    Component.text(" ")
                ))
            } else ItemStack(Material.RED_WOOL).apply {
                displayName = "${ChatColor.YELLOW}" + localization.get("trunk_key.name")
                lore(listOf(
                    Component.text(" "),
                    Component.text(localization.get("trunk_key.description_not_available")).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false),
                    Component.text(" ")
                ))
            },
            false
        ) {
            animal.setArgument(TRUNK_KEY, (!(animal.getArgument(TRUNK_KEY)?.toBoolean() ?: false)).toString())
            open(player)
        }
    }

})