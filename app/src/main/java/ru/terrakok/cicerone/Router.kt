package ru.terrakok.cicerone

import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

class Router internal constructor(private val navigatorHolder: NavigatorHolder) {
    private val resultListeners = mutableMapOf<Int, (Any?) -> Unit>()

    fun navigateTo(screenKey: String, data: Any? = null) {
        navigatorHolder.navigator?.applyCommands(arrayOf(Forward(screenKey, data)))
    }

    fun newRootScreen(screenKey: String, data: Any? = null) {
        navigatorHolder.navigator?.applyCommands(arrayOf(Replace(screenKey, data)))
    }

    fun exit() {
        navigatorHolder.navigator?.applyCommands(arrayOf(Back))
    }

    fun exitWithResult(resultCode: Int, result: Any?) {
        resultListeners[resultCode]?.invoke(result)
        exit()
    }

    fun setResultListener(resultCode: Int, listener: (Any?) -> Unit) {
        resultListeners[resultCode] = listener
    }

    fun removeResultListener(resultCode: Int) {
        resultListeners.remove(resultCode)
    }
}
