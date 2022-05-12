package com.example.coroutinetestviewmodel.ui.main

class BackendApi {

    fun getCities(): List<String> {
        Thread.sleep(4000)
        return listOf("Moscow", "Samara", "Saratov", "Kazan")
    }
}