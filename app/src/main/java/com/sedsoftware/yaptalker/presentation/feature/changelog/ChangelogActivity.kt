package com.sedsoftware.yaptalker.presentation.feature.changelog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import kotlinx.android.synthetic.main.activity_changelog.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import ru.noties.markwon.Markwon
import javax.inject.Inject

@LayoutResource(R.layout.activity_changelog)
class ChangelogActivity : BaseActivity(), ChangelogView {

    companion object {
        fun getIntent(ctx: Context): Intent = Intent(ctx, ChangelogActivity::class.java)
    }

    @Inject
    lateinit var messagesDelegate: MessagesDelegate

    @Inject
    @InjectPresenter
    lateinit var presenter: ChangelogPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun displayChangelog(markdown: String) {
        Markwon.setMarkdown(changelog, markdown)
    }

    override fun showLoadingIndicator() {
        changelog_container.isGone = true
        changelog_progressbar.isVisible = true
    }

    override fun hideLoadingIndicator() {
        changelog_progressbar.isGone = true
        changelog_container.isVisible = true
    }

    override fun showErrorMessage(message: String) {
        messagesDelegate.showMessageError(message)
    }
}
