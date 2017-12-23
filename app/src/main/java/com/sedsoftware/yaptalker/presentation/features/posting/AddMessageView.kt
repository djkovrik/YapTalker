package com.sedsoftware.yaptalker.presentation.features.posting

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView

@StateStrategyType(SkipStrategy::class)
interface AddMessageView : BaseView {

  fun updateCurrentUiState()

  fun insertTag(tag: String)

  fun insertTags(openingTag: String, closingTag: String)

  fun showLinkParametersDialogs()

  fun hideKeyboard()
}
