package com.sedsoftware.yaptalker.di.module.contribution

import com.sedsoftware.yaptalker.di.scope.ActivityScope
import com.sedsoftware.yaptalker.presentation.feature.blacklist.BlacklistActivity
import com.sedsoftware.yaptalker.presentation.feature.blacklist.di.BlacklistActivityModule
import com.sedsoftware.yaptalker.presentation.feature.changelog.ChangelogActivity
import com.sedsoftware.yaptalker.presentation.feature.changelog.di.ChangelogActivityModule
import com.sedsoftware.yaptalker.presentation.feature.gallery.TopicGalleryActivity
import com.sedsoftware.yaptalker.presentation.feature.gallery.di.TopicGalleryActivityModule
import com.sedsoftware.yaptalker.presentation.feature.imagedisplay.ImageDisplayActivity
import com.sedsoftware.yaptalker.presentation.feature.imagedisplay.di.ImageDisplayActivityModule
import com.sedsoftware.yaptalker.presentation.feature.mail.MailActivity
import com.sedsoftware.yaptalker.presentation.feature.mail.di.MailActivityModule
import com.sedsoftware.yaptalker.presentation.feature.navigation.MainActivity
import com.sedsoftware.yaptalker.presentation.feature.navigation.di.MainActivityModule
import com.sedsoftware.yaptalker.presentation.feature.settings.SettingsActivity
import com.sedsoftware.yaptalker.presentation.feature.settings.di.SettingsActivityModule
import com.sedsoftware.yaptalker.presentation.feature.videodisplay.VideoDisplayActivity
import com.sedsoftware.yaptalker.presentation.feature.videodisplay.di.VideoDisplayActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [(AndroidSupportInjectionModule::class)])
interface ActivityContributionModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    fun mainActivityInjector(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(SettingsActivityModule::class)])
    fun settingsActivityInjector(): SettingsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(ImageDisplayActivityModule::class)])
    fun imageActivityInjector(): ImageDisplayActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(VideoDisplayActivityModule::class)])
    fun videoActivityInjector(): VideoDisplayActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(TopicGalleryActivityModule::class)])
    fun galleryActivityInjector(): TopicGalleryActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(ChangelogActivityModule::class)])
    fun changelogActivityInjector(): ChangelogActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(BlacklistActivityModule::class)])
    fun blacklistActivityInjector(): BlacklistActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(MailActivityModule::class)])
    fun mailActivityInjector(): MailActivity
}
