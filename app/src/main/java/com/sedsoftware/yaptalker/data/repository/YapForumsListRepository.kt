package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.ForumsListMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.Forum
import com.sedsoftware.yaptalker.domain.repository.ForumsListRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapForumsListRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val settings: SettingsManager,
    private val dataMapper: ForumsListMapper
) : ForumsListRepository {

  @Suppress("MagicNumber")
  companion object {
    private val nsfwForumSections = setOf(4, 33, 36)
  }

  override fun getMainForumsList(): Observable<BaseEntity> =
      dataLoader
          .loadForumsList()
          .map { parsedList -> dataMapper.transform(parsedList) }
          .flatMap { forumsList -> Observable.fromIterable(forumsList) }
          .filter { forumItem ->
            forumItem as Forum
            if (settings.isNsfwEnabled())
              true
            else
              !nsfwForumSections.contains(forumItem.forumId)
          }
}
