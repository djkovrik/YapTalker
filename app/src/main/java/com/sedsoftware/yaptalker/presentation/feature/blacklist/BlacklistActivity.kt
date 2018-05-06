package com.sedsoftware.yaptalker.presentation.feature.blacklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.feature.blacklist.adapter.BlacklistAdapter
import com.sedsoftware.yaptalker.presentation.model.base.BlacklistedTopicModel
import kotlinx.android.synthetic.main.include_main_appbar.toolbar
import javax.inject.Inject

@LayoutResource(R.layout.activity_blacklist)
class BlacklistActivity : BaseActivity(), BlacklistView {

  companion object {
    fun getIntent(ctx: Context): Intent =
      Intent(ctx, BlacklistActivity::class.java)
  }

  @Inject
  lateinit var adapter: BlacklistAdapter

  @Inject
  lateinit var messagesDelegate: MessagesDelegate

  @Inject
  @InjectPresenter
  lateinit var presenter: BlacklistPresenter

  @ProvidePresenter
  fun providePresenter() = presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun appendBlacklistItem(topic: BlacklistedTopicModel) {
    adapter.addBlaclistItem(topic)
  }

  override fun showDeleteConfirmationDialog(topicId: Int) {
    MaterialDialog.Builder(this)
      .content(R.string.msg_blacklist_request_deletion)
      .positiveText(R.string.msg_blacklist_confirm_delete)
      .negativeText(R.string.msg_blacklist_confirm_no)
      .onPositive { _, _ -> presenter.deleteTopicFromBlacklist(topicId) }
      .show()
  }

  override fun showErrorMessage(message: String) {
    messagesDelegate.showMessageError(message)
  }

  override fun updateCurrentUiState() {
    supportActionBar?.title = string(R.string.title_blacklist)
  }
}
