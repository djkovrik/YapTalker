package com.sedsoftware.yaptalker

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.lazy
import com.github.salomonbrys.kodein.singleton
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.sedsoftware.yaptalker.commons.CrashReportingTree
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.data.requests.requestsClientModule
import com.sedsoftware.yaptalker.data.requests.requestsCookieModule
import com.sedsoftware.yaptalker.data.thumbnailsManagerModule
import com.sedsoftware.yaptalker.data.yapDataManagerModule
import com.sedsoftware.yaptalker.features.navigationModule
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

    bind<Context>() with singleton { this@YapTalkerApp }

    bind<SettingsHelper>() with singleton { SettingsHelper(this@YapTalkerApp) }

    import(rxModule)
    import(navigationModule)
    import(requestsClientModule)
    import(requestsCookieModule)
    import(yapDataManagerModule)
    import(thumbnailsManagerModule)
  }

  override fun onCreate() {
    super.onCreate()

    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return
    }

    LeakCanary.install(this)

    // Normal app init code below
    kodeinInstance = kodein

    initTimber()
    initToasty()
    initMaterialDrawerImageLoader()
  }

  @Suppress("ConstantConditionIf")
  private fun initTimber() {
    Timber.uprootAll()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    } else {
      Timber.plant(CrashReportingTree())
    }
  }

  private fun initToasty() {
    Toasty.Config.getInstance()
        .setErrorColor(color(R.color.toastyColorError))
        .setInfoColor(color(R.color.toastyColorInfo))
        .setSuccessColor(color(R.color.toastyColorSuccess))
        .setWarningColor(color(R.color.toastyColorWarning))
        .apply()
  }

  private fun initMaterialDrawerImageLoader() {
    DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
      override fun set(imageView: ImageView?, uri: Uri?, placeholder: Drawable?, tag: String?) {
        Picasso.with(imageView?.context).load(uri).placeholder(placeholder).into(imageView)
      }

      override fun cancel(imageView: ImageView?) {
        Picasso.with(imageView?.context).cancelRequest(imageView)
      }

      override fun placeholder(ctx: Context?, tag: String?): Drawable =
          when (tag) {
            DrawerImageLoader.Tags.PROFILE.name ->
              IconicsDrawable(ctx)
                  .icon(CommunityMaterial.Icon.cmd_account)
                  .colorRes(R.color.colorPrimary)
                  .backgroundColorRes(R.color.material_color_white_70_percent)
                  .paddingDp(NAV_DRAWER_AVATAR_PADDING)

            else -> super.placeholder(ctx, tag)
      }
    })
  }
}
