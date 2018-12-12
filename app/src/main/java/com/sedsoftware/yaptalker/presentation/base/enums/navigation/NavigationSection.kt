package com.sedsoftware.yaptalker.presentation.base.enums.navigation

import androidx.annotation.LongDef

class NavigationSection {
    companion object {
        const val MAIN_PAGE = 0L
        const val FORUMS = 1L
        const val ACTIVE_TOPICS = 2L
        const val BOOKMARKS = 3L
        const val PICTURES = 4L
        const val VIDEO = 5L
        const val EVENTS = 6L
        const val AUTO_MOTO = 7L
        const val ANIMALS = 8L
        const val PHOTOBOMB = 9L
        const val INCUBATOR = 10L
        const val SITE_SEARCH = 11L
        const val SETTINGS = 12L
        const val APP_UPDATES = 13L
        const val CHANGELOG = 14L
        const val SIGN_IN = 15L
        const val SIGN_OUT = 16L
    }

    @Retention(AnnotationRetention.SOURCE)
    @LongDef(
        MAIN_PAGE,
        FORUMS,
        ACTIVE_TOPICS,
        BOOKMARKS,
        PICTURES,
        VIDEO,
        EVENTS,
        AUTO_MOTO,
        ANIMALS,
        PHOTOBOMB,
        INCUBATOR,
        SITE_SEARCH,
        SETTINGS,
        APP_UPDATES,
        CHANGELOG,
        SIGN_IN,
        SIGN_OUT
    )
    annotation class Section
}
