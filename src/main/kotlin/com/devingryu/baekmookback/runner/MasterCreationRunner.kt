package com.devingryu.baekmookback.runner

import com.devingryu.baekmookback.entity.Authority
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.entity.UserAuthority
import com.devingryu.baekmookback.repository.AuthorityRepository
import com.devingryu.baekmookback.repository.UserAuthorityRepository
import com.devingryu.baekmookback.repository.UserRepository
import com.devingryu.baekmookback.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class MasterCreationRunner(
    private val memberRepository: UserRepository,
    private val authorityRepository: AuthorityRepository,
    private val memberAuthorityRepository: UserAuthorityRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${baekmook.master.username}")
    private val masterUsername: String?,
    @Value("\${baekmook.master.password}")
    private val masterPassword: String?,
) : ApplicationRunner {
    val log = logger()
    override fun run(args: ApplicationArguments?) {
        val masterRole = authorityRepository.findByName("ROLE_MASTER").let {
            if (it == null) {
                log.info("Master role ROLE_MASTER doesn't exist, creating...")
                val auth = Authority("ROLE_MASTER")
                authorityRepository.save(auth)
            } else it
        }

        val name = if (masterUsername != null) masterUsername else {
            log.warn("osmu.master.username NOT defined, using defalut value 'master'.")
            "master"
        }
        val pw = if (masterPassword != null) masterPassword else {
            log.warn("osmu.master.password NOT defined, using default value 'masterp@ssword!'.")
            "masterp@ssword!"
        }
        if (!memberRepository.existsByEmail(name)) {
            log.info("Master user $name not created, creating...")
            val member = User(0, name, passwordEncoder.encode(pw), "master")
            memberRepository.save(member)

            val memberAuthority = UserAuthority(member, masterRole)
            memberAuthorityRepository.save(memberAuthority)
        }
    }
}