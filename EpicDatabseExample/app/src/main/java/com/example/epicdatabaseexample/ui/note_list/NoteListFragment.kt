package com.example.epicdatabaseexample.ui.note_list

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.epicdatabaseexample.ui.note_detail.NoteDetailFragment
import com.example.epicdatabaseexample.R
import com.example.epicdatabaseexample.db.NoteDatabase
import com.example.epicdatabaseexample.ui.NavigationCommand
import com.example.epicdatabaseexample.ui.observe
import io.reactivex.disposables.CompositeDisposable

class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    companion object {

        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }

    private val compositeDisposable = CompositeDisposable()

    private val viewModel: NoteListViewModel by viewModels {
        NoteListViewModel.Factory(
            noteDao = NoteDatabase
                .getInstance(requireActivity().application)
                .noteDao()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.state, ::handleViewState)
        observe(viewModel.navigationCommands, ::handleNavigationCommand)

        if (savedInstanceState == null) {
            viewModel.init()
        }

        setupListAdapter(view)

        view.findViewById<Button>(R.id.create_note_button).setOnClickListener {
            viewModel.onCreateNoteClick()
        }
        view.findViewById<Button>(R.id.clear_button).setOnClickListener {
            viewModel.onClearNoteListClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun setupListAdapter(rootView: View) {
        context?.let { context ->
            rootView.findViewById<RecyclerView>(R.id.recycler_view).let { recycler ->
                recycler.layoutManager = LinearLayoutManager(context)
                recycler.adapter = NoteListAdapter(
                    onNoteClick = { clickedNoteId ->
                        viewModel.onNoteClick(noteId = clickedNoteId)
                    },
                    onIsCompletedClick = { clickedNote ->
                        viewModel.onIsCompletedClick(note = clickedNote)
                    }
                )
            }
        }
    }

    private fun handleViewState(viewState: NoteListViewState) {
        view?.findViewById<RecyclerView>(R.id.recycler_view)?.let { recyclerView ->
            (recyclerView.adapter as NoteListAdapter).submitList(viewState.noteList)
        }
    }

    private fun handleNavigationCommand(navigationCommand: NavigationCommand) {
        if (navigationCommand is NavigationCommand.ToNoteDetails) {
            openNoteDetailScreen(navigationCommand.noteId)
        }
    }

    private fun openNoteDetailScreen(noteId: Int? = null) {
        activity?.let { activity ->
            val noteDetailFragment = NoteDetailFragment.newInstance(noteId)
            activity.supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(
                    R.id.fragment_container_view,
                    noteDetailFragment
                )
                .commit()
        }
    }
}