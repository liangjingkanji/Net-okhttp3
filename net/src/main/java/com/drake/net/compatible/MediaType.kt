package com.drake.net.compatible

import okhttp3.MediaType


val MediaType.type: String
    get() = type()

fun String.toMediaType(): MediaType = MediaType.get(this)

fun String.toMediaTypeOrNull(): MediaType? = MediaType.parse(this)

val MediaType.subtype: String
    get() = subtype()