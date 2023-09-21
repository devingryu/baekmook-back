package com.devingryu.baekmookback.controller

import com.devingryu.baekmookback.dto.BaseResponse
import com.devingryu.baekmookback.dto.BaseResponse.Companion.toResponse
import com.devingryu.baekmookback.dto.BaseResponseCode
import com.devingryu.baekmookback.dto.request.UserRegisterRequestDto
import com.devingryu.baekmookback.dto.response.MeResponseDto
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.service.AuthorityService
import com.devingryu.baekmookback.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService, private val authorityService: AuthorityService) {

    @PostMapping("/api/register")
    fun register(@RequestBody req: UserRegisterRequestDto): ResponseEntity<MeResponseDto> {
        val user = with(req) { User(id, email, password, name) }
        val created = userService.createUser(user)
        val role = authorityService.getRole(if (req.isLecturer) "LECTURER" else "STUDENT")
        authorityService.addAuthority(created, role)

        val final = userService.findUserByEmail(user.name)
        return ResponseEntity.ok(MeResponseDto.fromUser(final))
    }
}