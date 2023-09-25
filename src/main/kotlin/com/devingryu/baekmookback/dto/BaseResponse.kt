package com.devingryu.baekmookback.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BaseResponse(
    val message: String,
    @JsonProperty("message_translated")
    val messageTranslated: String,
    @JsonProperty("err_clazz")
    val errClazz: String
) {
    companion object {
        fun BaseResponseCode.toResponse(): BaseResponse =
            BaseResponse(message, messageTranslated, clazz)
    }
}