package com.sedsoftware.yaptalker.features.activetopics

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.base.BaseView
import com.sedsoftware.yaptalker.data.model.ActiveTopicsPage

@StateStrategyType(AddToEndSingleStrategy::class)
interface ActiveTopicsView : BaseView {

  fun displayActiveTopicsPage(page: ActiveTopicsPage)

  fun scrollToViewTop()

  fun showCantLoadPageMessage(page: Int)
}
