package com.example.checkid.model

import ChildUser
import ParentUser
import User

object UserRepository {
    var users:MutableList<User> = mutableListOf()

    init {
        val a = ChildUser("1", "1")
        val b = ParentUser("2", "2")

        a.parent = b
        b.child = a

        users.add(a)
        users.add(b)
    }
}