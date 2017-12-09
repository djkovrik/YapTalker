package com.sedsoftware.yaptalker.di.modules

import com.sedsoftware.yaptalker.domain.executor.ExecutionThread
import com.sedsoftware.yaptalker.domain.executor.PostExecutionThread
import com.sedsoftware.yaptalker.presentation.executor.ComputationThread
import com.sedsoftware.yaptalker.presentation.executor.IOThread
import com.sedsoftware.yaptalker.presentation.executor.UIThread
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class SchedulersModule {

  @Singleton
  @Provides
  @Named("io")
  fun provideExecutionThreadIO(): ExecutionThread = IOThread()

  @Singleton
  @Provides
  @Named("computation")
  fun provideExecutionThreadComputation(): ExecutionThread = ComputationThread()

  @Singleton
  @Provides
  @Named("ui")
  fun providePostExecutionThread(): PostExecutionThread = UIThread()
}
