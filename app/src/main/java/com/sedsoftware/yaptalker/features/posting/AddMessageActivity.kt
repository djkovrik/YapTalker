package com.sedsoftware.yaptalker.features.posting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.features.topic.ChosenTopicController
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.include_main_appbar.*

// TODO() EditText leaks AGAIN
// Investigate or replace with some custom view

// TODO() Improve editor layout
class AddMessageActivity : MvpAppCompatActivity(), AddMessageView {

  @InjectPresenter
  lateinit var messagingPresenter: AddMessagePresenter

  private val currentTopicTitle: String by lazy {
    intent.getStringExtra(ChosenTopicController.TOPIC_TITLE_KEY)
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

  private fun returnMessageText() {
    val message = new_post_edit_text.text.toString()
    if (message.isNotEmpty()) {
      val returnIntent = Intent()
      returnIntent.putExtra(ChosenTopicController.MESSAGE_TEXT_KEY, message)
      setResult(Activity.RESULT_OK, returnIntent)
    }
    finish()
  }
}
