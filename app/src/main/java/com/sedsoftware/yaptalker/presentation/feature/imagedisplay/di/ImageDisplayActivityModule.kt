package com.sedsoftware.yaptalker.presentation.feature.imagedisplay.di

import com.sedsoftware.yaptalker.device.sharing.YapSharingHelper
import com.sedsoftware.yaptalker.device.storage.YapImageStorage
import com.sedsoftware.yaptalker.di.scope.ActivityScope
import com.sedsoftware.yaptalker.domain.device.ImageStorage
import com.sedsoftware.yaptalker.domain.device.SharingHelper
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import com.sedsoftware.yaptalker.presentation.feature.imagedisplay.ImageDisplayActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module
abstract class ImageDisplayActivityModule {

  @Module
  companion object {
    @ActivityScope
    @Provides
    @JvmStatic
    fun provideMessagesDelegate(activity: ImageDisplayActivity): MessagesDelegate =
      MessagesDelegate(WeakReference(activity))
  }

  @ActivityScope
  @Binds
  abstract fun imageSharingHelper(helper: YapSharingHelper): SharingHelper

  @ActivityScope
  @Binds
  abstract fun imageStorage(storage: YapImageStorage): ImageStorage
}
