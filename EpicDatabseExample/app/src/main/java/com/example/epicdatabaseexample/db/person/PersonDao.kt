package com.example.epicdatabaseexample.db.person

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
abstract class PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPersonList(personList: List<PersonEntity>): Completable

    @Query("SELECT * FROM person_table WHERE name = :name")
    abstract fun getPerson(name: String): Maybe<PersonEntity>

    @Query("SELECT * FROM person_table")
    abstract fun requirePersonList(): Single<List<PersonEntity>>
}