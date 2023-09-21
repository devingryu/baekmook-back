package com.devingryu.baekmookback.configuration

import com.devingryu.baekmookback.security.AuthorityEvaluator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class MethodSecurityConfig {
    companion object {
        @Bean
        fun methodSecurityExpressionHandler(): MethodSecurityExpressionHandler {
            val handler = DefaultMethodSecurityExpressionHandler()
            handler.setPermissionEvaluator(AuthorityEvaluator())
            return handler
        }
    }
}