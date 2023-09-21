package com.devingryu.baekmookback.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

data class BaseResponse(
    val message: String,
    @JsonProperty("message_translated")
    val messageTranslated: String,
    val clazz: String
) {
    companion object {
        fun BaseResponseCode.toResponse(): BaseResponse =
            BaseResponse(message, messageTranslated, clazz)
    }
}