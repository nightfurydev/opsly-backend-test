package com.nightfurydev.opslybackendtest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("social-networks")
data class SocialNetworkUrls(
    val facebook: String,
    val instagram: String,
    val twitter: String
)