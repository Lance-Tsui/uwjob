package com.cs346JobinfoAPI.plugins

import com.cs346JobinfoAPI.routes.*

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        benefitRouting()
        companyRouting()
        progRouting()
        reportRouting()
        reportBenefitRouting()
        reportInfoRouting()
        studentPersonalInfoRouting()
    }
}
