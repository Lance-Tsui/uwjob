package com.cs346JobinfoAPI

import com.cs346JobinfoAPI.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.security.MessageDigest

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module){
        install(ContentNegotiation) {
            json()
        }
        install(Authentication) {
            basic("auth-basic") {
                realm = "Ktor Server"
                validate { credentials ->
                    if (fakeAuthenticate(credentials.name, credentials.password)) {
                        UserIdPrincipal(credentials.name)
                    } else null
                }
            }
        }
        routing {
            fakeAuthenticate("auth-basic") {
                post("/login") {
                    val principal = call.principal<UserIdPrincipal>()!!
                    val token = generateToken(principal.name)
                    call.respond(mapOf("token" to token))
                }
            }

            // Protected route using bearer token
            get("/secured") {
                val authHeader = call.request.headers[HttpHeaders.Authorization] ?: ""
                if (authHeader.startsWith("Bearer ")) {
                    val token = authHeader.removePrefix("Bearer ")
                    if (validateToken(token)) {
                        call.respondText("Access to secured content granted.")
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "Invalid Token")
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Missing or invalid authorization header")
                }
            }
        }
    }.start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

// placeholder to be replaced by the real thing once database is up
fun fakeAuthenticate(username: String, password: String): Boolean {
    return username == "testuser@uwaterloo.ca" && password == hashPassword("password")
}

// the real thing that needs implementation
fun authenticate(username: String, password: String): Boolean {
    // username = fetch username from database
    // passwordHash = fetch password hash belonging to the username
    // salt = fetch salt belonging to the username
    // return hashPassword(password + salt) == passwordHash
}

fun generateToken(username: String): String {
    val secret = "VoffxLF%ykPsDNXms7Yp"
    val issuer = "RayDai"
    val validityInMs = 36_000_00 * 10 // Example: 10 hours
    val now = System.currentTimeMillis()
    return JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("username", username)
        .withExpiresAt(Date(now + validityInMs))
        .sign(Algorithm.HMAC256(secret))
}

fun hashPassword(password: String): String {
    val md = MessageDigest.getInstance("SHA-512")
    val digest = md.digest(password.toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}