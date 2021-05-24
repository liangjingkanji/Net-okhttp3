package com.drake.net.compatible

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.ByteString
import java.io.IOException

fun BufferedSource.asResponseBody(
    contentType: MediaType? = null,
    contentLength: Long = -1L
): ResponseBody = ResponseBody.create(contentType, contentLength, this)

@Throws(IOException::class)
fun ResponseBody.byteString(): ByteString? {
    val contentLength = contentLength()
    if (contentLength > Int.MAX_VALUE) {
        throw IOException("Cannot buffer entire body for content length: $contentLength")
    }
    val bytes = source().use { it.readByteString() }
    val size = bytes.size()
    if (contentLength != -1L && contentLength != size.toLong()) {
        throw IOException("Content-Length ($contentLength) and stream length ($size) disagree")
    }
    return bytes
}