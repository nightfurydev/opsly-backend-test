package com.nightfurydev.opslybackendtest

import com.nightfurydev.opslybackendtest.model.FacebookMessage
import com.nightfurydev.opslybackendtest.model.InstagramMessage
import com.nightfurydev.opslybackendtest.model.TwitterMessage

object TestData {
    val facebookMessages = listOf(
        FacebookMessage(name = "test", status = "test status"),
        FacebookMessage(name = "test2", status = "new status")
    )
    val twitterMessages = listOf(
        TwitterMessage(username = "test", tweet = "some tweet"),
        TwitterMessage(username = "test2", tweet = "another tweet"),
        TwitterMessage(username = "test3", tweet = "new tweet")
    )
    val instagramMessages = listOf(
        InstagramMessage(username = "test", picture = "Test Forest"),
        InstagramMessage(username = "test2", picture = "Delicious Food"),
        InstagramMessage(username = "test3", picture = "Nothing interesting")
    )
}