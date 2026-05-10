package ru.terrakok.cicerone

class Cicerone private constructor() {
    val navigatorHolder = NavigatorHolder()
    val router = Router(navigatorHolder)

    companion object {
        fun create(): Cicerone = Cicerone()
    }
}
