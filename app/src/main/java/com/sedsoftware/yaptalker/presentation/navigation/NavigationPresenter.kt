package com.sedsoftware.yaptalker.presentation.navigation

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class NavigationPresenter @Inject constructor(private val router: Router) : BasePresenter<NavigationView>()
