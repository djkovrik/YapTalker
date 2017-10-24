package com.sedsoftware.yaptalker.base

import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.data.remote.video.ThumbnailsManager
import com.sedsoftware.yaptalker.features.settings.SettingsHelper

abstract class BaseAdapterInjections : LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injections
  protected val thumbnailsLoader: ThumbnailsManager by instance()
  private val settings: SettingsHelper by instance()

  protected val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  protected val bigFontSize by lazy {
    settings.getBigFontSize()
  }

  protected val smallFontSize by lazy {
    settings.getSmallFontSize()
  }
}