package de.bilkewall.cinder.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class APIManager {

    var jsonHTTPClient = HttpClient {
        expectSuccess = true

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            url.host = "thecocktaildb.com"
            url.protocol = URLProtocol.HTTPS
            url.encodedPath = "/api/json/v1/1/" + url.encodedPath
            contentType(ContentType.Application.Json)
        }

        HttpResponseValidator {
            getCustomResponseValidator(this)
        }
    }

    private fun getCustomResponseValidator(responseValidator: HttpCallValidator.Config): HttpCallValidator.Config {
        responseValidator.handleResponseExceptionWithRequest { exception, _ ->
            var exceptionResponseText =
                exception.message ?: "Unknown Error occurred. Please contact your administrator"

            if (exception is ClientRequestException) {
                // 400 Errors

                val exceptionResponse = exception.response
                exceptionResponseText = exceptionResponse.bodyAsText()
            } else if (exception is ServerResponseException) {
                // 500 Errors

                val exceptionResponse = exception.response
                exceptionResponseText = exceptionResponse.bodyAsText()
            }

            throw CancellationException(exceptionResponseText)
        }

        return responseValidator
    }
}