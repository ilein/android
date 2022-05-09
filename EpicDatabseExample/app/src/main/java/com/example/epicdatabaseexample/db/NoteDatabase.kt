package com.example.epicdatabaseexample.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.epicdatabaseexample.db.person.PersonDao
import com.example.epicdatabaseexample.db.person.PersonEntity
import com.example.epicdatabaseexample.db.note.NoteDao
import com.example.epicdatabaseexample.db.note.NoteEntity

@Database(
    version = 2,
    exportSchema = false,
    entities = [
        NoteEntity::class,
        PersonEntity::class,
    ]
)
abstract class NoteDatabase : RoomDatabase() {

    companion object {

        private const val databaseName = "note_database"
        const val NOTE_TABLE = "note_table"
        const val PERSON_TABLE = "person_table"

        @Volatile
        private var instance: NoteDatabase? = null

        fun getInstance(application: Application): NoteDatabase {
            synchronized(this) {
                return instance ?: instance ?: createDatabaseInstance(application)
            }
        }

        private fun migrationList() = arrayOf(
            object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE $PERSON_TABLE (" +
                            "${PersonEntity.COLUMN_NAME} TEXT NOT NULL," +
                            "PRIMARY KEY(${PersonEntity.COLUMN_NAME})" +
                            ")")
                    database.execSQL("ALTER TABLE $NOTE_TABLE " +
                            "ADD COLUMN ${NoteEntity.COLUMN_PERSON_NAME} TEXT NOT NULL " +
                            "DEFAULT ('${PersonEntity.DEFAULT_PERSON_NAME}')")
                }
            },
            // TODO Добавить миграцию.
        )

        private fun createDatabaseInstance(application: Application): NoteDatabase {
            return Room
                .databaseBuilder(
                    application,
                    NoteDatabase::class.java,
                    databaseName
                )
                .addMigrations(*migrationList())
                .build()
        }
    }

    abstract fun noteDao(): NoteDao

    abstract fun personDao(): PersonDao
}