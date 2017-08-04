package com.sedsoftware.yaptalker

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.facebook.stetho.Stetho
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.sedsoftware.yaptalker.di.ApplicationComponent
import com.sedsoftware.yaptalker.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import timber.log.Timber

class YapTalkerApp : Application() {

  companion object {
    lateinit var appComponent: ApplicationComponent
  }

  override fun onCreate() {
    super.onCreate()

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
    } else {
      // TODO() Init Timber with Firebase Crash Reporting tree here
    }

    appComponent = DaggerApplicationComponent.builder().build()

    // Init MaterialDrawer image loader


    DrawerImageLoader.init(object: AbstractDrawerImageLoader() {
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