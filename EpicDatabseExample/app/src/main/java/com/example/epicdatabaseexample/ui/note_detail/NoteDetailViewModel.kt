package com.example.epicdatabaseexample.ui.note_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epicdatabaseexample.db.note.NoteDao
import com.example.epicdatabaseexample.db.note.NoteEntity
import com.example.epicdatabaseexample.db.person.PersonEntity
import com.example.epicdatabaseexample.ui.CommandsLiveData
import com.example.epicdatabaseexample.ui.NavigationCommand
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NoteDetailViewModel constructor(
    private val noteDao: NoteDao,
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val noteDao: NoteDao,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NoteDetailViewModel(noteDao) as T
        }
    }

    val state = MutableLiveData<NoteDetailViewState>()
    val navigationCommands = CommandsLiveData<NavigationCommand>()

    private val compositeDisposable = CompositeDisposable()

    fun init(noteId: Int?) {
        if (noteId == null) {
            val initialState = NoteDetailViewState(noteId = noteId)
            state.postValue(initialState)
        } else {
            compositeDisposable.add(
                noteDao.getNoteWithPerson(noteId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { noteWithPerson ->
                        val initialState = NoteDetailViewState(
                            noteId = noteId,
                            title = noteWithPerson.note.title,
                            description = noteWithPerson.note.description,
                            personName = noteWithPerson.person?.title ?:
                            PersonEntity.DEFAULT_PERSON_NAME
                        )
                        state.postValue(initialState)
                    }
            )
        }
    }

    fun onTitleChange(title: String) {
        if (state.value!!.title != title) {
            state.postValue(state.value!!.copy(
                title = title
            ))
        }
    }

    fun onDescriptionChange(description: String) {
        if (state.value!!.description != description) {
            state.postValue(state.value!!.copy(
                description = description
            ))
        }
    }

    fun onCheckedPersonChanged(personName: String) {
        if (state.value!!.personName != personName) {
            state.postValue(state.value!!.copy(
                personName = personName
            ))
        }
    }

    fun onDeleteNoteClick() {
        state.value?.noteId?.let {
            compositeDisposable.add(
                noteDao.deleteNote(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        navigationCommands.onNext(NavigationCommand.Back)
                    }
            )
        }
    }

    fun onSaveNoteClick() {
        state.value?.let {
            compositeDisposable.add(
                if (it.noteId == null) {
                    noteDao.insertNote(
                        NoteEntity(
                            title = it.title,
                            description = it.description,
                            personName = it.personName
                        )
                    )
                } else {
                    noteDao.insertNote(
                        NoteEntity(
                            id = it.noteId,
                            title = it.title,
                            description = it.description,
                            personName = it.personName
                        )
                    )
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        navigationCommands.onNext(NavigationCommand.Back)
                    }
            )
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}