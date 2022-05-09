package com.example.epicdatabaseexample.ui.note_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.epicdatabaseexample.db.note.NoteDao
import com.example.epicdatabaseexample.db.note.NoteEntity
import com.example.epicdatabaseexample.ui.CommandsLiveData
import com.example.epicdatabaseexample.ui.NavigationCommand
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NoteListViewModel constructor(
    private val noteDao: NoteDao,
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val noteDao: NoteDao,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NoteListViewModel(noteDao) as T
        }
    }

    val state = MutableLiveData<NoteListViewState>()
    val navigationCommands = CommandsLiveData<NavigationCommand>()

    private val compositeDisposable = CompositeDisposable()

    fun init() {
        compositeDisposable.add(
            noteDao.observeNoteList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { noteList ->
                    state.postValue(NoteListViewState(
                        noteList = noteList
                    ))
                }
        )
    }

    fun onClearNoteListClick() {
        compositeDisposable.add(
            noteDao.deleteAllNote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    state.postValue(state.value!!.copy(
                        noteList = emptyList()
                    ))
                }
        )
    }

    fun onCreateNoteClick() {
        navigationCommands.onNext(NavigationCommand.ToNoteDetails(noteId = null))
    }

    fun onNoteClick(noteId: Int) {
        navigationCommands.onNext(NavigationCommand.ToNoteDetails(noteId = noteId))
    }

    fun onIsCompletedClick(note: NoteEntity) {
        // TODO Добавить вызов noteDao для обновления элемента в таблице.
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}