/*
 * Copyright (c) 2018 by Denis Verentsov (decsent@Yandex.ru)
 * All rights reserved.
 */

package com.dvinc.notepad.ui.note

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.dvinc.notepad.App
import com.dvinc.notepad.R
import com.dvinc.notepad.common.visible
import com.dvinc.notepad.domain.model.Note
import com.dvinc.notepad.domain.model.NoteMarker
import com.dvinc.notepad.ui.adapters.NoteMarkersAdapter
import com.dvinc.notepad.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_note.*
import javax.inject.Inject
import android.content.Context
import android.view.inputmethod.InputMethodManager

class NoteFragment : BaseFragment(), NoteView {

    @Inject lateinit var presenter: NotePresenter

    private val noteId: Long? get() = arguments?.getLong(NOTE_ID, 0)

    override fun getFragmentLayoutId(): Int = R.layout.fragment_note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context?.applicationContext as App).appComponent.inject(this)

        setupAddNoteButton()
        setupEditNoteButton()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.initView(noteId)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun closeScreen() {
        hideKeyboard()
        activity?.let {
            findNavController(it, R.id.nav_host_fragment).navigateUp()
        }
    }

    override fun showMarkers(markers: List<NoteMarker>) {
        val adapter = NoteMarkersAdapter(context, R.layout.item_note_marker, markers)
        spNoteType.adapter = adapter
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(message: String) {
        //Temporarily empty
    }

    override fun setNoteButton(isEditMode: Boolean) {
        if (isEditMode) {
            btAddNote.visible(!isEditMode)
            btEditNote.visible(isEditMode)
        }
    }

    override fun showNote(note: Note) {
        etNoteName.setText(note.name)
        etNoteContent.setText(note.content)
        spNoteType.setSelection(note.markerId)
    }

    private fun setupAddNoteButton() {
        btAddNote.setOnClickListener {
            val name = etNoteName.text.toString()
            val content = etNoteContent.text.toString()
            val currentTime = System.currentTimeMillis()
            val markerId = spNoteType.selectedItemId.toInt()

            presenter.saveNewNote(name, content, currentTime, markerId)
        }
    }

    private fun setupEditNoteButton() {
        btEditNote.setOnClickListener {
            val name = etNoteName.text.toString()
            val content = etNoteContent.text.toString()
            val currentTime = System.currentTimeMillis()
            val markerId = spNoteType.selectedItemId.toInt()

            noteId?.let {
                presenter.editNote(it, name, content, currentTime, markerId)
            }
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        const val NOTE_ID = "noteId"
    }
}
