package com.sedsoftware.yaptalker.presentation.features.navigation

import android.text.Spanned
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface MainActivityView : BaseView {

  fun setAppbarTitle(title: String)

  fun selectNavDrawerItem(item: Long)

  fun requestEulaDisplaying()

  fun displayFormattedEulaText(spanned: Spanned)
}
