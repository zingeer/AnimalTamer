package com.github.zingeer.animaltamer.localization

import org.bukkit.configuration.file.YamlConfiguration

class Localization(
    val config: YamlConfiguration
) {
    fun get(path: String): String = config.getString(path) ?: path
}