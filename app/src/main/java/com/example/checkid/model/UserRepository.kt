package com.example.checkid.model

import User

object UserRepository {
    var users:MutableList<User> = mutableListOf()

    init {
        val a = User("1", "1234", "2")
        val b = User("2", "1234", "1")

        users.add(a)
        users.add(b)
    }

    fun findByIdPw(id: String, pw: String): User? {
        return users.find {it.id == id && it.pw == pw}
    }
}