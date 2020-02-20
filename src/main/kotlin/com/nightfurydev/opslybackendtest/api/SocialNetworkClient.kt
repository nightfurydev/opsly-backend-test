package com.nightfurydev.opslybackendtest.api

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class SocialNetworkClient {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun <T> getMessages(url: String, cls: Class<T>): Mono<List<T>> {
        logger.info("Retrieving messages of type {} from {} ...", cls.simpleName, url)
        return WebClient.create()
            .get()
            .uri(url)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus({ it.isError }, { Mono.empty() })
            .bodyToFlux(cls)
            .onErrorResume {
                logger.warn("The error happened in reactive stream! Replacing it with empty Mono.", it)
                Mono.empty()
            }
            .collectList()
    }
}