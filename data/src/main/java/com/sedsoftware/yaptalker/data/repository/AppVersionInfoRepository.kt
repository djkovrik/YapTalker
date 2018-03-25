package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.BuildConfig
import com.sedsoftware.yaptalker.data.mappers.AppVersionInfoMapper
import com.sedsoftware.yaptalker.data.network.external.AppUpdatesChecker
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.VersionInfoRepository
import io.reactivex.Single
import javax.inject.Inject

class AppVersionInfoRepository @Inject constructor(
  private val dataLoader: AppUpdatesChecker,
  private val dataMapper: AppVersionInfoMapper
) : VersionInfoRepository {

  override fun getRemoteVersionInfo(): Single<BaseEntity> =
      dataLoader
        .loadCurrentVersionInfo()
        .map(dataMapper)

  override fun getInstalledVersionInfo(): Single<String> =
    Single.just(BuildConfig.VERSION_NAME)
}
