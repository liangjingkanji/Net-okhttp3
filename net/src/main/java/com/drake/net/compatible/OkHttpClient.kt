package com.drake.net.compatible

import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient

val OkHttpClient.interceptors: MutableList<Interceptor>
    get() = interceptors()

val OkHttpClient.dispatcher: Dispatcher
    get() = dispatcher()