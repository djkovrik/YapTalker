package com.sedsoftware.yaptalker.di.modules

import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [(AndroidSupportInjectionModule::class)])
interface ActivityContributionModule
