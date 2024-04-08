package com.cs346JobinfoAPI

import com.cs346JobinfoAPI.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

import com.cs346JobinfoAPI.models.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        jobinfo.connect()
        application {
            configureRouting()
            configureSerialization()
        }
        client.get("/benefit").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        client.get("/company").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        client.get("/prog").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        client.get("/report").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        client.get("/reportBenefit").apply {
            assertEquals("[]", bodyAsText())
        }
        client.get("/reportInfo").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        client.get("/studentPersonalInfo").apply {
            assertEquals(HttpStatusCode.OK, status)
        }


        jobinfo.disconnect()
    }
}
