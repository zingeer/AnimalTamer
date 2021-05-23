package com.github.zingeer.animaltamer

import com.github.rqbik.bukkt.extensions.event.register
import com.github.zingeer.animaltamer.localization.Localization
import com.zingeer.sign.SignManager
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

lateinit var plugin: Plugin

lateinit var lang: String
lateinit var localization: Localization

class Plugin : JavaPlugin() {

    override fun onEnable() {
        plugin = this

        loadSetup()
        loadLang()
        EventListener.register(this )
        SignManager.initialization(this)

    }

    fun loadSetup() {
        val file = File(dataFolder.absolutePath, "setup.yml")
        if (!file.exists()) {
            File(dataFolder.absolutePath).mkdirs()
            file.createNewFile()
        }

        lang = YamlConfiguration.loadConfiguration(file).getString("lang") ?: run {
            config.set("lang", "en_EN")
            config.save(file)
            "en_EN"
        }
    }

    fun loadLang() {
        val directory = File(dataFolder.absolutePath, "lang/")
        val file = File(directory, "$lang.yml")

        if (!file.exists()) {
            directory.mkdirs()
            file.createNewFile()
            file.writeText(getTextResource("lang/$lang.yml")?.readText() ?: "{}")
        }
        localization = Localization(YamlConfiguration.loadConfiguration(file))
    }

}