package com.sedsoftware.yaptalker.presentation.features.changelog

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.extensions.snackError
import kotlinx.android.synthetic.main.fragment_changelog.*
import ru.noties.markwon.Markwon
import javax.inject.Inject

@LayoutResource(R.layout.fragment_changelog)
class ChangelogFragment : BaseFragment(), ChangelogView {

  companion object {
    fun getNewInstance() = ChangelogFragment()
  }

  @Inject
  @InjectPresenter
  lateinit var presenter: ChangelogPresenter

  @ProvidePresenter
  fun providePresenter() = presenter

  override fun displayChangelog(markdown: String) {
    Markwon.setMarkdown(changelog, markdown)
  }

  override fun showErrorMessage(message: String) {
    snackError(message)
  }
}
