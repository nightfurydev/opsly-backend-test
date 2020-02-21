package com.nightfurydev.opslybackendtest.handler

import com.nightfurydev.opslybackendtest.SocialNetworkUrls
import com.nightfurydev.opslybackendtest.api.SocialNetworkClient
import com.nightfurydev.opslybackendtest.model.AggregatedMessages
import com.nightfurydev.opslybackendtest.model.FacebookMessage
import com.nightfurydev.opslybackendtest.model.InstagramMessage
import com.nightfurydev.opslybackendtest.model.TwitterMessage
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Component
class Handler(val client: SocialNetworkClient,
              val urls: SocialNetworkUrls) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getAllMessages(req: ServerRequest) = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromProducer(retrieveMessages(), AggregatedMessages::class.java))

    private fun retrieveMessages(): Mono<AggregatedMessages> {
        logger.info("Starting message retrieval...")
        val facebook = client.getMessages(urls.facebook, FacebookMessage::class.java).subscribeOn(Schedulers.elastic())
        val twitter = client.getMessages(urls.twitter, TwitterMessage::class.java).subscribeOn(Schedulers.elastic())
        val instagram = client.getMessages(urls.instagram, InstagramMessage::class.java).subscribeOn(Schedulers.elastic())

        return Mono.zip(facebook, twitter, instagram)
            .flatMap { tuple -> Mono.just(AggregatedMessages(
                facebook = tuple.t1.map { it.status },
                twitter = tuple.t2.map { it.tweet },
                instagram = tuple.t3.map { it.picture })) }
            .doAfterTerminate {
                logger.info("Messages retrieved!")
            }
    }
}