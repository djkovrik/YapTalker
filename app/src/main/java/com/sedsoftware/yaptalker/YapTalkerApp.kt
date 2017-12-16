package com.sedsoftware.yaptalker

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.sedsoftware.yaptalker.di.DaggerAppComponent
import com.sedsoftware.yaptalker.presentation.commons.extensions.color
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import es.dmoral.toasty.Toasty
import timber.log.Timber
import javax.inject.Inject

class YapTalkerApp : Application(), HasActivityInjector {

  companion object {
    private const val NAV_DRAWER_AVATAR_PADDING = 16
  }

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  override fun onCreate() {
    super.onCreate()

    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    LeakCanary.install(this)

    DaggerAppComponent
        .builder()
        .context(this)
        .build()
        .inject(this)

    initTimber()
    initToasty()
    initMaterialDrawerImageLoader()
  }

  override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

  @Suppress("ConstantConditionIf")
  private fun initTimber() {
    Timber.uprootAll()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
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
                  .colorRes(R.color.colorGuestProfile)
                  .backgroundColorRes(R.color.colorGuestProfileBackground)
                  .paddingDp(NAV_DRAWER_AVATAR_PADDING)

            else -> super.placeholder(ctx, tag)
          }
    })
  }
}
