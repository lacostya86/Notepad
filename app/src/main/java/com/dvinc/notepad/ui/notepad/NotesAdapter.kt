/*
 * Copyright (c) 2018 by Denis Verentsov (decsent@Yandex.ru)
 * All rights reserved.
 */

package com.dvinc.notepad.ui.notepad

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dvinc.notepad.R
import com.dvinc.notepad.data.database.entity.Note

class NotesAdapter(private var notes: List<Note>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.name?.text = notes[position].name
        holder?.content?.text = notes[position].content
        holder?.updateTime?.text = notes[position].updateTime.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tv_note_name)
        val content = itemView.findViewById<TextView>(R.id.tv_note_content)
        val updateTime = itemView.findViewById<TextView>(R.id.tv_note_updated_time)
    }

    fun setNotes(notes : List<Note>) {
        this.notes = notes
    }
}