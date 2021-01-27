package com.monkeydp.tools.ext.javax.servlet.http

import org.apache.commons.io.IOUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class MultiReadHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val cachedBytes: ByteArrayOutputStream

    init {
        cachedBytes = ByteArrayOutputStream()
        IOUtils.copy(super.getInputStream(), cachedBytes)
    }

    override fun getInputStream() =
            CachedServletInputStream(cachedBytes.toByteArray())

    override fun getReader() =
            BufferedReader(InputStreamReader(inputStream))

    class CachedServletInputStream(contents: ByteArray) : ServletInputStream() {

        private val buffer: ByteArrayInputStream

        init {
            buffer = ByteArrayInputStream(contents)
        }

        override fun read() =
                buffer.read()

        override fun isFinished() =
                buffer.available() == 0

        override fun isReady() =
                true

        override fun setReadListener(listener: ReadListener) {
            throw RuntimeException("Not implemented")
        }
    }
}
