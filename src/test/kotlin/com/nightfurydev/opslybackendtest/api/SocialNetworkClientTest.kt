package com.nightfurydev.opslybackendtest.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nightfurydev.opslybackendtest.TestData
import com.nightfurydev.opslybackendtest.model.FacebookMessage
import com.nightfurydev.opslybackendtest.model.InstagramMessage
import com.nightfurydev.opslybackendtest.model.TwitterMessage
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class SocialNetworkClientTest {
    private val client = SocialNetworkClient()

    @Test
    fun `can retrieve Facebook messages`() {
        mockServer.enqueue(successfulFacebookResponse)

        val response = client.getMessages(baseUrl, FacebookMessage::class.java).block()

        assertNotNull(response)
        assertEquals(2, response?.size)
    }

    @Test
    fun `can handle partially correct Facebook response`() {
        mockServer.enqueue(partiallyCorrectFacebookResponse)

        val response = client.getMessages(baseUrl, FacebookMessage::class.java).block()

        assertNotNull(response)
        assertEquals(0, response?.size)
    }

    @Test
    fun `can retrieve Twitter messages`() {
        mockServer.enqueue(successfulTwitterResponse)

        val response = client.getMessages(baseUrl, TwitterMessage::class.java).block()

        assertNotNull(response)
        assertEquals(3, response?.size)
    }

    @Test
    fun `can handle partially correct Twitter response`() {
        mockServer.enqueue(partiallyCorrectTwitterResponse)

        val response = client.getMessages(baseUrl, TwitterMessage::class.java).block()

        assertNotNull(response)
        assertEquals(0, response?.size)
    }

    @Test
    fun `can retrieve Instagram messages`() {
        mockServer.enqueue(successfulInstagramResponse)

        val response = client.getMessages(baseUrl, InstagramMessage::class.java).block()

        assertNotNull(response)
        assertEquals(3, response?.size)
    }

    @Test
    fun `can handle partially correct Instagram response`() {
        mockServer.enqueue(partiallyCorrectInstagramResponse)

        val response = client.getMessages(baseUrl, InstagramMessage::class.java).block()

        assertNotNull(response)
        assertEquals(0, response?.size)
    }

    @Test
    fun `can handle incorrect media type response`() {
        mockServer.enqueue(incorrectTextResponse)

        val response = client.getMessages(baseUrl, FacebookMessage::class.java).block()

        assertNotNull(response)
        assertEquals(0, response?.size)
    }

    @Test
    fun `can handle fully malformed response`() {
        mockServer.enqueue(malformedJsonResponse)

        val response = client.getMessages(baseUrl, FacebookMessage::class.java).block()

        assertNotNull(response)
        assertEquals(0, response?.size)
    }

    @Test
    fun `can handle erroneous response`() {
        mockServer.enqueue(erroneousResponse)

        val response = client.getMessages(baseUrl, FacebookMessage::class.java).block()

        assertNotNull(response)
        assertEquals(0, response?.size)
    }

    companion object {
        private lateinit var mockServer: MockWebServer
        private lateinit var baseUrl: String

        @BeforeAll
        @JvmStatic
        fun setUp() {
            mockServer = MockWebServer()
            mockServer.start()
            baseUrl = "http://localhost:${mockServer.port}"
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            mockServer.shutdown()
        }

        private const val CONTENT_TYPE = "Content-Type"
        private val objectMapper = ObjectMapper().registerModule(KotlinModule())

        val successfulFacebookResponse = MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(objectMapper.writeValueAsString(TestData.facebookMessages))

        val partiallyCorrectFacebookResponse = MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("""[
            |{},
            |${objectMapper.writeValueAsString(TestData.facebookMessages[0])}
            |]""".trimMargin())

        val successfulTwitterResponse = MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(objectMapper.writeValueAsString(TestData.twitterMessages))

        val partiallyCorrectTwitterResponse = MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("""[
            |{},
            |{},
            |${objectMapper.writeValueAsString(TestData.twitterMessages[0])}
            |]""".trimMargin())

        val successfulInstagramResponse = MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(objectMapper.writeValueAsString(TestData.instagramMessages))

        val partiallyCorrectInstagramResponse = MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("""[
            |{},
            |${objectMapper.writeValueAsString(TestData.instagramMessages[0])},
            |{}
            |]""".trimMargin())

        val incorrectTextResponse = MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
            .setBody("Some text")

        val malformedJsonResponse = MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("[{} {}]")

        val erroneousResponse = MockResponse()
            .setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value())
    }

}