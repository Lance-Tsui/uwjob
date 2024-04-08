package com.cs346JobinfoAPI

import com.cs346JobinfoAPI.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import com.cs346JobinfoAPI.models.*

fun main() {
    jobinfo.connect()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

    jobinfo.disconnect()
}

fun Application.module() {
    configureRouting()
    configureSerialization()
}
