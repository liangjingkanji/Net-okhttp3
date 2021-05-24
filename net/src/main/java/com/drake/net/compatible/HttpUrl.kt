package com.drake.net.compatible

import okhttp3.HttpUrl

fun String.toHttpUrl(): HttpUrl = HttpUrl.get(this)

fun String.toHttpUrlOrNull(): HttpUrl? = HttpUrl.parse(this)

val HttpUrl.pathSegments: MutableList<String>
    get() = pathSegments()