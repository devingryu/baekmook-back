package com.devingryu.baekmookback.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
class User(
    @Id
    val id: Long,
    @Column(nullable = false, unique = true)
    private var email: String,
    @Column(nullable = false)
    private var password: String,
    @Column(nullable = false)
    var name: String,
): UserDetails {

    @field:CreationTimestamp
    lateinit var createdDate: LocalDateTime

    @Column(nullable = false)
    private val enabled: Boolean = true

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE])
    val authorities: Set<UserAuthority> = hashSetOf()
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val out = ArrayList<GrantedAuthority>()
        authorities.forEach {
            out.add(SimpleGrantedAuthority(it.authority.name))
        }
        return out
    }

    fun setPassword(value: String) {
        this.password = value
    }
    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled
}

@Entity
class UserAuthority(
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne
    @JoinColumn(name = "authority_id")
    val authority: Authority
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

@Entity
class Authority(

    @Column(nullable = false, unique = true)
    val name: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    // Member가 삭제되어 MemberAuthority가 삭제되어도 Authority까지 전파되지 않음
    @OneToMany(mappedBy = "authority", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val member: List<UserAuthority> = ArrayList()
}