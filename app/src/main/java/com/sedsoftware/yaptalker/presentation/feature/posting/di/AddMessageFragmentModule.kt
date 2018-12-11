package com.sedsoftware.yaptalker.presentation.feature.posting.di

import com.sedsoftware.yaptalker.data.repository.YapEmojiRepository
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.di.scope.FragmentScope
import com.sedsoftware.yaptalker.domain.interactor.EmojiInteractor
import com.sedsoftware.yaptalker.domain.repository.EmojiRepository
import com.sedsoftware.yaptalker.presentation.feature.posting.AddMessagePresenter
import com.sedsoftware.yaptalker.presentation.feature.posting.adapter.EmojiClickListener
import com.sedsoftware.yaptalker.presentation.mapper.EmojiModelMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router

@Module
abstract class AddMessageFragmentModule {

    @Module
    companion object {

        @FragmentScope
        @Provides
        @JvmStatic
        fun providePresenter(
            router: Router,
            interactor: EmojiInteractor,
            mapper: EmojiModelMapper,
            schedulers: SchedulersProvider
        ): AddMessagePresenter = AddMessagePresenter(router, interactor, mapper, schedulers)
    }

    @FragmentScope
    @Binds
    abstract fun emojiRepository(repository: YapEmojiRepository): EmojiRepository

    @FragmentScope
    @Binds
    abstract fun emojiClickListener(presenter: AddMessagePresenter): EmojiClickListener
}
