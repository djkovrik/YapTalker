package com.sedsoftware.yaptalker.device.delegate

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPrefsDelegate<R, T>(
    private val type: Class<T>,
    private val gson: Gson,
    private val sharedPreferences: SharedPreferences
) : ReadWriteProperty<R, T?> {

    var value: T? = null

    override fun getValue(thisRef: R, property: KProperty<*>): T? {
        if (value == null) {
            value = gson.fromJson(sharedPreferences.getString(type.name, null), type)
        }

        return value
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T?) {
        if (value != null) {
            sharedPreferences.edit().putString(type.name, gson.toJson(value)).apply()
        }

        this.value = value
    }
}
