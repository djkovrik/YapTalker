package com.sedsoftware.yaptalker.di.modules

import com.sedsoftware.yaptalker.data.remote.yap.YapChosenForumLoader
import com.sedsoftware.yaptalker.data.remote.yap.YapChosenTopicLoader
import com.sedsoftware.yaptalker.data.remote.yap.YapForumsListLoader
import com.sedsoftware.yaptalker.data.remote.yap.YapNewsLoader
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = arrayOf(RetrofitModule::class))
class RequestsModule {

  @Provides
  @Singleton
  fun provideNewsLoader(@Named("NewsLoader") retrofit: Retrofit): YapNewsLoader {
    return retrofit.create(YapNewsLoader::class.java)
  }

  @Provides
  @Singleton
  fun provideForumsListLoader(@Named("ForumsListLoader") retrofit: Retrofit): YapForumsListLoader {
    return retrofit.create(YapForumsListLoader::class.java)
  }

  @Provides
  @Singleton
  fun provideChosenForumLoader(@Named("ChosenForumLoader") retrofit: Retrofit): YapChosenForumLoader {
    return retrofit.create(YapChosenForumLoader::class.java)
  }

  @Provides
  @Singleton
  fun provideChosenTopicLoader(@Named("ChosenTopicLoader") retrofit: Retrofit): YapChosenTopicLoader {
    return retrofit.create(YapChosenTopicLoader::class.java)
  }
}