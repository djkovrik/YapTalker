package com.sedsoftware.yaptalker.presentation.base.enums.navigation

import android.support.annotation.LongDef

class NavigationSection {
    companion object {
        const val MAIN_PAGE = 0L
        const val FORUMS = 1L
        const val ACTIVE_TOPICS = 2L
        const val INCUBATOR = 3L
        const val BOOKMARKS = 4L
        const val SITE_SEARCH = 5L
        const val SETTINGS = 6L
        const val APP_UPDATES = 7L
        const val CHANGELOG = 8L
        const val SIGN_IN = 9L
        const val SIGN_OUT = 10L
        const val MAIL = 11L
    }

    @Retention(AnnotationRetention.SOURCE)
    @LongDef(
        MAIN_PAGE,
        FORUMS,
        ACTIVE_TOPICS,
        INCUBATOR,
        BOOKMARKS,
        SITE_SEARCH,
        SETTINGS,
        APP_UPDATES,
        CHANGELOG,
        SIGN_IN,
        SIGN_OUT,
        MAIL
    )
    annotation class Section
}
