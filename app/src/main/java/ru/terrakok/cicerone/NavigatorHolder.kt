package ru.terrakok.cicerone

class NavigatorHolder {
    internal var navigator: Navigator? = null

    fun setNavigator(navigator: Navigator) {
        this.navigator = navigator
    }

    fun removeNavigator() {
        navigator = null
    }
}
