package com.sedsoftware.yaptalker.presentation.feature.posting

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.EmojiInteractor
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.RequestCode
import com.sedsoftware.yaptalker.presentation.feature.posting.adapter.EmojiClickListener
import com.sedsoftware.yaptalker.presentation.feature.posting.tags.MessageTagCodes
import com.sedsoftware.yaptalker.presentation.feature.posting.tags.MessageTagCodes.Tag
import com.sedsoftware.yaptalker.presentation.feature.posting.tags.MessageTags
import com.sedsoftware.yaptalker.presentation.mapper.EmojiModelMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@InjectViewState
class AddMessagePresenter @Inject constructor(
  private val router: Router,
  private val emojiInteractor: EmojiInteractor,
  private val emojiMapper: EmojiModelMapper
) : BasePresenter<AddMessageView>(), EmojiClickListener {

  private var clearCurrentList = false

  private var isBOpened = false
  private var isIOpened = false
  private var isUOpened = false

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    loadEmojiList()
  }

  override fun attachView(view: AddMessageView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  override fun detachView(view: AddMessageView?) {
    viewState.hideKeyboard()
    super.detachView(view)
  }

  override fun onEmojiClicked(code: String) {
    viewState.insertTag(" $code ")
  }

  fun insertChosenTag(selectionStart: Int, selectionEnd: Int, @Tag tag: Long) {
    when {
      tag == MessageTagCodes.TAG_LINK -> onLinkTagClicked()
      tag == MessageTagCodes.TAG_VIDEO -> onVideoLinkTagClicked()
      selectionStart != selectionEnd -> onTagClickedWithSelection(tag)
      else -> onTagClickedWithNoSelection(tag)
    }
  }

  fun insertLinkTag(url: String, title: String) {
    val result = String.format(Locale.getDefault(), MessageTags.LINK_BLOCK, url, title)
    viewState.insertTag(result)
  }

  fun insertVideoTag(url: String) {
    val result = String.format(Locale.getDefault(), MessageTags.VIDEO_BLOCK, url)
    viewState.insertTag(result)
  }

  fun sendMessageTextBackToView(message: String, isEdited: Boolean, chosenImagePath: String) {
    if (isEdited) {
      router.exitWithResult(RequestCode.EDITED_MESSAGE_TEXT, message)
    } else {
      router.exitWithResult(RequestCode.MESSAGE_TEXT, Pair(message, chosenImagePath))
    }
  }

  fun onSmilesButtonClicked() {
    viewState.hideKeyboard()
    viewState.callForSmilesBottomSheet()
  }

  fun onImageAttachButtonClicked() {
    viewState.showImagePickerDialog()
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

  private fun onVideoLinkTagClicked() {
    viewState.showVideoLinkParametersDialog()
  }

  private fun loadEmojiList() {

    clearCurrentList = true

    emojiInteractor
      .loadEmojiList()
      .subscribeOn(Schedulers.io())
      .map(emojiMapper)
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe(getEmojiObserver())
  }

  private fun getEmojiObserver() =
    object : DisposableObserver<YapEntity>() {

      override fun onNext(item: YapEntity) {

        if (clearCurrentList) {
          clearCurrentList = false
          viewState.clearEmojiList()
        }

        viewState.appendEmojiItem(item)
      }

      override fun onComplete() {
        Timber.i("Emoji list loading completed.")
      }

      override fun onError(error: Throwable) {
        error.message?.let { viewState.showErrorMessage(it) }
      }
    }
}
