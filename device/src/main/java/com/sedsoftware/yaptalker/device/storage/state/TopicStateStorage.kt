package com.sedsoftware.yaptalker.device.storage.state

import android.content.SharedPreferences
import com.google.gson.Gson
import com.sedsoftware.yaptalker.device.delegate.SharedPrefsDelegate
import javax.inject.Inject

class TopicStateStorage @Inject constructor(
    gson: Gson,
    sharedPreferences: SharedPreferences
) {

    private var savedState: TopicState? by SharedPrefsDelegate(TopicState::class.java, gson, sharedPreferences)

    fun getState(): TopicState? = savedState

    fun saveState(newState: TopicState) {
        savedState = newState
    }

    fun clearState() {
        savedState = null
    }
}
