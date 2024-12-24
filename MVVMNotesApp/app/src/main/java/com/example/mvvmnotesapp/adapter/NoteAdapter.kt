package com.example.mvvmnotesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnotesapp.data.entity.Note
import com.example.mvvmnotesapp.databinding.ItemNotesBinding
import java.text.SimpleDateFormat

class NoteAdapter(
    private val mNotes:List<Note>,
    private val listener:OnClickListener
):RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    interface OnClickListener{
        fun onClickListener(note: Note)
        fun onLongClickListener(note: Note)
    }
    inner class ViewHolder(private val binding: ItemNotesBinding):RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val note = mNotes[position]
                        listener.onClickListener(note)
                    }
                }
                root.setOnLongClickListener{
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val note = mNotes[position]
                        listener.onClickListener(note)
                    }
                    true
                }

            }
        }
        fun bind(note: Note){
            binding.apply {
                cardTitle.text=note.title
                cardContent.text=note.content
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                cardDate.text=formatter.format(note.date)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ItemNotesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        with(mNotes[position]){
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int {
        return mNotes.size
    }
}