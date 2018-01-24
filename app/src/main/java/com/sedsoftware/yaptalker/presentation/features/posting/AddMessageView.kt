package com.sedsoftware.yaptalker.presentation.features.posting

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.BaseView
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface AddMessageView : BaseView {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendEmojiItem(emoji: YapEntity)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearEmojiList()

  fun updateCurrentUiState()

  fun insertTag(tag: String)

  fun insertTags(openingTag: String, closingTag: String)

  fun showLinkParametersDialogs()

  fun showVideoLinkParametersDialog()

  fun hideKeyboard()

  fun callForSmilesBottomSheet()
}
