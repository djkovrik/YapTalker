package com.sedsoftware.yaptalker.presentation.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.arellomobile.mvp.MvpAppCompatActivity
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.common.annotation.LayoutResourceTablets
import com.sedsoftware.yaptalker.common.exception.MissingAnnotationException
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.ActivityLifecycle
import com.sedsoftware.yaptalker.presentation.extensions.colorFromAttr
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.Maybe
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

abstract class BaseActivity : MvpAppCompatActivity(), HasAndroidInjector, CanHandleBackPressed {

    @Inject
    lateinit var settings: Settings

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    protected lateinit var backPressFragment: BaseFragment

    private val lifecycleRelay: BehaviorRelay<Long> = BehaviorRelay.create()

    protected open val edgeToEdgeEnabled: Boolean = true

    @get:AttrRes
    protected open val edgeToEdgeStatusBarColorAttr: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        applyTheme()
        applyEdgeToEdgeWindowFlags()
        super.onCreate(savedInstanceState)

        val clazz = this::class.java

        when {
            clazz.isAnnotationPresent(LayoutResourceTablets::class.java) -> {
                if (settings.isInTwoPaneMode()) {
                    setContentView(clazz.getAnnotation(LayoutResourceTablets::class.java).tabletsValue)
                } else {
                    setContentView(clazz.getAnnotation(LayoutResourceTablets::class.java).normalValue)
                }
            }

            clazz.isAnnotationPresent(LayoutResource::class.java) -> {
                setContentView(clazz.getAnnotation(LayoutResource::class.java).value)
            }
            else -> {
                throw MissingAnnotationException("$this must be annotated with specific LayoutResource annotation.")
            }
        }

        applyEdgeToEdgeInsets()
        lifecycleRelay.accept(ActivityLifecycle.CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRelay.accept(ActivityLifecycle.DESTROY)
    }

    override fun onStart() {
        super.onStart()
        lifecycleRelay.accept(ActivityLifecycle.START)
    }

    override fun onStop() {
        super.onStop()
        lifecycleRelay.accept(ActivityLifecycle.STOP)
    }

    override fun onResume() {
        super.onResume()
        lifecycleRelay.accept(ActivityLifecycle.RESUME)
    }

    override fun onPause() {
        super.onPause()
        lifecycleRelay.accept(ActivityLifecycle.PAUSE)
    }

    override fun androidInjector(): AndroidInjector<Any> = fragmentInjector

    override fun setSelectedFragment(fragment: BaseFragment) {
        backPressFragment = fragment
    }

    protected fun event(@ActivityLifecycle.Event event: Long): Maybe<*> =
        lifecycleRelay.filter { it == event }.firstElement()

    private fun applyTheme() {
        val dark = getString(R.string.pref_appearance_theme_value_dark)
        val darkBlack = getString(R.string.pref_appearance_theme_value_dark_black)
        val lightContrast = getString(R.string.pref_appearance_theme_value_light_contrast)
        val lightYap = getString(R.string.pref_appearance_theme_value_light_yap)
        val current = settings.getCurrentTheme()

        when (current) {
            dark -> setTheme(R.style.AppTheme_Dark)
            darkBlack -> setTheme(R.style.AppTheme_DarkBlack)
            lightContrast -> setTheme(R.style.AppTheme_LightContrast)
            lightYap -> setTheme(R.style.AppTheme_LightYap)
        }
    }

    @Suppress("DEPRECATION")
    private fun applyEdgeToEdgeWindowFlags() {
        if (!edgeToEdgeEnabled) {
            return
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = edgeToEdgeStatusBarColorAttr?.let(::colorFromAttr) ?: Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        WindowInsetsControllerCompat(window, window.decorView).run {
            isAppearanceLightStatusBars = edgeToEdgeStatusBarColorAttr == null && isLightTheme()
            isAppearanceLightNavigationBars = isLightTheme()
        }
    }

    private fun applyEdgeToEdgeInsets() {
        if (!edgeToEdgeEnabled) {
            return
        }

        val content = findViewById<View>(android.R.id.content)
        val contentGroup = content as? ViewGroup ?: return
        val root = contentGroup.getChildAt(0) ?: return
        val initialLeft = root.paddingLeft
        val initialTop = root.paddingTop
        val initialRight = root.paddingRight
        val initialBottom = root.paddingBottom
        val statusBarScrim = createStatusBarScrim(contentGroup)

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            statusBarScrim?.layoutParams = statusBarScrim.layoutParams.apply {
                height = systemBars.top
            }
            view.setPadding(
                initialLeft + systemBars.left,
                initialTop + systemBars.top,
                initialRight + systemBars.right,
                initialBottom + systemBars.bottom
            )
            insets
        }
        ViewCompat.requestApplyInsets(root)
    }

    private fun createStatusBarScrim(contentGroup: ViewGroup): View? {
        val colorAttr = edgeToEdgeStatusBarColorAttr ?: return null
        return View(this).apply {
            setBackgroundColor(colorFromAttr(colorAttr))
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                0
            )
            contentGroup.addView(this)
        }
    }

    private fun isLightTheme(): Boolean {
        val dark = getString(R.string.pref_appearance_theme_value_dark)
        val darkBlack = getString(R.string.pref_appearance_theme_value_dark_black)
        val current = settings.getCurrentTheme()

        return current != dark && current != darkBlack
    }
}
