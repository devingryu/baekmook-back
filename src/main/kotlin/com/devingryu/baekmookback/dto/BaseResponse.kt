package com.devingryu.baekmookback.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BaseResponse(
    val message: String,
    val messageTranslated: String,
    val errClazz: String
) {
    companion object {
        fun BaseResponseCode.toResponse(): BaseResponse =
            BaseResponse(message, messageTranslated, clazz)
    }
}