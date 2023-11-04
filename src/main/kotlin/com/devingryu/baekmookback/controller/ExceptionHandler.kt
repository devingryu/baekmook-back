package com.devingryu.baekmookback.controller

import com.devingryu.baekmookback.dto.BaseException
import com.devingryu.baekmookback.dto.BaseResponse
import com.devingryu.baekmookback.dto.BaseResponse.Companion.toResponse
import com.devingryu.baekmookback.dto.BaseResponseCode
import org.springframework.http.HttpMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(BaseException::class)
    protected fun handleBaseException(e: BaseException): ResponseEntity<BaseResponse> {
        return with(e.baseResponseCode) {
            ResponseEntity.status(status)
                    .body(BaseResponse(message, messageTranslated, clazz))
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleRequestParseException(mnr: HttpMessageNotReadableException): ResponseEntity<BaseResponse> {
        val body = BaseResponseCode.BAD_REQUEST
        return ResponseEntity.status(body.status)
            .body(body.toResponse())
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected fun handleNoHandlerFoundException(e: NoHandlerFoundException): ResponseEntity<BaseResponse> {
        val body = BaseResponseCode.HANDLER_NOT_FOUND
        return ResponseEntity.status(body.status)
            .body(body.toResponse())
    }

    @ExceptionHandler(Exception::class)
    protected fun handleAnyException(e: Exception): ResponseEntity<BaseResponse> {
        val body = BaseResponseCode.INTERNAL_SERVER_ERROR
        return ResponseEntity.status(body.status)
            .body(body.toResponse())
    }
}