package com.airwallex.fc

import com.google.gson.Gson
import kotlin.reflect.KClass

class Utils {

    companion object {
        val gson = Gson()
    }

    inline fun <reified T> str2Pojo(string: String): T {
        return gson.fromJson(string, T::class.java)
    }
}