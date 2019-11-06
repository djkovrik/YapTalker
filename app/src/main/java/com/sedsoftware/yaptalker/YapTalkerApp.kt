package com.sedsoftware.yaptalker

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.widget.ImageView
import com.facebook.stetho.Stetho
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.sedsoftware.yaptalker.common.CrashReportingTree
import com.sedsoftware.yaptalker.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.spans.SpannableTheme
import timber.log.Timber
import java.security.MessageDigest
import javax.inject.Inject

@Suppress("ConstantConditionIf")
class YapTalkerApp : Application(), HasActivityInjector {

    companion object {
        private const val NAV_DRAWER_AVATAR_PADDING = 16
        private const val YAP_API_KEY = "JanW23Sh"

        private lateinit var appContext: Context

        fun getMd5(): String =
            md5(String.format("%s:%s", YAP_API_KEY, getUdid()))

        fun getUdid(): String =
            Settings.System.getString(appContext.contentResolver, "android_id")

        fun getAppVersion(): String = "0.998"

        private fun md5(str: String): String {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(str.toByteArray())
            val messageDigest = digest.digest()
            val hexString = StringBuffer()

            for (i in 0 until messageDigest.size) {
                var hex = Integer.toHexString(0xFF and messageDigest[i].toInt())
                while (hex.length < 2)
                    hex = "0$hex"
                hexString.append(hex)
            }
            return hexString.toString()
        }
    }



    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        appContext = this

        LeakCanary.install(this)

        DaggerAppComponent.builder().create(this).inject(this)

        initStetho()
        initTimber()
        initMaterialDrawerImageLoader()
        initMarkwon()
        dumpAppInfo()
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initTimber() {
        Timber.uprootAll()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    private fun initMaterialDrawerImageLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView?, uri: Uri?, placeholder: Drawable?, tag: String?) {
                Picasso.with(imageView?.context).load(uri).placeholder(placeholder).into(imageView)
            }

            override fun cancel(imageView: ImageView?) {
                Picasso.with(imageView?.context).cancelRequest(imageView)
            }

            override fun placeholder(ctx: Context, tag: String?): Drawable =
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

    private fun initMarkwon() {
        val theme = SpannableTheme.builderWithDefaults(this)
            .codeTextColor(Color.parseColor("#C0341D"))
            .codeBackgroundColor(Color.parseColor("#FCEDEA"))
            .build()
        SpannableConfiguration.builder(this)
            .theme(theme)
            .build()
    }

    private fun dumpAppInfo() {
        if (BuildConfig.DEBUG) {
            packageManager.getPackageInfo(packageName, 0).let { packageInfo ->
                Timber.d("Init YapTalker:")
                Timber.d("--- version name: ${packageInfo.versionName}")
            }
        }
    }
}
