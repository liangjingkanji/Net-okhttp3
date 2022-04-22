package com.drake.net.compatible

import okhttp3.*
import okio.*
import java.io.*
import java.security.cert.Certificate
import java.util.concurrent.TimeUnit

val OkHttpClient.interceptors: MutableList<Interceptor>
    get() = interceptors()

val OkHttpClient.dispatcher: Dispatcher
    get() = dispatcher()

val OkHttpClient.cache: Cache?
    get() = cache()

fun String.toHttpUrl(): HttpUrl = HttpUrl.get(this)

fun String.toHttpUrlOrNull(): HttpUrl? = HttpUrl.parse(this)

val HttpUrl.pathSegments: MutableList<String>
    get() = pathSegments()

val MediaType.type: String
    get() = type()

fun String.toMediaType(): MediaType = MediaType.get(this)

fun String.toMediaTypeOrNull(): MediaType? = MediaType.parse(this)

val MediaType.subtype: String
    get() = subtype()

val Buffer.size: Long
    get() = size()

val BufferedSource.buffer: Buffer
    get() = buffer()

fun Sink.buffer(): BufferedSink = Okio.buffer(this)

val BufferedSink.buffer: Buffer get() = buffer()

fun Source.buffer(): BufferedSource = Okio.buffer(this)

@Throws(FileNotFoundException::class)
fun File.source(): Source = Okio.source(this)

@Throws(FileNotFoundException::class)
fun InputStream.source(): Source = Okio.source(this)

@JvmOverloads
@Throws(FileNotFoundException::class)
fun File.sink(append: Boolean = false): Sink = Okio.sink(this)

@Throws(IOException::class)
fun BufferedSource.peek(): BufferedSource {
    val buffer = buffer()
    request(buffer.size())
    return buffer.clone().buffer()
}

val Request.body: RequestBody?
    get() = body()

val Request.method: String
    get() = method()

val Request.url: HttpUrl
    get() = url()

val Request.headers: Headers
    get() = headers()

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

fun Source.discard(timeout: Int, timeUnit: TimeUnit): Boolean = try {
    this.skipAll(timeout, timeUnit)
} catch (_: IOException) {
    false
}

@Throws(IOException::class)
fun Source.skipAll(duration: Int, timeUnit: TimeUnit): Boolean {
    val nowNs = System.nanoTime()
    val originalDurationNs = if (timeout().hasDeadline()) {
        timeout().deadlineNanoTime() - nowNs
    } else {
        Long.MAX_VALUE
    }
    timeout().deadlineNanoTime(nowNs + minOf(originalDurationNs, timeUnit.toNanos(duration.toLong())))
    return try {
        val skipBuffer = Buffer()
        while (read(skipBuffer, 8192) != -1L) {
            skipBuffer.clear()
        }
        true // Success! The source has been exhausted.
    } catch (_: InterruptedIOException) {
        false // We ran out of time before exhausting the source.
    } finally {
        if (originalDurationNs == Long.MAX_VALUE) {
            timeout().clearDeadline()
        } else {
            timeout().deadlineNanoTime(nowNs + originalDurationNs)
        }
    }
}

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

fun String.toLongOrDefault(defaultValue: Long): Long {
    return try {
        toLong()
    } catch (_: NumberFormatException) {
        defaultValue
    }
}

fun Closeable.closeQuietly() {
    try {
        close()
    } catch (rethrown: RuntimeException) {
        throw rethrown
    } catch (_: Exception) {
    }
}

fun String.decodeBase64() = ByteString.decodeBase64(this)

fun String.encodeUtf8(): ByteString = ByteString.encodeUtf8(this)

fun ByteArray.toByteString(): ByteString = ByteString.of(*this)

@JvmField
val EMPTY_HEADERS = Headers.of()
val Response.protocol: Protocol
    get() = protocol()
val Response.handshake: Handshake?
    get() = handshake()
val Response.message: String
    get() = message()
val Response.sentRequestAtMillis: Long
    get() = receivedResponseAtMillis()
val Response.networkResponse: Response?
    get() = networkResponse()
val Response.receivedResponseAtMillis: Long
    get() = receivedResponseAtMillis()
val Handshake.cipherSuite: CipherSuite
    get() = cipherSuite()
val CipherSuite.javaName: String
    get() = javaName()
val Handshake.peerCertificates: MutableList<Certificate>
    get() = peerCertificates()
val Handshake.localCertificates: MutableList<Certificate>
    get() = localCertificates()
val Handshake.tlsVersion: TlsVersion
    get() = tlsVersion()
val TlsVersion.javaName: String
    get() = javaName()
val Headers.size: Int
    get() = size()
