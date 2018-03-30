package com.sedsoftware.yaptalker.presentation.features.updater

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.showView
import com.sedsoftware.yaptalker.presentation.extensions.snackError
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_updater.updater_btn_changelog
import kotlinx.android.synthetic.main.fragment_updater.updater_btn_check_updates
import kotlinx.android.synthetic.main.fragment_updater.updater_btn_download
import kotlinx.android.synthetic.main.fragment_updater.updater_current_version
import kotlinx.android.synthetic.main.fragment_updater.updater_last_update_check_label
import kotlinx.android.synthetic.main.fragment_updater.updater_new_version
import kotlinx.android.synthetic.main.fragment_updater.updater_progressbar
import kotlinx.android.synthetic.main.fragment_updater.updater_progressbar_status
import kotlinx.android.synthetic.main.fragment_updater.updater_title
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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    subscribeViews()
  }

  override fun showErrorMessage(message: String) {
    snackError(message)
  }

  override fun showLoadingIndicator() {
    updater_progressbar.showView()
  }

  override fun hideLoadingIndicator() {
    updater_progressbar.hideView()
  }

  override fun updateCurrentUiState() {
    context?.stringRes(R.string.nav_drawer_updates)?.let { presenter.setAppbarTitle(it) }
    presenter.setNavDrawerItem(NavigationSection.APP_UPDATES)
  }

  override fun showUpdateAvailableLabel() {
    updater_title.text = context?.stringRes(R.string.updater_title_update_available)
  }

  override fun showNoUpdateAvailableLabel() {
    updater_title.text = context?.stringRes(R.string.updater_title_no_updates)
  }

  override fun displayInstalledVersionInfo(versionInfo: AppVersionInfoModel) {
    updater_current_version.text = versionInfo.versionName
  }

  override fun displayLastUpdateCheckDate(versionInfo: AppVersionInfoModel) {
    updater_last_update_check_label.text = versionInfo.lastUpdateCheckDate
  }

  override fun displayRemoteVersionInfo(versionInfo: AppVersionInfoModel) {
    updater_new_version.text = versionInfo.versionName
  }

  override fun showCheckingStatus() {
    updater_progressbar_status.text = context?.stringRes(R.string.updater_status_checking)
  }

  override fun showUpdateCompletedStatus() {
    updater_progressbar_status.text = context?.stringRes(R.string.updater_status_checked)
  }

  override fun showUpdateErrorStatus() {
    updater_progressbar_status.text = context?.stringRes(R.string.updater_status_checking)
  }

  override fun showEmptyUpdateStatus() {
    updater_progressbar_status.text = ""
  }

  override fun setUpdateButtonAvailability(isAvailable: Boolean) {
    updater_btn_check_updates.isEnabled = isAvailable
  }

  override fun setDownloadButtonVisibility(isVisible: Boolean) {
    if (isVisible) {
      updater_btn_check_updates.hideView()
      updater_btn_download.showView()
    } else {
      updater_btn_download.hideView()
      updater_btn_check_updates.showView()
    }
  }

  private fun subscribeViews() {

    RxView
      .clicks(updater_btn_check_updates)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { presenter.checkForUpdates() }

    RxView
      .clicks(updater_btn_changelog)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { presenter.showChangelog() }
  }
}
