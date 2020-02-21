package com.nightfurydev.opslybackendtest.model

data class AggregatedMessages(val facebook: List<String>,
                              val twitter: List<String>,
                              val instagram: List<String>)