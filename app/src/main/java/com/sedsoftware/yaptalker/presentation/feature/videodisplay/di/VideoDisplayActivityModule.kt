package com.sedsoftware.yaptalker.presentation.feature.videodisplay.di

import com.sedsoftware.yaptalker.di.scope.ActivityScope
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import com.sedsoftware.yaptalker.presentation.feature.videodisplay.VideoDisplayActivity
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module
class VideoDisplayActivityModule {

  @ActivityScope
  @Provides
  fun provideMessagesDelegate(activity: VideoDisplayActivity): MessagesDelegate =
    MessagesDelegate(WeakReference(activity))
}
