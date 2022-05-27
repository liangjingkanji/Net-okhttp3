package com.drake.net.utils

import android.os.Build
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient

/**
 * API<21 支持 TLSv1.1 和 TLSv1.2
 */
fun OkHttpClient.Builder.useLegacyTLS() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        val legacyTls: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .cipherSuites(
                *ConnectionSpec.MODERN_TLS.cipherSuites()?.toTypedArray() ?: emptyArray(),
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA
            ).build()

        connectionSpecs(listOf(legacyTls, ConnectionSpec.CLEARTEXT))
    }
}