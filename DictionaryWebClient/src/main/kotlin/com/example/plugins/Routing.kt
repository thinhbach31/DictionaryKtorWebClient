package com.example.plugins

import com.example.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.json.Json

fun Application.configureRouting() {

    routing {

        get("/") {
            call.respondRedirect("translator")
        }
        route("translator") {
            post {
                val formParameters = call.receiveParameters()
                val english = formParameters.getOrFail("english")
                call.respondRedirect("/translator?english=${english}")

            }
            get {

                if (call.parameters["english"] == null || call.parameters["english"] == "") {
                    val dataMap = mapOf(
                        "word" to emptyWord,
                        "error" to ""
                    )
                    call.respond(FreeMarkerContent("dictionary.ftl", dataMap))
                }
                call.parameters["english"]?.let { english ->
                    HttpClient(CIO) {
                        install(ContentNegotiation) {
                            json(Json {
                                prettyPrint = true
                                isLenient = true
                            })
                        }
                    }.use { client ->
                        val response = client.get {
                            url("http://0.0.0.0:8080/word")
                            parameter("english", english)
                        }
                        when (response.status.value) {
                            404 -> {
                                val dataMap = mapOf(
                                    "word" to Word(0, english, ""),
                                    "error" to "Word Not Found"
                                )
                                call.respond(FreeMarkerContent("dictionary.ftl", dataMap))
                            }

                            200 -> {
                                val word: Word = response.body()
                                val dataMap = mapOf(
                                    "word" to word,
                                    "error" to ""
                                )
                                call.respond(FreeMarkerContent("dictionary.ftl", dataMap))
                            }

                            else -> {
                                val dataMap = mapOf(
                                    "word" to Word(0, english, ""),
                                    "error" to "Unknown Error"
                                )
                                call.respond(FreeMarkerContent("dictionary.ftl", dataMap))
                            }
                        }
                    }
                }
            }

        }
    }
}
