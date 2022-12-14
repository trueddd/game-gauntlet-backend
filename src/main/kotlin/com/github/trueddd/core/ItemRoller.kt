package com.github.trueddd.core

import com.github.trueddd.data.items.InventoryItem
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class ItemRoller(
    @Named(InventoryItem.Factory.SET_NAME)
    private val allItemsFactories: Set<InventoryItem.Factory>,
) {

    fun pick(): InventoryItem {
        return allItemsFactories.random().create()
    }
}
