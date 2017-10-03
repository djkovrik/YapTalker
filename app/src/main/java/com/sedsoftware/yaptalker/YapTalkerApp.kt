package com.sedsoftware.yaptalker

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.facebook.stetho.Stetho
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.lazy
import com.github.salomonbrys.kodein.singleton
import com.jakewharton.rxrelay2.BehaviorRelay
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.sedsoftware.yaptalker.commons.AppEvent
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.data.remote.requestsClientModule
import com.sedsoftware.yaptalker.data.remote.requestsCookieModule
import com.sedsoftware.yaptalker.data.remote.video.thumbnailsManagerModule
import com.sedsoftware.yaptalker.data.remote.yapDataManagerModule
import com.sedsoftware.yaptalker.features.settings.SettingsHelper
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import timber.log.Timber

class YapTalkerApp : Application(), KodeinAware {

  companion object {
    lateinit var kodeinInstance: Kodein
    private const val NAV_DRAWER_AVATAR_PADDING = 16
  }

  override val kodein by Kodein.lazy {

    // App context
    bind<Context>() with singleton { this@YapTalkerApp }

    // Global settings helper
    bind<SettingsHelper>() with singleton { SettingsHelper(this@YapTalkerApp) }

    // Global rx bus for app events
    bind<BehaviorRelay<AppEvent>>() with singleton { BehaviorRelay.createDefault(AppEvent()) }

    // Kodein modules
    import(requestsClientModule)
    import(requestsCookieModule)
    import(yapDataManagerModule)
    import(thumbnailsManagerModule)
  }

  override fun onCreate() {
    super.onCreate()

    kodeinInstance = kodein

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return
    }

    LeakCanary.install(this)

    Timber.uprootAll()

    if (BuildConfig.DEBUG) {
      LeakCanary.install(this)
      Stetho.initializeWithDefaults(this)
      Timber.plant(Timber.DebugTree())
    }

    // Toasty coloring
    Toasty.Config.getInstance()
        .setErrorColor(color(R.color.toastyColorError))
        .setInfoColor(color(R.color.toastyColorInfo))
        .setSuccessColor(color(R.color.toastyColorSuccess))
        .setWarningColor(color(R.color.toastyColorWarning))
        .apply()

    // Init MaterialDrawer image loader
    DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
      override fun set(imageView: ImageView?, uri: Uri?, placeholder: Drawable?, tag: String?) {
        Picasso.with(imageView?.context).load(uri).placeholder(placeholder).into(imageView)
      }

      override fun cancel(imageView: ImageView?) {
        Picasso.with(imageView?.context).cancelRequest(imageView)
      }

      override fun placeholder(ctx: Context?, tag: String?): Drawable {

        return when (tag) {
          DrawerImageLoader.Tags.PROFILE.name ->
            IconicsDrawable(ctx)
                .icon(CommunityMaterial.Icon.cmd_account)
                .colorRes(R.color.colorPrimary)
                .backgroundColorRes(R.color.material_color_white_70_percent)
                .paddingDp(NAV_DRAWER_AVATAR_PADDING)

          else -> super.placeholder(ctx, tag)
        }

      }
    })
  }
}
