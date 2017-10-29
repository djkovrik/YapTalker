package com.sedsoftware.yaptalker.features.posting

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter
import com.sedsoftware.yaptalker.features.posting.MessageTags.Tag
import java.util.Locale

@InjectViewState
class AddMessagePresenter : BasePresenter<AddMessageView>() {

  companion object {
    private const val B_OPEN = "[b]"
    private const val B_CLOSE = "[/b]"
    private const val I_OPEN = "[i]"
    private const val I_CLOSE = "[/i]"
    private const val U_OPEN = "[u]"
    private const val U_CLOSE = "[/u]"
    private const val LINK_BLOCK = "[url=%s]%s[/url]"

    private var isBOpened = false
    private var isIOpened = false
    private var isUOpened = false
  }

  fun onTagClicked(selectionStart: Int, selectionEnd: Int, @Tag tag: Long) {
    when {
      tag == MessageTags.TAG_LINK -> onLinkTagClicked()
      selectionStart != selectionEnd -> onTagClickedWithSelection(tag)
      else -> onTagClickedWithNoSelection(tag)
    }
  }

  fun insertVideoTag(url: String, title: String) {
    val result = String.format(Locale.getDefault(), LINK_BLOCK, url, title)
    viewState.insertTag(result)
  }

  fun isAnyTagNotClosed() = isBOpened || isIOpened || isUOpened

  fun sendMessageTextBackToView(message: String) {
    router.exitWithResult(AddMessageFragment.MESSAGE_TEXT_REQUEST, message)
  }

  private fun onTagClickedWithSelection(@Tag tag: Long) {
    when (tag) {
      MessageTags.TAG_B -> {
        viewState.insertTags(B_OPEN, B_CLOSE)
      }
      MessageTags.TAG_I -> {
        viewState.insertTags(I_OPEN, I_CLOSE)
      }
      MessageTags.TAG_U -> {
        viewState.insertTags(U_OPEN, U_CLOSE)
      }
    }
  }

  private fun onTagClickedWithNoSelection(@Tag tag: Long) {
    when (tag) {
      MessageTags.TAG_B -> {
        if (isBOpened) {
          viewState.insertTag(B_CLOSE)
        } else {
          viewState.insertTag(B_OPEN)
        }
        isBOpened = !isBOpened
      }
      MessageTags.TAG_I -> {
        if (isIOpened) {
          viewState.insertTag(I_CLOSE)
        } else {
          viewState.insertTag(I_OPEN)
        }
        isIOpened = !isIOpened
      }
      MessageTags.TAG_U -> {
        if (isUOpened) {
          viewState.insertTag(U_CLOSE)
        } else {
          viewState.insertTag(U_OPEN)
        }
        isUOpened = !isUOpened
      }
    }
  }

  private fun onLinkTagClicked() {
    viewState.showLinkParametersDialogs()
  }
}
