package com.example.pektu.lifecounter.Model

class ModelSingleton {
    companion object {
        lateinit var model: Model
        lateinit var currentDate: DayDate
        var inited = false

        fun init(saver: Model) {
            this.model = saver
            inited = true
        }
    }
}