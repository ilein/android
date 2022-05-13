package com.example.epicdatabaseexample.db.note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.epicdatabaseexample.db.NoteDatabase
import com.example.epicdatabaseexample.db.person.PersonEntity

@Entity(tableName = NoteDatabase.NOTE_TABLE)
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Int = 0,

    @ColumnInfo(name = COLUMN_TITLE)
    var title: String = "",

    @ColumnInfo(name = COLUMN_DESCRIPTION)
    var description: String = "",

    @ColumnInfo(name = COLUMN_PERSON_NAME)
    var personName: String = PersonEntity.DEFAULT_PERSON_NAME,

    // TODO Нужно добавить нове поле isCompleted типа Boolean.
    //  В SQLite не поддерживается Boolean, но в Room его можно использовать.
    //  Поэтому поле делаем именно Boolean, а при добавлении миграции - нужно будет погуглить,
    //  как обойти это ограничение.
    @ColumnInfo(name = COLUMN_IS_COMPLETED)
    var isCompleted: Boolean = false

) {

    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "descriptions"
        const val COLUMN_PERSON_NAME = "person_name"
        const val COLUMN_IS_COMPLETED = "is_completed"
    }
}