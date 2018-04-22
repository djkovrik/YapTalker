package com.sedsoftware.yaptalker.presentation.feature.updater

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.showView
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_updater.*
import java.util.Locale
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_updater)
class UpdaterFragment : BaseFragment(), UpdaterView {

  companion object {
    fun getNewInstance(): UpdaterFragment = UpdaterFragment()

    private const val STORAGE_WRITE_PERMISSION = 0
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
    messagesDelegate.showMessageError(message)
  }

  override fun updateCurrentUiState() {
//    context?.string(R.string.nav_drawer_updates)?.let { presenter.setAppbarTitle(it) }
//    presenter.setNavDrawerItem(NavigationSection.APP_UPDATES)
  }

  override fun showUpdateAvailableLabel() {
    updater_title.text = context?.string(R.string.updater_title_update_available)
  }

  override fun showNoUpdateAvailableLabel() {
    updater_title.text = context?.string(R.string.updater_title_no_updates)
  }

  override fun displayInstalledVersionInfo(versionInfo: AppVersionInfoModel) {
    updater_current_version.text = String.format(
      Locale.getDefault(),
      "%s %s",
      context?.string(R.string.updater_info_installed_version),
      versionInfo.versionName
    )
  }

  override fun displayRemoteVersionInfo(versionInfo: AppVersionInfoModel) {

    updater_new_version.text = String.format(
      Locale.getDefault(),
      "%s %s",
      context?.string(R.string.updater_info_latest_version),
      versionInfo.versionName
    )

    updater_new_version.showView()
  }

  override fun displayLastUpdateCheckDate(date: String) {
    updater_last_update_check.text = date
  }

  override fun showCheckingStatus() {
    updater_progressbar_status.text = context?.string(R.string.updater_status_checking)
  }

  override fun showUpdateCompletedStatus() {
    updater_progressbar_status.text = context?.string(R.string.updater_status_checked)
  }

  override fun showUpdateErrorStatus() {
    updater_progressbar_status.text = context?.string(R.string.updater_status_checking)
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

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    when (requestCode) {
      STORAGE_WRITE_PERMISSION -> {
        presenter.downloadNewVersion()
      }
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

    RxView
      .clicks(updater_btn_download)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { checkPermissionForDownloading() }
  }

  private fun checkPermissionForDownloading() {
    if (context?.let { ctx ->
        ContextCompat.checkSelfPermission(
          ctx,
          Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
      } != PackageManager.PERMISSION_GRANTED) {

      requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_WRITE_PERMISSION)
    } else {
      presenter.downloadNewVersion()
    }
  }
}
