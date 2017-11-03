package com.sedsoftware.yaptalker.features

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

val navigationModule = Kodein.Module {

  bind<Cicerone<Router>>() with singleton { Cicerone.create() }

  bind<Router>() with singleton { instance<Cicerone<Router>>().router }

  bind<NavigatorHolder>() with singleton { instance<Cicerone<Router>>().navigatorHolder }
}
