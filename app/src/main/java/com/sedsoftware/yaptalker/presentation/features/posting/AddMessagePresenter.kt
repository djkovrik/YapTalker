package com.sedsoftware.yaptalker.presentation.features.posting

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.sedsoftware.yaptalker.presentation.features.posting.MessageTagCodes.Tag
import ru.terrakok.cicerone.Router
import java.util.Locale
import javax.inject.Inject

@InjectViewState
class AddMessagePresenter @Inject constructor(
  private val router: Router
) : BasePresenter<AddMessageView>() {

  private var isBOpened = false
  private var isIOpened = false
  private var isUOpened = false

  override fun attachView(view: AddMessageView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  override fun detachView(view: AddMessageView?) {
    viewState.hideKeyboard()
    super.detachView(view)
  }

  fun insertChosenTag(selectionStart: Int, selectionEnd: Int, @Tag tag: Long) {
    when {
      tag == MessageTagCodes.TAG_LINK -> onLinkTagClicked()
      selectionStart != selectionEnd -> onTagClickedWithSelection(tag)
      else -> onTagClickedWithNoSelection(tag)
    }
  }

  fun insertVideoTag(url: String, title: String) {
    val result = String.format(Locale.getDefault(), MessageTags.LINK_BLOCK, url, title)
    viewState.insertTag(result)
  }

  fun sendMessageTextBackToView(message: String, isEdited: Boolean) {
    if (isEdited) {
      router.exitWithResult(RequestCode.EDITED_MESSAGE_TEXT, message)
    } else {
      router.exitWithResult(RequestCode.MESSAGE_TEXT, message)
    }
  }

  private fun onTagClickedWithSelection(@Tag tag: Long) {
    when (tag) {
      MessageTagCodes.TAG_B -> {
        viewState.insertTags(MessageTags.B_OPEN, MessageTags.B_CLOSE)
      }
      MessageTagCodes.TAG_I -> {
        viewState.insertTags(MessageTags.I_OPEN, MessageTags.I_CLOSE)
      }
      MessageTagCodes.TAG_U -> {
        viewState.insertTags(MessageTags.U_OPEN, MessageTags.U_CLOSE)
      }
    }
  }

  private fun onTagClickedWithNoSelection(@Tag tag: Long) {
    when (tag) {
      MessageTagCodes.TAG_B -> {
        if (isBOpened) {
          viewState.insertTag(MessageTags.B_CLOSE)
        } else {
          viewState.insertTag(MessageTags.B_OPEN)
        }
        isBOpened = !isBOpened
      }
      MessageTagCodes.TAG_I -> {
        if (isIOpened) {
          viewState.insertTag(MessageTags.I_CLOSE)
        } else {
          viewState.insertTag(MessageTags.I_OPEN)
        }
        isIOpened = !isIOpened
      }
      MessageTagCodes.TAG_U -> {
        if (isUOpened) {
          viewState.insertTag(MessageTags.U_CLOSE)
        } else {
          viewState.insertTag(MessageTags.U_OPEN)
        }
        isUOpened = !isUOpened
      }
    }
  }

  private fun onLinkTagClicked() {
    viewState.showLinkParametersDialogs()
  }
}
