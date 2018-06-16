package com.sedsoftware.yaptalker.presentation.feature.mail.di

import com.sedsoftware.yaptalker.di.scope.ActivityScope
import com.sedsoftware.yaptalker.presentation.delegate.MessagesDelegate
import com.sedsoftware.yaptalker.presentation.feature.mail.MailActivity
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module
abstract class MailActivityModule {
  @Module
  companion object {
    @ActivityScope
    @Provides
    @JvmStatic
    fun provideMessagesDelegate(activity: MailActivity): MessagesDelegate =
      MessagesDelegate(WeakReference(activity))
  }
}
