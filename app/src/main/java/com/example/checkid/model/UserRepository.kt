package com.example.checkid.model

import User
import ParentUser

object UserRepository {
    var users:MutableList<User> = mutableListOf()

    init {
        val kid = User("kid", "kid", "parent")
        val parent = User("parent", "parent", "kid")

        users.add(kid)
        users.add(parent)
    }

    fun findByIdPw(id: String, pw: String): User? {
        return users.find {it.id == id && it.pw == pw}
    }
}