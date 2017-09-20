package com.sedsoftware.yaptalker.data.remote

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton

val remoteDataModule = Kodein.Module {

  bind<YapDataManager>() with singleton {
    YapDataManager(instance("YapLoader"), instance())
  }

  bind<ThumbnailsManager>() with singleton {
    ThumbnailsManager(
        instance("CoubLoader"),
        instance("RutubeLoader"),
        instance("YapVideoLoader"),
        instance("VkLoader"))
  }
}
