package com.nightfurydev.opslybackendtest.model

data class AggregatedMessages(val facebook: List<FacebookMessage>,
                              val twitter: List<TwitterMessage>,
                              val instagram: List<InstagramMessage>)