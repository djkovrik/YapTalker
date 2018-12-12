package com.sedsoftware.yaptalker.di.module

import android.app.Application
import androidx.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.common.AppSchedulers
import com.sedsoftware.yaptalker.data.database.YapTalkerDatabase
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Module(includes = [(AndroidInjectionModule::class)])
abstract class AppModule {

    @Module
    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideContext(app: YapTalkerApp): Context = app

        @Provides
        @Singleton
        @JvmStatic
        fun provideResources(context: Context): Resources = context.resources

        @Provides
        @Singleton
        @JvmStatic
        fun provideDatabase(context: Context): YapTalkerDatabase =
            Room.databaseBuilder(context, YapTalkerDatabase::class.java, YapTalkerDatabase.DATABASE_NAME).build()

        @Provides
        @Singleton
        @JvmStatic
        fun provideSchedulers(): SchedulersProvider = AppSchedulers()

        @Provides
        @Singleton
        @JvmStatic
        fun provideGson(): Gson = Gson()

        @Provides
        @Singleton
        @JvmStatic
        fun provideSharedPreferences(ctx: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    @Binds
    abstract fun application(app: YapTalkerApp): Application
}
