package com.sedsoftware.yaptalker.presentation.features.updater

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.extensions.snackError
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_updater)
class UpdaterFragment : BaseFragment(), UpdaterView {

  companion object {
    fun getNewInstance(): UpdaterFragment = UpdaterFragment()
  }

  @Inject
  @InjectPresenter
  lateinit var presenter: UpdaterPresenter

  @ProvidePresenter
  fun providePresenter() = presenter

  override fun showErrorMessage(message: String) {
    snackError(message)
  }

  override fun showLoadingIndicator() {

  }

  override fun hideLoadingIndicator() {

  }

  override fun updateCurrentUiState(forumTitle: String) {

  }

  override fun showUpdateAvailableLabel() {

  }

  override fun showNoUpdateAvailableLabel() {

  }

  override fun displayInstalledVersionInfo(version: String) {

  }

  override fun displayRemoteVersionInfo(version: String) {

  }

  override fun displayLastUpdateCheckDate(date: String) {

  }

  override fun showUpdatingStatus() {

  }

  override fun showUpdateCompletedStatus() {

  }

  override fun showUpdateErrorStatus() {

  }

  override fun showEmptyUpdateStatus() {

  }
}
