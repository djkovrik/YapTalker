package ru.terrakok.cicerone

import ru.terrakok.cicerone.commands.Command

class NavigatorHolder {
    internal var navigator: Navigator? = null
    private val pendingCommands = mutableListOf<Array<out Command>>()

    fun setNavigator(navigator: Navigator) {
        this.navigator = navigator
        pendingCommands.forEach(navigator::applyCommands)
        pendingCommands.clear()
    }

    fun removeNavigator() {
        navigator = null
    }

    internal fun executeCommands(commands: Array<out Command>) {
        val currentNavigator = navigator

        if (currentNavigator != null) {
            currentNavigator.applyCommands(commands)
        } else {
            pendingCommands.add(commands)
        }
    }
}
