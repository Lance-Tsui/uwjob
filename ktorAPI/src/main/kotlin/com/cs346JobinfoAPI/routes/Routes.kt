package com.cs346JobinfoAPI.routes

import com.cs346JobinfoAPI.models.*

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.benefitRouting() {
    route("/benefit") {
        get {
            call.respond(jobinfo.get<Benefit>("Benefit"))
        }
    }
}

fun Route.companyRouting() {
    route("/company") {
        get {
            call.respond(jobinfo.get<Company>("Company"))
        }
    }
}

fun Route.progRouting() {
    route("/prog") {
        get {
            call.respond(jobinfo.get<Prog>("Prog"))
        }
    }
}

fun Route.reportRouting() {
    route("/report") {
        get {
            call.respond(jobinfo.get<Report>("Report"))
        }
    }
}

fun Route.reportBenefitRouting() {
    route("/reportBenefit") {
        get {
            call.respond(jobinfo.get<ReportBenefit>("ReportBenefit"))
        }
    }
}

fun Route.reportInfoRouting() {
    route("/reportInfo") {
        get {
            call.respond(jobinfo.get<ReportInfo>("ReportInfo"))
        }
    }
}

fun Route.studentPersonalInfoRouting() {
    route("/studentPersonalInfo") {
        get {
            call.respond(jobinfo.get<StudentPersonalInfo>("StudentPersonalInfo"))
        }
    }
}
