package com.sedsoftware.yaptalker.presentation.features.changelog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.showView
import com.sedsoftware.yaptalker.presentation.extensions.snackError
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
  @InjectPresenter
  lateinit var presenter: ChangelogPresenter

  @ProvidePresenter
  fun providePresenter() = presenter

  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        NavUtils.navigateUpFromSameTask(this)
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun displayChangelog(markdown: String) {
    Markwon.setMarkdown(changelog, markdown)
  }

  override fun showLoadingIndicator() {
    changelog_container.hideView()
    changelog_progressbar.showView()
  }

  override fun hideLoadingIndicator() {
    changelog_progressbar.hideView()
    changelog_container.showView()
  }

  override fun showErrorMessage(message: String) {
    snackError(message)
  }
}
