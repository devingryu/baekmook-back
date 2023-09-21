package com.devingryu.baekmookback.security

import com.devingryu.baekmookback.dto.BaseResponse
import com.devingryu.baekmookback.dto.BaseResponse.Companion.toResponse
import com.devingryu.baekmookback.dto.BaseResponseCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthEntryPoint : AuthenticationEntryPoint {

    private val mapper = ObjectMapper()
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        val code = BaseResponseCode.ACCESS_TOKEN_INVALID
        val body = code.toResponse()

        response?.apply {
            status = code.status.value()
            contentType = "application/json"
            characterEncoding = "UTF-8"
            writer.write(mapper.writeValueAsString(body))
        }
    }
}