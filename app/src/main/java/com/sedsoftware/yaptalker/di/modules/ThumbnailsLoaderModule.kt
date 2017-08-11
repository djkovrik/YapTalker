package com.sedsoftware.yaptalker.di.modules

import com.sedsoftware.yaptalker.data.remote.thumbnails.CoubThumbnailLoader
import com.sedsoftware.yaptalker.data.remote.thumbnails.RutubeThumbnailLoader
import com.sedsoftware.yaptalker.data.remote.thumbnails.ThumbnailsLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(RequestsModule::class))
class ThumbnailsLoaderModule {

  @Provides
  @Singleton
  fun provideThumbnailsLoader(rutube: RutubeThumbnailLoader, coub: CoubThumbnailLoader) =
      ThumbnailsLoader(rutube, coub)
}