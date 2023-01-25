package com.drake.net.body

import com.drake.net.compatible.body
import com.drake.net.compatible.buffer
import com.drake.net.compatible.headers
import com.drake.net.compatible.peek
import com.drake.net.compatible.size
import com.drake.net.interfaces.ProgressListener
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import okio.ByteString
import java.util.concurrent.ConcurrentLinkedQueue

fun RequestBody.toNetRequestBody(listeners: ConcurrentLinkedQueue<ProgressListener>? = null) = run {
    NetRequestBody(this, listeners)
}

fun ResponseBody.toNetResponseBody(
    listeners: ConcurrentLinkedQueue<ProgressListener>? = null,
    complete: (() -> Unit)? = null
) = run { NetResponseBody(this, listeners, complete) }

/**
 * 复制一段指定长度的字符串内容
 * @param byteCount 复制的字节长度, 允许超过实际长度, 如果-1则返回完整的字符串内容
 */
fun RequestBody.peekBytes(byteCount: Long = 1024 * 1024): ByteString {
    return when (this) {
        is NetRequestBody -> peekBytes(byteCount)
        else -> {
            val buffer = Buffer()
            writeTo(buffer)
            val maxSize = if (byteCount < 0) buffer.size else minOf(buffer.size, byteCount)
            buffer.readByteString(maxSize)
        }
    }
}

/**
 * 复制一段指定长度的字符串内容
 * @param byteCount 复制的字节长度, 允许超过实际长度, 如果-1则返回完整的字符串内容
 */
fun ResponseBody.peekBytes(byteCount: Long = 1024 * 1024 * 4): ByteString {
    return when (this) {
        is NetResponseBody -> peekBytes(byteCount)
        else -> {
            val peeked = source().peek()
            peeked.request(byteCount)
            val maxSize = if (byteCount < 0) peeked.buffer.size else minOf(byteCount, peeked.buffer.size)
            peeked.readByteString(maxSize)
        }
    }
}

/**
 * 通过判断[okhttp3.Headers]里面的Content-Disposition是否存在filename属性来确定是否为文件类型[MultipartBody.Part]
 */
fun MultipartBody.Part.isFile(): Boolean {
    val contentDisposition = headers?.get("Content-Disposition") ?: return false
    return ";\\s${"filename"}=\"(.+?)\"".toRegex().find(contentDisposition)?.groupValues?.getOrNull(1) != null
}

/**
 * 返回Content-Disposition里面的字段名称
 */
fun MultipartBody.Part.name(): String? {
    val contentDisposition = headers?.get("Content-Disposition") ?: return null
    return ";\\s${"name"}=\"(.+?)\"".toRegex().find(contentDisposition)?.groupValues?.getOrNull(1) ?: ""
}

/**
 * 将[MultipartBody.Part.body]作为字符串返回
 * 如果[MultipartBody.Part]是文件类型则返回的是文件名称, 确定文件类型请参考[MultipartBody.Part.isFile]
 */
fun MultipartBody.Part.value(): String? {
    val contentDisposition = headers?.get("Content-Disposition") ?: return null
    return ";\\s${"filename"}=\"(.+?)\"".toRegex().find(contentDisposition)?.groupValues?.getOrNull(1)
        ?: body.peekBytes().utf8()
}