package com.sedsoftware.yaptalker.presentation.feature.updater

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.model.base.AppVersionInfoModel
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_updater.updater_btn_changelog
import kotlinx.android.synthetic.main.fragment_updater.updater_btn_check_updates
import kotlinx.android.synthetic.main.fragment_updater.updater_btn_download
import kotlinx.android.synthetic.main.fragment_updater.updater_current_version
import kotlinx.android.synthetic.main.fragment_updater.updater_last_update_check
import kotlinx.android.synthetic.main.fragment_updater.updater_new_version
import kotlinx.android.synthetic.main.fragment_updater.updater_progressbar_status
import kotlinx.android.synthetic.main.fragment_updater.updater_title
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
        setCurrentAppbarTitle(string(R.string.nav_drawer_updates))
        setCurrentNavDrawerItem(NavigationSection.APP_UPDATES)
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

        updater_new_version.isVisible = true
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
            updater_btn_download.isVisible = true
        } else {
            updater_btn_download.isGone = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_WRITE_PERMISSION -> {
                presenter.downloadNewVersion()
            }
        }
    }

    private fun subscribeViews() {

        RxView.clicks(updater_btn_check_updates)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({
                presenter.checkForUpdates()
            }, { e: Throwable ->
                e.message?.let { showErrorMessage(it) }
            })

        RxView.clicks(updater_btn_changelog)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({
                presenter.showChangelog()
            }, { e: Throwable ->
                e.message?.let { showErrorMessage(it) }
            })

        RxView.clicks(updater_btn_download)
            .autoDisposable(event(FragmentLifecycle.DESTROY))
            .subscribe({
                checkPermissionForDownloading()
            }, { e: Throwable ->
                e.message?.let { showErrorMessage(it) }
            })
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
