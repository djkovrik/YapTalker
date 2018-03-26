package com.sedsoftware.yaptalker.presentation.features.updater

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.domain.interactor.updater.GetInstalledVersionInfo
import com.sedsoftware.yaptalker.domain.interactor.updater.GetRemoteVersionInfo
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.mappers.VersionInfoMapper
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class UpdaterPresenter @Inject constructor(
  private val router: Router,
  private val installedVersionUseCase: GetInstalledVersionInfo,
  private val remoteVersionUseCase: GetRemoteVersionInfo,
  private val versionInfoMapper: VersionInfoMapper
) : BasePresenter<UpdaterView>() {

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    fetchCurrentVersionInfo()
  }

  override fun attachView(view: UpdaterView?) {
    super.attachView(view)
    viewState.updateCurrentUiState()
  }

  private fun fetchCurrentVersionInfo() {
    installedVersionUseCase
      .execute()
      .map(versionInfoMapper)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(event(PresenterLifecycle.DESTROY))
      .subscribe({ info: YapEntity ->
        viewState.displayInstalledVersionInfo(info)
      }, { throwable: Throwable? ->
        throwable?.message?.let { viewState.showErrorMessage(it) }
      })
  }
}
