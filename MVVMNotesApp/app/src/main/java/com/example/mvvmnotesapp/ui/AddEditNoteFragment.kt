package com.example.mvvmnotesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mvvmnotesapp.R
import com.example.mvvmnotesapp.data.entity.Note
import com.example.mvvmnotesapp.databinding.FragmentAddEditNoteBinding
import com.example.mvvmnotesapp.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<NoteViewModel>()
        val binding = FragmentAddEditNoteBinding.bind(requireView())
        val args: AddEditNoteFragmentArgs by navArgs()

        val note = args.note

        binding.apply {
            if (note != null) {
                // Editing an existing note
                textView.setText(note.title)
                textView2.setText(note.content)
                floatingActionButton.setOnClickListener {
                    val title = textView.text.toString()
                    val content = textView2.text.toString()
                    val updatedNote = note.copy(
                        title = title,
                        content = content,
                        date = System.currentTimeMillis()
                    )
                    viewModel.updateNote(updatedNote)
                }
            } else {
                // Adding a new note
                floatingActionButton.setOnClickListener {
                    val title = textView.text.toString()
                    val content = textView2.text.toString()
                    if (title.isNotBlank() && content.isNotBlank()) {
                        val newNote = Note(
                            title = title,
                            content = content,
                            date = System.currentTimeMillis()
                        )
                        viewModel.insertNote(newNote)
                    } else {
                        Snackbar.make(requireView(), "Title or content cannot be empty", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notesEvent.collect { event ->
                if (event is NoteViewModel.NotesEvent.NavigateToNotesFragment) {
                    val action =
                        AddEditNoteFragmentDirections.actionAddEditNoteFragmentToNotesFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }
}
