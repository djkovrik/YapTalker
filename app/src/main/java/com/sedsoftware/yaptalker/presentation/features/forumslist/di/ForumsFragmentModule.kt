package com.sedsoftware.yaptalker.presentation.features.forumslist.di

import com.sedsoftware.yaptalker.data.repository.YapForumsListRepository
import com.sedsoftware.yaptalker.di.scopes.FragmentScope
import com.sedsoftware.yaptalker.domain.repository.ForumsListRepository
import com.sedsoftware.yaptalker.presentation.features.forumslist.ForumsFragment
import com.sedsoftware.yaptalker.presentation.features.forumslist.adapter.ForumsItemClickListener
import dagger.Binds
import dagger.Module

@Module
abstract class ForumsFragmentModule {

  @FragmentScope
  @Binds
  abstract fun forumsListRepository(repository: YapForumsListRepository): ForumsListRepository

  @FragmentScope
  @Binds
  abstract fun forumsItemClickListener(fragment: ForumsFragment): ForumsItemClickListener
}
