package com.github.zingeer.animaltamer.menu

import com.github.rqbik.bukkt.extensions.item.displayName
import com.github.rqbik.bukkt.extensions.item.meta
import com.github.rqbik.bukkt.extensions.scheduler
import com.github.zingeer.animaltamer.localization
import com.github.zingeer.animaltamer.plugin
import com.github.zingeer.inventory.InventoryBuilder
import com.github.zingeer.inventory.InventoryBuilderManager.player
import com.zingeer.sign.SignBuilder
import com.zingeer.sign.SignTexture
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

val FRAME_SLOTS = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 46, 47, 48, 49, 50, 51, 52)
val FRAME_ITEM = ItemStack(Material.BLUE_STAINED_GLASS_PANE).apply {
    displayName = " "
}
val ARROW_ITEM = ItemStack(Material.ARROW)

class AnimalTransferMenu(
    val animal: Tameable,
) {
    fun open(player: Player) {
        val chunkedPlayers = mutableListOf<Player>().apply {
            addAll(Bukkit.getOnlinePlayers())
            remove(player)
        }.chunked(36)

        class AnimalTransferPageMenu(
            val numberPage: Int,
        ) : InventoryBuilder(plugin, 6, localization.get("transfer_pet_menu.name") + "$numberPage/${if (chunkedPlayers.isEmpty()) 1 else chunkedPlayers.size})", {

            FRAME_SLOTS.apply {
                if (numberPage - 1 <= 0) {
                    add(45)
                } else {
                    addButton(45, ARROW_ITEM.apply {
                        displayName = "${ChatColor.GREEN}" + localization.get("transfer_pet_menu.previous_page")
                    }, false) {
                        AnimalTransferPageMenu(numberPage - 1)
                    }
                }
                if (numberPage >= chunkedPlayers.size) {
                    add(53)
                } else {
                    addButton(53, ARROW_ITEM.apply {
                        displayName = "${ChatColor.GREEN}" + localization.get("transfer_pet_menu.next_page")
                    }, false) {
                        AnimalTransferPageMenu(numberPage - 1)
                    }
                }
            }.forEach { slot ->
                addButton(slot, FRAME_ITEM, false) {}
            }

            addButton(48, ItemStack(Material.BOOK).apply {
                 displayName = "${ChatColor.GREEN}" + localization.get("transfer_pet_menu.exit")
            }, false ) {
                AnimalControlMenu(animal).open(player)
            }

            addButton(50, ItemStack(Material.OAK_SIGN).apply {
                displayName = "${ChatColor.YELLOW}" + localization.get("transfer_pet_menu.search.name")
            }, false ) {
                SignBuilder(
                    arrayOf("", "---------------", localization.get("transfer_pet_menu.search.sign"), "---------------"), SignTexture.BIRCH
                ) {
                    val name = lines[0]
                    if (name.isNotEmpty()) {
                        val playerOwned = Bukkit.getPlayer(name)
                        if (playerOwned != null) {
                            animal.owner = playerOwned
                            player.sendMessage(localization.get("transfer_pet_menu.message") + playerOwned.name)
                        } else {
                            player.sendMessage(localization.get("transfer_pet_menu.message_wrong") + name)
                        }
                    }
                }.open(player)
            }

            if (chunkedPlayers.isNotEmpty()) {
                scheduler.runTaskAsynchronously(plugin, Runnable {
                    chunkedPlayers[numberPage - 1].forEachIndexed { index, player ->
                        addButton(index + 9, ItemStack(Material.PLAYER_HEAD).apply {
                            meta {
                                this as SkullMeta
                                this.owningPlayer = player
                                displayName(Component.text("").color(NamedTextColor.WHITE).append(player.displayName()).decoration(TextDecoration.ITALIC, false))
                            }
                        }, false) {
                            animal.owner = player
                            this.player.closeInventory()
                            this.player.sendMessage(localization.get("transfer_pet_menu.message") + player.name)
                        }
                    }
                })
            }
        })

        AnimalTransferPageMenu(1).open(player)
    }

}