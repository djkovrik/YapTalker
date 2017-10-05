package com.sedsoftware.yaptalker.features.posting

import android.os.Bundle
import android.view.Menu
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.features.topic.ChosenTopicController
import kotlinx.android.synthetic.main.include_main_appbar.*
import timber.log.Timber

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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_new_post)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    Timber.d("currentForumId = $currentForumId, currentTopicId = $currentTopicId")
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_post_editor, menu)
    return true
  }
}
