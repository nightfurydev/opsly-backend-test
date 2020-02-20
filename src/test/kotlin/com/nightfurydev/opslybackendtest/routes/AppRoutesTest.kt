package com.nightfurydev.opslybackendtest.routes

import com.nhaarman.mockitokotlin2.whenever
import com.nightfurydev.opslybackendtest.SocialNetworkUrls
import com.nightfurydev.opslybackendtest.TestData
import com.nightfurydev.opslybackendtest.api.SocialNetworkClient
import com.nightfurydev.opslybackendtest.model.AggregatedMessages
import com.nightfurydev.opslybackendtest.model.FacebookMessage
import com.nightfurydev.opslybackendtest.model.InstagramMessage
import com.nightfurydev.opslybackendtest.model.TwitterMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@SpringBootTest
class AppRoutesTest {

    @Autowired
    private lateinit var context: ApplicationContext

    @Autowired
    private lateinit var urls: SocialNetworkUrls

    @MockBean
    private lateinit var client: SocialNetworkClient

    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build()
    }

    @Test
    fun `should return combined messages`() {
        whenever(client.getMessages(urls.facebook, FacebookMessage::class.java))
            .thenReturn(Mono.just(TestData.facebookMessages))
        whenever(client.getMessages(urls.twitter, TwitterMessage::class.java))
            .thenReturn(Mono.just(TestData.twitterMessages))
        whenever(client.getMessages(urls.instagram, InstagramMessage::class.java))
            .thenReturn(Mono.just(TestData.instagramMessages))

        webTestClient.get()
            .uri("/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<AggregatedMessages>()
            .isEqualTo(
                AggregatedMessages(
                    TestData.facebookMessages,
                    TestData.twitterMessages,
                    TestData.instagramMessages
                )
            )
    }

    @Test
    fun `should return combined messages when Facebook returns nothing`() {
        whenever(client.getMessages(urls.facebook, FacebookMessage::class.java))
            .thenReturn(Mono.just(emptyList()))
        whenever(client.getMessages(urls.twitter, TwitterMessage::class.java))
            .thenReturn(Mono.just(TestData.twitterMessages))
        whenever(client.getMessages(urls.instagram, InstagramMessage::class.java))
            .thenReturn(Mono.just(TestData.instagramMessages))

        webTestClient.get()
            .uri("/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<AggregatedMessages>()
            .isEqualTo(
                AggregatedMessages(
                    emptyList(),
                    TestData.twitterMessages,
                    TestData.instagramMessages
                )
            )
    }

    @Test
    fun `should return combined messages when Twitter returns nothing`() {
        whenever(client.getMessages(urls.facebook, FacebookMessage::class.java))
            .thenReturn(Mono.just(TestData.facebookMessages))
        whenever(client.getMessages(urls.twitter, TwitterMessage::class.java))
            .thenReturn(Mono.just(emptyList()))
        whenever(client.getMessages(urls.instagram, InstagramMessage::class.java))
            .thenReturn(Mono.just(TestData.instagramMessages))

        webTestClient.get()
            .uri("/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<AggregatedMessages>()
            .isEqualTo(
                AggregatedMessages(
                    TestData.facebookMessages,
                    emptyList(),
                    TestData.instagramMessages
                )
            )
    }

    @Test
    fun `should return combined messages when Instagram returns nothing`() {
        whenever(client.getMessages(urls.facebook, FacebookMessage::class.java))
            .thenReturn(Mono.just(TestData.facebookMessages))
        whenever(client.getMessages(urls.twitter, TwitterMessage::class.java))
            .thenReturn(Mono.just(TestData.twitterMessages))
        whenever(client.getMessages(urls.instagram, InstagramMessage::class.java))
            .thenReturn(Mono.just(emptyList()))

        webTestClient.get()
            .uri("/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<AggregatedMessages>()
            .isEqualTo(
                AggregatedMessages(
                    TestData.facebookMessages,
                    TestData.twitterMessages,
                    emptyList()
                )
            )
    }

    @Test
    fun `should return combined messages when no social network responds`() {
        whenever(client.getMessages(urls.facebook, FacebookMessage::class.java))
            .thenReturn(Mono.just(emptyList()))
        whenever(client.getMessages(urls.twitter, TwitterMessage::class.java))
            .thenReturn(Mono.just(emptyList()))
        whenever(client.getMessages(urls.instagram, InstagramMessage::class.java))
            .thenReturn(Mono.just(emptyList()))

        webTestClient.get()
            .uri("/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<AggregatedMessages>()
            .isEqualTo(
                AggregatedMessages(
                    emptyList(),
                    emptyList(),
                    emptyList()
                )
            )
    }
}