package com.sedsoftware.yaptalker.features.posting

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseActivity
import com.sedsoftware.yaptalker.base.events.ActivityLifecycle
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.include_main_appbar.*

// TODO() EditText leaks AGAIN
// Investigate or replace with some custom view

class AddMessageActivity : BaseActivity(), AddMessageView {

  @InjectPresenter
  lateinit var messagingPresenter: AddMessagePresenter

  override val layoutId: Int
    get() = R.layout.activity_new_post

//  private val currentTopicTitle: String by lazy {
//    intent.getStringExtra(ChosenTopicController.TOPIC_TITLE_KEY)
//  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

//    if (currentTopicTitle.isNotEmpty()) {
//      new_post_topic_title.text = currentTopicTitle
//    }
  }

  override fun subscribeViews() {

    // B
    RxView
        .clicks(new_post_button_bold)
        .autoDisposeWith(event(ActivityLifecycle.DESTROY))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_B)
          }
        }

    // I
    RxView
        .clicks(new_post_button_italic)
        .autoDisposeWith(event(ActivityLifecycle.DESTROY))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_I)
          }
        }

    // U
    RxView
        .clicks(new_post_button_underlined)
        .autoDisposeWith(event(ActivityLifecycle.DESTROY))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_U)
          }
        }

    // Link
    RxView
        .clicks(new_post_button_link)
        .autoDisposeWith(event(ActivityLifecycle.DESTROY))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_LINK)
          }
        }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_post_editor, menu)
    return true
  }

  override fun showErrorMessage(message: String) {

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

    MaterialDialog.Builder(this)
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

          MaterialDialog.Builder(this)
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

//    if (messagingPresenter.isAnyTagNotClosed()) {
//      toastWarning(stringRes(R.string.msg_unclosed_tag))
//      return
//    }
//
//    val message = new_post_edit_text.text.toString()
//    if (message.isNotEmpty()) {
//      val returnIntent = Intent()
//      returnIntent.putExtra(ChosenTopicController.MESSAGE_TEXT_KEY, message)
//      setResult(Activity.RESULT_OK, returnIntent)
//    }
//    finish()
  }
}
