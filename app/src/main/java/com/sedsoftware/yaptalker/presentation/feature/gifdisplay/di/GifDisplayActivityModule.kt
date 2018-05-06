package com.sedsoftware.yaptalker.presentation.feature.gifdisplay.di

import com.sedsoftware.yaptalker.di.scope.ActivityScope
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import com.sedsoftware.yaptalker.presentation.feature.gifdisplay.GifDisplayActivity
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module
class GifDisplayActivityModule {

  @ActivityScope
  @Provides
  fun provideMessagesDelegate(activity: GifDisplayActivity): MessagesDelegate =
    MessagesDelegate(WeakReference(activity))
}
