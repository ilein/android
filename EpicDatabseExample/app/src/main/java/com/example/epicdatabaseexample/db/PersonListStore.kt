package com.example.epicdatabaseexample.db

import com.example.epicdatabaseexample.db.person.PersonEntity

object PersonListStore {

    val personList = listOf(
        PersonEntity(title = "Павел"),
        PersonEntity(title = "Кристина"),
        PersonEntity(title = "Николай"),
    )
}