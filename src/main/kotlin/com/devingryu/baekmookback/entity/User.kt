package com.devingryu.baekmookback.entity

import com.devingryu.baekmookback.entity.lecture.LectureUser
import com.devingryu.baekmookback.entity.lecture.Post
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
class User(
    @Column(nullable = false, unique = true)
    var studentId: String,
    @Column(nullable = false, unique = true)
    private var email: String,
    @Column(nullable = false)
    private var password: String,
    @Column(nullable = false)
    var name: String,
) : UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @field:CreationTimestamp
    lateinit var createdDate: LocalDateTime

    @Column(nullable = false)
    private val enabled: Boolean = true

    @OneToMany(
        mappedBy = "user",
        fetch = FetchType.EAGER,
    )
    private val authorities: Set<UserAuthority> = hashSetOf()

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val lectures: List<LectureUser> = listOf()

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

    override fun equals(other: Any?): Boolean = other is User && other.id == id
    override fun hashCode(): Int {
        var result = studentId.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + createdDate.hashCode()
        result = 31 * result + enabled.hashCode()
        result = 31 * result + authorities.hashCode()
        result = 31 * result + lectures.hashCode()
        return result
    }
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

    @OneToMany(mappedBy = "authority", fetch = FetchType.LAZY)
    val member: List<UserAuthority> = ArrayList()
}