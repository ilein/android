package com.example.epicdatabaseexample.ui

sealed class NavigationCommand {

    object Back: NavigationCommand()
    data class ToNoteDetails(val noteId: Int?): NavigationCommand()
}