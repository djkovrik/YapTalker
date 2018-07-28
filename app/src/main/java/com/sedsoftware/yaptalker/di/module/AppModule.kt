package com.sedsoftware.yaptalker.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
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
        fun provideDatabase(context: Context): YapTalkerDatabase =
            Room
                .databaseBuilder(context, YapTalkerDatabase::class.java, YapTalkerDatabase.DATABASE_NAME)
                .build()

        @Provides
        @Singleton
        @JvmStatic
        fun provideSchedulers(): SchedulersProvider =
            AppSchedulers()
    }

    @Binds
    abstract fun application(app: YapTalkerApp): Application
}
