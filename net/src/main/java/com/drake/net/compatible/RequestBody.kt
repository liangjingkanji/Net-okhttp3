package com.drake.net.compatible

import okhttp3.*
import okio.ByteString
import java.io.File

val FormBody.size: Int
    get() = size()

fun String.toRequestBody(contentType: MediaType? = null): RequestBody =
    RequestBody.create(contentType, this)

internal fun ByteString.toRequestBody(contentType: MediaType? = null): RequestBody =
    RequestBody.create(contentType, this)

fun ByteArray.toRequestBody(
    contentType: MediaType? = null,
    offset: Int = 0,
    byteCount: Int = size
): RequestBody = RequestBody.create(contentType, this, offset, byteCount)

fun File.asRequestBody(contentType: MediaType? = null): RequestBody =
    RequestBody.create(contentType, this)

val MultipartBody.Part.headers: Headers?
    get() = headers()

val MultipartBody.Part.body: RequestBody
    get() = body()

val MultipartBody.parts: MutableList<MultipartBody.Part>
    get() = parts()
