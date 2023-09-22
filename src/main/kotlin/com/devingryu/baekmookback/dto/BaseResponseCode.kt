package com.devingryu.baekmookback.dto

import org.springframework.http.HttpStatus

enum class BaseResponseCode(val status: HttpStatus, val message: String, val messageTranslated: String, val clazz: String) {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request", "잘못된 요청입니다.", "bad_request_exception"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Wrong password. Please try again.", "잘못된 비밀번호입니다. 다시 입력해주세요.", "wrong_password_exception"),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "Email already in use.", "이미 사용 중인 이메일입니다.", "duplicate_email_exception"),
    DUPLICATE_STUDENT_ID(HttpStatus.BAD_REQUEST, "Student ID is already in use.", "이미 사용 중인 학번입니다.", "duplicate_student_id_exception"),

    ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Login token is invalid", "로그인 정보가 유효하지 않습니다.", "access_token_invalid_exception"),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Login refresh token expired", "로그인 유지 정보가 만료되었습니다.", "refresh_token_invalid_exception"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Could not find user.", "사용자를 찾을 수 없습니다.", "user_not_found_exception"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "File not found", "요청하신 파일을 찾을 수 없습니다.", "file_not_found_exception"),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "Access Denied", "접근 권한이 없습니다.", "access_denied_exception"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "서버 내부 오류", "internal_server_error"),
    OK(HttpStatus.OK, "Request success", "요청 성공", "")
}