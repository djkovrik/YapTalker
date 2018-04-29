package com.sedsoftware.yaptalker.presentation.feature.posting

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sedsoftware.yaptalker.presentation.base.CanShowErrorMessage
import com.sedsoftware.yaptalker.presentation.base.CanUpdateUiState
import com.sedsoftware.yaptalker.presentation.model.base.EmojiModel

@StateStrategyType(SkipStrategy::class)
interface AddMessageView : MvpView, CanShowErrorMessage, CanUpdateUiState {

  @StateStrategyType(AddToEndStrategy::class)
  fun appendEmojiItem(emoji: EmojiModel)

  @StateStrategyType(SingleStateStrategy::class)
  fun clearEmojiList()

  fun insertTag(tag: String)

  fun insertTags(openingTag: String, closingTag: String)

  fun showLinkParametersDialogs()

  fun showVideoLinkParametersDialog()

  fun hideKeyboard()

  fun callForSmilesBottomSheet()

  fun showImagePickerDialog()
}
