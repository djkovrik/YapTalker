package com.sedsoftware.yaptalker.presentation.feature.posting

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.model.YapEntity

@StateStrategyType(SkipStrategy::class)
interface AddMessageView : MvpView, CanShowErrorMessage {

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

  fun showImagePickerDialog()
}
