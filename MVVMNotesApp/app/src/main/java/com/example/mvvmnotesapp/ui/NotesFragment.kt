package com.example.mvvmnotesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvmnotesapp.R
import com.example.mvvmnotesapp.adapter.NoteAdapter
import com.example.mvvmnotesapp.data.entity.Note
import com.example.mvvmnotesapp.databinding.FragmentNotesBinding
import com.example.mvvmnotesapp.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes),NoteAdapter.OnClickListener {
    val viewModel by viewModels<NoteViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNotesBinding.bind(requireView())
        binding.apply {
            notesRv.layoutManager= GridLayoutManager(context,2)
            notesRv.setHasFixedSize(true)

            btnAddNew.setOnClickListener {
                val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(null)
                findNavController().navigate(action)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notes.collect{notes ->
                    val adapter = NoteAdapter(notes,this@NotesFragment)
                    notesRv.adapter=adapter


                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notesEvent.collect{event ->
                    if(event is NoteViewModel.NotesEvent.showUndoSnackBar){
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_LONG).setAction("Undo"){
                            viewModel.insertNote(event.note)
                        }.show()
                    }
                }
            }
        }
    }

    override fun onClickListener(note: Note) {
        val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(note)
        findNavController().navigate(action)
    }

    override fun onLongClickListener(note: Note) {
        viewModel.deleteNote(note)
    }

}