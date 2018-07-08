package com.sedsoftware.yaptalker.presentation.feature.topic.fabmenu

import java.util.ArrayList

class FabMenu(var isMenuExpanded: Boolean) {

    fun add(item: FabMenuItem) {
        menuItems.add(item)
    }

    fun clear() {
        menuItems.clear()
    }

    fun showItems() {
        isMenuExpanded = true
        menuItems.forEach { item -> item.show() }
    }

    fun hideItems() {
        isMenuExpanded = false
        menuItems.forEach { item -> item.hide() }
    }

    private val menuItems: MutableList<FabMenuItem> = ArrayList()
}
