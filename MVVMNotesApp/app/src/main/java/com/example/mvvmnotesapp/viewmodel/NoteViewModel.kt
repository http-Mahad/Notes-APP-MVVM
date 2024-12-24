package com.example.mvvmnotesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnotesapp.NotesApplication
import com.example.mvvmnotesapp.data.dao.NoteDao
import com.example.mvvmnotesapp.data.entity.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteDao: NoteDao) : ViewModel() {
    val notes = noteDao.getAllNotes()
    val notesChannel = Channel<NotesEvent>()
    val notesEvent = notesChannel.receiveAsFlow()

    fun insertNote(note:Note)= viewModelScope.launch {
        noteDao.insertNote(note)
        notesChannel.send(NotesEvent.NavigateToNotesFragment)
    }
    fun updateNote(note:Note)= viewModelScope.launch {
        noteDao.updateNote(note)
        notesChannel.send(NotesEvent.NavigateToNotesFragment)

    }
    fun deleteNote(note: Note) = viewModelScope.launch {
        noteDao.deleteNote(note)
        notesChannel.send(NotesEvent.showUndoSnackBar("Note Deleted",note))
    }
    sealed class NotesEvent{
        data class showUndoSnackBar(val msg:String, val note: Note):NotesEvent()
        object NavigateToNotesFragment:NotesEvent()
    }
}