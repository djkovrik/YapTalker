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
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.data.remote.YapRequestState
import com.sedsoftware.yaptalker.data.remote.remoteDataModule
import com.sedsoftware.yaptalker.data.remote.requestsModule
import com.sedsoftware.yaptalker.features.settings.settingsModule
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import timber.log.Timber

class YapTalkerApp : Application(), KodeinAware {

  companion object {
    lateinit var kodeinInstance: Kodein
  }

  override val kodein by Kodein.lazy {
    // Android auto module import
    //import(autoAndroidModule(this@YapTalkerApp))

    // App context
    bind<Context>() with singleton { this@YapTalkerApp }

    // Custom modules
    import(requestsModule)
    import(remoteDataModule)
    import(settingsModule)

    // Global rx bus for loading state
    bind<BehaviorRelay<Long>>() with singleton { BehaviorRelay.createDefault(YapRequestState.IDLE) }
    // Global rx bus for appbar title handling
    bind<BehaviorRelay<String>>() with singleton { BehaviorRelay.createDefault("YapTalker") }
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

      override fun placeholder(ctx: Context?, tag: String?) = DrawerUIUtils.getPlaceHolder(ctx)
    })
  }
}
