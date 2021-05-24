package com.drake.net.compatible

import okio.*
import java.io.*

val Buffer.size: Long
    get() = size()

val BufferedSource.buffer: Buffer
    get() = buffer()

fun Sink.buffer(): BufferedSink = Okio.buffer(this)

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