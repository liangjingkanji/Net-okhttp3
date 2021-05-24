package com.drake.net.compatible

import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

val Response.request: Request
    get() = request()

val Response.body: ResponseBody?
    get() = body()

val Response.code: Int
    get() = code()

val Response.headers: Headers
    get() = headers()

fun Response.closeQuietly() {
    try {
        close()
    } catch (rethrown: RuntimeException) {
        throw rethrown
    } catch (_: Exception) {
    }
}