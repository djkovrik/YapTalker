package com.sedsoftware.yaptalker.di.modules

import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = arrayOf(
    AndroidSupportInjectionModule::class
))
interface ActivityContributionModule