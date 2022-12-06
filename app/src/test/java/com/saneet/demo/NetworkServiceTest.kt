package com.saneet.demo

import com.saneet.demo.network.Constants.Companion.ARG_LIMIT
import com.saneet.demo.network.Constants.Companion.ARG_OFFSET
import com.saneet.demo.network.NetworkModule
import com.saneet.demo.network.NetworkService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkServiceTest {
    @Test
    fun fetch_all_schools() {
        val recordedRequest = runRequest {
            fetchAllSchoolsList(10, 20)
                .subscribe()
        }
        assertEquals(recordedRequest.method, "GET")
        assertEquals(recordedRequest.requestUrl!!.queryParameter(ARG_LIMIT), "10")
        assertEquals(recordedRequest.requestUrl!!.queryParameter(ARG_OFFSET), "20")
    }

    private fun runRequest(code: NetworkService.() -> Unit) =
        MockWebServer().use {
            it.enqueue(MockResponse())
            it.start()
            code(NetworkModule().provideRxService(it.url("/").toString()))
            it.takeRequest()
        }
}


