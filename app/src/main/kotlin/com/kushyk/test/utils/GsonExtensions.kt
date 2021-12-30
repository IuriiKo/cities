package com.kushyk.test.utils

import android.content.Context
import androidx.annotation.RawRes
import com.google.gson.Gson

fun <T> Gson.fromRawJson(
    context: Context,
    @RawRes rawId: Int,
    classOf: Class<T>
): T = context.resources.openRawResource(rawId).bufferedReader().use {
    fromJson(it, classOf)
}