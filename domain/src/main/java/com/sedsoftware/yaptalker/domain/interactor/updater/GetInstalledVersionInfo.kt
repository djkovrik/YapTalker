package com.sedsoftware.yaptalker.domain.interactor.updater

import com.sedsoftware.yaptalker.domain.interactor.SingleUseCase
import com.sedsoftware.yaptalker.domain.repository.VersionInfoRepository
import io.reactivex.Single
import javax.inject.Inject

class GetInstalledVersionInfo @Inject constructor(
  private val versionInfoRepository: VersionInfoRepository
) : SingleUseCase<String> {

  override fun execute(): Single<String> =
    versionInfoRepository
      .getInstalledVersionInfo()
}
