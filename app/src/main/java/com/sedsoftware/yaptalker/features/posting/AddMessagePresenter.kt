package com.sedsoftware.yaptalker.features.posting

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.features.base.BasePresenter

@InjectViewState
class AddMessagePresenter : BasePresenter<AddMessageView>() {

  companion object {
    private const val B_OPEN = "[b]"
    private const val B_CLOSE = "[/b]"
    private const val I_OPEN = "[i]"
    private const val I_CLOSE = "[/i]"
    private const val U_OPEN = "[u]"
    private const val U_CLOSE = "[/u]"

    private var isBOpened = false
    private var isIOpened = false
    private var isUOpened = false
  }

  fun onBoldTagClick() {
    if (isBOpened) {
      viewState.insertTag(B_CLOSE)
    } else {
      viewState.insertTag(B_OPEN)
    }
    isBOpened = !isBOpened
  }

  fun onItalicTagClick() {
    if (isIOpened) {
      viewState.insertTag(I_CLOSE)
    } else {
      viewState.insertTag(I_OPEN)
    }
    isIOpened = !isIOpened
  }

  fun onUnderlineTagClick() {
    if (isUOpened) {
      viewState.insertTag(U_CLOSE)
    } else {
      viewState.insertTag(U_OPEN)
    }
    isUOpened = !isUOpened
  }
}
