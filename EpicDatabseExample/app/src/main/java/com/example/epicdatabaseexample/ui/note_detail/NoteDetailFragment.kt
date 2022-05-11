package com.example.epicdatabaseexample.ui.note_detail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.epicdatabaseexample.R
import com.example.epicdatabaseexample.db.NoteDatabase
import com.example.epicdatabaseexample.db.PersonListStore
import com.example.epicdatabaseexample.db.person.PersonEntity
import com.example.epicdatabaseexample.ui.NavigationCommand
import com.example.epicdatabaseexample.ui.observe

class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {

    companion object {

        private const val NOTE_ID_ARG = "note_id_arg"
        private const val NOTE_ID_ARG_DEFAULT_VALUE = -1

        fun newInstance(noteId: Int?): NoteDetailFragment {
            return NoteDetailFragment().apply {
                arguments = bundleOf(
                    NOTE_ID_ARG to noteId
                )
            }
        }
    }

    private val viewModel: NoteDetailViewModel by viewModels {
        NoteDetailViewModel.Factory(
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
            viewModel.init(getNoteIdFromArgs())
        }

        view.findViewById<Button>(R.id.title_edit_text)
            .doOnTextChanged { text, _, _, _ ->
                viewModel.onTitleChange(text.toString())
            }

        view.findViewById<Button>(R.id.description_edit_text)
            .doOnTextChanged { text, _, _, _ ->
                viewModel.onDescriptionChange(text.toString())
            }

        view.findViewById<RadioGroup>(R.id.person_radio_group)
            .setOnCheckedChangeListener { _, _ ->
                val checkedPerson = view.getCheckedPerson()
                viewModel.onCheckedPersonChanged(
                    checkedPerson?.title ?: PersonEntity.DEFAULT_PERSON_NAME
                )
            }

        view.findViewById<Button>(R.id.save_note_button).setOnClickListener {
            viewModel.onSaveNoteClick()
        }

        view.findViewById<Button>(R.id.delete_note_button).setOnClickListener {
            viewModel.onDeleteNoteClick()
        }
    }

    private fun handleViewState(viewStata: NoteDetailViewState) {
        view?.let { rootView ->
            rootView.findViewById<EditText>(R.id.title_edit_text).let {
                it.setText(viewStata.title)
                it.setSelection(it.text.length)
            }
            rootView.findViewById<EditText>(R.id.description_edit_text).let {
                it.setText(viewStata.description)
                it.setSelection(it.text.length)
            }
            rootView.findViewById<Button>(R.id.delete_note_button).isEnabled =
                viewStata.noteId != null
            rootView.findViewById<RadioGroup>(R.id.person_radio_group)
                .check(getRadioIdByPersonName(viewStata.personName))
        }
    }

    private fun handleNavigationCommand(navigationCommand: NavigationCommand) {
        if (navigationCommand is NavigationCommand.Back) {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun getNoteIdFromArgs(): Int? {
        val noteId = arguments?.getInt(NOTE_ID_ARG, NOTE_ID_ARG_DEFAULT_VALUE)
        val isNewNote = noteId == NOTE_ID_ARG_DEFAULT_VALUE
        return if (isNewNote) {
            null
        } else {
            noteId
        }
    }

    private fun getRadioIdByPersonName(personName: String?): Int {
        val personList = PersonListStore.personList
        return personName?.let {
            when (it) {
                personList[0].title -> R.id.radio_button_person_1
                personList[1].title -> R.id.radio_button_person_2
                personList[2].title -> R.id.radio_button_person_3
                else -> R.id.radio_button_default_person
            }
        } ?: R.id.radio_button_default_person
    }

    private fun View.getCheckedPerson(): PersonEntity? {
        val checkedId = findViewById<RadioGroup>(R.id.person_radio_group).checkedRadioButtonId
        val personList = PersonListStore.personList
        return when (checkedId) {
            R.id.radio_button_person_1 -> personList[0]
            R.id.radio_button_person_2 -> personList[1]
            R.id.radio_button_person_3 -> personList[2]
            else -> null
        }
    }
}