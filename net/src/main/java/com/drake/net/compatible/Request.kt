package com.drake.net.compatible

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.RequestBody

val Request.body: RequestBody?
    get() = body()

val Request.method: String
    get() = method()

val Request.url: HttpUrl
    get() = url()

val Request.headers: Headers
    get() = headers()