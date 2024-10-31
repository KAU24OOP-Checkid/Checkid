package com.example.checkid.model

open class User {
    val id: String = ""
    var pw: String = ""
}

class ChildUser: User() {
    var Parent: ParentUser? = null
    var applicationList: MutableList<String>? = mutableListOf()
}

class ParentUser: User() {
    var Child: ChildUser? = null
}