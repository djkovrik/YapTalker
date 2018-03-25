package com.sedsoftware.yaptalker.domain.interactor.updater

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.interactor.SingleUseCase
import com.sedsoftware.yaptalker.domain.repository.VersionInfoRepository
import io.reactivex.Single
import javax.inject.Inject

class GetRemoteVersionInfo @Inject constructor(
  private val versionInfoRepository: VersionInfoRepository
) : SingleUseCase<BaseEntity> {

  override fun execute(): Single<BaseEntity> =
    versionInfoRepository
      .getRemoteVersionInfo()
}
