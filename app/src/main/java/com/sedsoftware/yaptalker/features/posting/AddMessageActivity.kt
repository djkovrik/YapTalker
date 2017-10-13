package com.sedsoftware.yaptalker.features.posting

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.features.topic.ChosenTopicController
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.include_main_appbar.*

// TODO() EditText leaks AGAIN
// Investigate or replace with some custom view
class AddMessageActivity : MvpAppCompatActivity(), AddMessageView {

  @InjectPresenter
  lateinit var messagingPresenter: AddMessagePresenter

  private val currentForumId: Int by lazy {
    intent.getIntExtra(ChosenTopicController.FORUM_ID_KEY, 0)
  }

  private val currentTopicId: Int by lazy {
    intent.getIntExtra(ChosenTopicController.TOPIC_ID_KEY, 0)
  }

  private val currentTopicTitle: String by lazy {
    intent.getStringExtra(ChosenTopicController.TOPIC_TITLE_KEY)
  }

  private val currentStartingPost: Int by lazy {
    intent.getIntExtra(ChosenTopicController.START_POST_NUMBER_KEY, 0)
  }

  private val currentAuthKey: String by lazy {
    intent.getStringExtra(ChosenTopicController.AUTH_KEY)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_new_post)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (currentTopicTitle.isNotEmpty()) {
      new_post_topic_title.text = currentTopicTitle
    }

    // B
    RxView
        .clicks(new_post_button_bold)
        .autoDisposeWith(AndroidLifecycleScopeProvider.from(this))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_B)
          }
        }

    // I
    RxView
        .clicks(new_post_button_italic)
        .autoDisposeWith(AndroidLifecycleScopeProvider.from(this))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_I)
          }
        }

    // U
    RxView
        .clicks(new_post_button_underlined)
        .autoDisposeWith(AndroidLifecycleScopeProvider.from(this))
        .subscribe {
          with(new_post_edit_text) {
            messagingPresenter.onTagClicked(selectionStart, selectionEnd, MessageTags.TAG_U)
          }
        }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_post_editor, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_send -> {
        val message = new_post_edit_text.text.toString()
        if (message.isNotEmpty()) {
          messagingPresenter.sendMessage(currentForumId, currentTopicId, currentStartingPost, currentAuthKey, message)
        }
        finish()
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

  override fun showErrorMessage(message: String) {
    toastError(message)
  }
}
