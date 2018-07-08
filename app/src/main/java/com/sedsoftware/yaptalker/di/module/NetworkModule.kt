package com.sedsoftware.yaptalker.di.module

import com.sedsoftware.yaptalker.common.converter.HashSearchConverterFactory
import com.sedsoftware.yaptalker.data.network.external.AppUpdatesChecker
import com.sedsoftware.yaptalker.data.network.external.GitHubLoader
import com.sedsoftware.yaptalker.data.network.site.YapIncubatorLoader
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.network.site.YapLoaderAlpha
import com.sedsoftware.yaptalker.data.network.site.YapSearchIdLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.CoubLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.RutubeLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.VkLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.YapFileLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.YapVideoLoader
import com.sedsoftware.yaptalker.di.module.network.HttpClientsModule
import com.sedsoftware.yaptalker.domain.device.Settings
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [(HttpClientsModule::class)])
class NetworkModule {

    companion object {
        // Yap
        private const val SITE_BASE_URL = "http://www.yaplakal.com/"
        private const val SITE_BASE_URL_HTTPS = "https://www.yaplakal.com/"
        private const val SITE_ALPHA_URL = "http://alpha.yaplakal.com/"
        private const val SITE_ALPHA_URL_HTTPS = "https://alpha.yaplakal.com/"
        private const val SITE_INCUBATOR_BASE_URL = "http://inkubator.yaplakal.com/"
        private const val SITE_INCUBATOR_BASE_URL_HTTPS = "https://inkubator.yaplakal.com/"

        // Videos
        private const val COUB_BASE_URL = "https://coub.com/"
        private const val RUTUBE_BASE_URL = "https://rutube.ru/"
        private const val YAP_FILES_BASE_URL = "http://www.yapfiles.ru/"
        private const val YAP_API_BASE_URL = "http://api.yapfiles.ru/"
        private const val VK_API_BASE_URL = "https://api.vk.com/"

        // Misc
        private const val YAP_FILE_HASH_MARKER = "md5="
        private const val YAP_SEARCH_ID_HASH_MARKER = "searchid="

        // Github
        private const val GITHUB_BASE_URL = "https://raw.githubusercontent.com/"

        // Deploy
        private const val APP_DEPLOY_BASE_URL = "http://sedsoftware.com/"
    }

    @Singleton
    @Provides
    fun provideYapLoader(
        @Named("siteClient") okHttpClient: OkHttpClient,
        settings: Settings
    ): YapLoader {

        val endpoint = if (settings.isHttpsEnabled()) {
            SITE_BASE_URL_HTTPS
        } else {
            SITE_BASE_URL
        }

        return Retrofit
            .Builder()
            .baseUrl(endpoint)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JspoonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(YapLoader::class.java)
    }

    @Singleton
    @Provides
    fun provideYapLoaderAlpha(
        @Named("siteClient") okHttpClient: OkHttpClient,
        settings: Settings
    ): YapLoaderAlpha {

        val endpoint = if (settings.isHttpsEnabled()) {
            SITE_ALPHA_URL_HTTPS
        } else {
            SITE_ALPHA_URL
        }

        return Retrofit
            .Builder()
            .baseUrl(endpoint)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JspoonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(YapLoaderAlpha::class.java)
    }

    @Singleton
    @Provides
    fun provideYapIncubatorLoader(
        @Named("siteClient") okHttpClient: OkHttpClient,
        settings: Settings
    ): YapIncubatorLoader {

        val endpoint = if (settings.isHttpsEnabled()) {
            SITE_INCUBATOR_BASE_URL_HTTPS
        } else {
            SITE_INCUBATOR_BASE_URL
        }

        return Retrofit
            .Builder()
            .baseUrl(endpoint)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JspoonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(YapIncubatorLoader::class.java)
    }

    @Singleton
    @Provides
    fun provideYapSearchIdLoader(
        @Named("siteClient") okHttpClient: OkHttpClient
    ): YapSearchIdLoader =
        Retrofit
            .Builder()
            .baseUrl(SITE_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(HashSearchConverterFactory.create(YAP_SEARCH_ID_HASH_MARKER))
            .build()
            .create(YapSearchIdLoader::class.java)


    @Singleton
    @Provides
    fun provideCoubLoader(): CoubLoader =
        Retrofit
            .Builder()
            .baseUrl(COUB_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoubLoader::class.java)

    @Singleton
    @Provides
    fun provideRutubeLoader(): RutubeLoader =
        Retrofit
            .Builder()
            .baseUrl(RUTUBE_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RutubeLoader::class.java)

    @Singleton
    @Provides
    fun provideYapFileLoader(): YapFileLoader =
        Retrofit.Builder()
            .baseUrl(YAP_FILES_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(HashSearchConverterFactory.create(YAP_FILE_HASH_MARKER))
            .build()
            .create(YapFileLoader::class.java)

    @Singleton
    @Provides
    fun provideYapVideoLoader(): YapVideoLoader =
        Retrofit.Builder()
            .baseUrl(YAP_API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YapVideoLoader::class.java)

    @Singleton
    @Provides
    fun provideVkLoader(): VkLoader =
        Retrofit.Builder()
            .baseUrl(VK_API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VkLoader::class.java)

    @Singleton
    @Provides
    fun provideGithubLoader(): GitHubLoader =
        Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(GitHubLoader::class.java)

    @Singleton
    @Provides
    fun provideAppUpdatesLoader(): AppUpdatesChecker =
        Retrofit.Builder()
            .baseUrl(APP_DEPLOY_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppUpdatesChecker::class.java)
}
