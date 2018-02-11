package com.sedsoftware.yaptalker.presentation.features.imagedisplay.di

import com.sedsoftware.yaptalker.device.sharing.YapSharingHelper
import com.sedsoftware.yaptalker.device.storage.YapImageStorage
import com.sedsoftware.yaptalker.di.scopes.ActivityScope
import com.sedsoftware.yaptalker.domain.device.ImageStorage
import com.sedsoftware.yaptalker.domain.device.SharingHelper
import dagger.Binds
import dagger.Module

@Module
abstract class ImageDisplayActivityModule {

  @ActivityScope
  @Binds
  abstract fun imageSharingHelper(helper: YapSharingHelper): SharingHelper

  @ActivityScope
  @Binds
  abstract fun imageStorage(storage: YapImageStorage): ImageStorage
}
