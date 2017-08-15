package com.sedsoftware.yaptalker.di.modules

import com.sedsoftware.yaptalker.data.remote.thumbnails.CoubThumbnailLoader
import com.sedsoftware.yaptalker.data.remote.thumbnails.RutubeThumbnailLoader
import com.sedsoftware.yaptalker.data.remote.yap.YapLoader
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = arrayOf(RetrofitModule::class))
class RequestsModule {

  @Provides
  @Singleton
  fun provideYapLoader(
      @Named("YapLoader") retrofit: Retrofit): YapLoader {
    return retrofit.create(YapLoader::class.java)
  }

  @Provides
  @Singleton
  fun provideRutubeThumbnailLoader(
      @Named("RutubeThumbnailLoader") retrofit: Retrofit): RutubeThumbnailLoader {
    return retrofit.create(RutubeThumbnailLoader::class.java)
  }

  @Provides
  @Singleton
  fun provideCoubThumbnailLoader(
      @Named("CoubThumbnailLoader") retrofit: Retrofit): CoubThumbnailLoader {
    return retrofit.create(CoubThumbnailLoader::class.java)
  }
}
