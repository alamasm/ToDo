package com.example.pektu.lifecounter.Controller

class ControllerSingleton {
    companion object {
        lateinit var controller: Controller
        var inited = false

        fun init(controller: Controller) {
            this.controller = controller
            inited = true
        }
    }
}