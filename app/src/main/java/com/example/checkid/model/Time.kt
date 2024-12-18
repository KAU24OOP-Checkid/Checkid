package com.example.checkid.model

class Time {
    var hour: String = "00"

    var min: String = "00"

    var sec: String = "00"

    val sumTime: String
        get() = "$hour:$min:$sec"

    constructor(hour: String, min: String, sec: String) {
        this.hour = hour.padStart(2, '0')
        this.min = min.padStart(2, '0')
        this.sec = sec.padStart(2, '0')
    }

    constructor(sumTime: String)  {
        splitTime(sumTime)
    }

    // sumTime을 hour, min, sec로 분리하는 메서드
    fun splitTime(sumTime: String) {
        val parts = sumTime.split(":")
        if (parts.size == 3) {
            hour = parts[0].padStart(2, '0')
            min = parts[1].padStart(2, '0')
            sec = parts[2].padStart(2, '0')
        } else {
            throw IllegalArgumentException("Invalid time format. Expected format: HH:mm:ss")
        }
    }
}