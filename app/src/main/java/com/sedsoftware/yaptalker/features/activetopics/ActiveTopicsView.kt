package com.sedsoftware.yaptalker.features.activetopics

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsPage

@StateStrategyType(SkipStrategy::class)
interface ActiveTopicsView : BaseView {

  @StateStrategyType(AddToEndSingleStrategy::class)
  fun displayActiveTopicsPage(page: ActiveTopicsPage)

  fun scrollToViewTop()

  fun showCantLoadPageMessage(page: Int)
}
