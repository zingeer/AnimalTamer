package com.github.zingeer.animaltamer

import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataType

const val SADDLE_KEY = "animal.saddle"
const val TRUNK_KEY = "animal.trunk"

object AnimaManager {


    fun Entity.getArgument(key: String): String? =
        persistentDataContainer.get(NamespacedKey(plugin, key), PersistentDataType.STRING)

    fun Entity.setArgument(key: String, value: String) =
        persistentDataContainer.set(NamespacedKey(plugin, key), PersistentDataType.STRING, value)

}