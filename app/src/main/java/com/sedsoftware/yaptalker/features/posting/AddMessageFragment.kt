package com.sedsoftware.yaptalker.features.posting

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseFragment
import com.sedsoftware.yaptalker.base.events.FragmentLifecycle
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastWarning
import com.sedsoftware.yaptalker.features.topic.ChosenTopicFragment
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.fragment_new_post.*

class AddMessageFragment : BaseFragment(), AddMessageView {

  companion object {
    const val MESSAGE_TEXT_REQUEST = 321
    private const val TOPIC_TITLE_KEY = "TOPIC_TITLE_KEY"

    fun getNewInstance(title: String): ChosenTopicFragment {
      val fragment = ChosenTopicFragment()
      val args = Bundle()
      args.putString(TOPIC_TITLE_KEY, title)
      fragment.arguments = args
      return fragment
    }
  }

  @InjectPresenter
  lateinit var messagingPresenter: AddMessagePresenter

  override val layoutId: Int
    get() = R.layout.fragment_new_post

  private val currentTopicTitle: String by lazy {
    arguments.getString(TOPIC_TITLE_KEY)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    messagingPresenter.updateAppbarTitle("")

    if (currentTopicTitle.isNotEmpty()) {
      new_post_topic_title.text = currentTopicTitle
    }
  }

  override fun subscribeViews() {
    // B
    RxView
        .clicks(new_post_button_bold)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_B)
          }
        }

    // I
    RxView
        .clicks(new_post_button_italic)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_I)
          }
        }

    // U
    RxView
        .clicks(new_post_button_underlined)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_U)
          }
        }

    // Link
    RxView
        .clicks(new_post_button_link)
        .autoDisposeWith(event(FragmentLifecycle.STOP))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_LINK)
          }
        }
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_post_editor, menu)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_send -> {
        returnMessageText()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun insertTag(tag: String) {
    new_post_edit_text.text.insert(new_post_edit_text.selectionStart, tag)
  }

  override fun insertTags(openingTag: String, closingTag: String) {
    new_post_edit_text.text.insert(new_post_edit_text.selectionStart, openingTag)
    new_post_edit_text.text.insert(new_post_edit_text.selectionEnd, closingTag)
  }

  override fun showLinkParametersDialogs() {

    var url: String
    var title: String

    MaterialDialog.Builder(context)
        .title(R.string.post_insert_link)
        .positiveText(R.string.post_button_submit)
        .negativeText(R.string.post_button_dismiss)
        .inputType(InputType.TYPE_CLASS_TEXT)
        .alwaysCallInputCallback()
        .input(R.string.post_insert_link_hint, 0, false, { firstDialog, firstInput ->
          firstDialog.getActionButton(DialogAction.POSITIVE).isEnabled = firstInput.toString().startsWith("http")
        })
        .onPositive { firstDialog, _ ->
          url = firstDialog.inputEditText?.text.toString()

          MaterialDialog.Builder(context)
              .title(R.string.post_insert_link_title)
              .positiveText(R.string.post_button_submit)
              .negativeText(R.string.post_button_dismiss)
              .inputType(InputType.TYPE_CLASS_TEXT)
              .alwaysCallInputCallback()
              .input(R.string.post_insert_link_title_hint, 0, false, { _, _ -> })
              .onPositive { secondDialog, _ ->
                title = secondDialog.inputEditText?.text.toString()

                if (url.isNotEmpty() || title.isNotEmpty()) {
                  messagingPresenter.insertVideoTag(url, title)
                }
              }
              .show()
        }
        .show()
  }


  private fun returnMessageText() {
    if (messagingPresenter.isAnyTagNotClosed()) {
      toastWarning(context.stringRes(R.string.msg_unclosed_tag))
      return
    }

    val message = new_post_edit_text.text.toString()
    if (message.isNotEmpty()) {
      messagingPresenter.sendMessageTextBackToView(message)
    }
  }
}
