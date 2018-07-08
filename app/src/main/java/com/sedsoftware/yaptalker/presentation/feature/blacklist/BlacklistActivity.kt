package com.sedsoftware.yaptalker.presentation.feature.blacklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_blacklist.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import javax.inject.Inject

@LayoutResource(R.layout.activity_blacklist)
class BlacklistActivity : BaseActivity(), BlacklistView {

    companion object {
        fun getIntent(ctx: Context): Intent =
            Intent(ctx, BlacklistActivity::class.java)
    }

    @Inject
    lateinit var blacklistAdapter: BlacklistAdapter

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

        with(blacklisted_topics) {
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            adapter = blacklistAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_blacklist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_delete_all -> {
                presenter.clearBlacklist()
                true
            }
            R.id.action_delete_month -> {
                presenter.clearBlacklistMonthOld()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun showBlacklistedTopics(topics: List<BlacklistedTopicModel>) {
        blacklistAdapter.setTopics(topics)
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
