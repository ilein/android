package com.example.epicdatabaseexample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.epicdatabaseexample.ui.note_list.NoteListFragment
import com.example.epicdatabaseexample.R
import com.example.epicdatabaseexample.db.NoteDatabase
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.Factory(
            personDao = NoteDatabase.getInstance(application).personDao()
        )
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            viewModel.init()
            addListFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun addListFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fragment_container_view,
                NoteListFragment.newInstance(),
            )
            .commit()
    }
}