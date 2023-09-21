package com.devingryu.baekmookback.util

import org.slf4j.LoggerFactory
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object Extensions {
    fun LocalDateTime.toTimestamp()
    = Timestamp.valueOf(this).time

    fun LocalDateTime.toDate()
    = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
}

inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!
