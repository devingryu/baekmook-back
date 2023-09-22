package com.devingryu.baekmookback.controller

import com.devingryu.baekmookback.dto.BaseException
import com.devingryu.baekmookback.dto.BaseResponse
import com.devingryu.baekmookback.dto.BaseResponse.Companion.toResponse
import com.devingryu.baekmookback.dto.BaseResponseCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(BaseException::class)
    protected fun handleBaseException(e: BaseException): ResponseEntity<BaseResponse> {
        return with(e.baseResponseCode) {
            ResponseEntity.status(status)
                    .body(BaseResponse(message, messageTranslated, clazz))
        }
    }

    @ExceptionHandler(Exception::class)
    protected fun handleAnyException(e: Exception): ResponseEntity<BaseResponse> {
        val body = BaseResponseCode.INTERNAL_SERVER_ERROR
        return ResponseEntity.status(body.status)
            .body(body.toResponse())
    }
}