package com.yausername.youtubedl_android.data.local.streams

import android.util.Log
import com.yausername.youtubedl_android.BuildConfig
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

internal class StreamGobbler(private val buffer: StringBuilder, private val stream: InputStream) :
    Thread() {

    init {
        start()
    }

    override fun run() {
        try {
            InputStreamReader(stream, StandardCharsets.UTF_8).use { reader ->
                var nextChar: Int
                while (reader.read().also { nextChar = it } != -1) {
                    buffer.append(nextChar.toChar())
                }
            }
        } catch (e: IOException) {
            if (BuildConfig.DEBUG) Log.e(TAG, "failed to read stream", e)
        }
    }

    companion object {
        private val TAG = StreamGobbler::class.java.simpleName
    }
}
