/*
 * Copyright (c) 2018 by Denis Verentsov (decsent@Yandex.ru)
 * All rights reserved.
 */

package com.dvinc.notepad.ui.note

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.dvinc.notepad.NotepadApplication
import com.dvinc.notepad.R
import com.dvinc.notepad.domain.model.Note
import com.dvinc.notepad.domain.model.NoteMarker
import com.dvinc.notepad.ui.adapters.NoteMarkersAdapter
import com.dvinc.notepad.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_note.*
import javax.inject.Inject
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.dvinc.notepad.common.visible

class NoteFragment : BaseFragment(), NoteView {

    @Inject lateinit var presenter: NotePresenter

    private val noteId: Long? get() = arguments?.getLong(NOTE_ID, 0)

    override fun getFragmentLayoutId(): Int = R.layout.fragment_note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context?.applicationContext as NotepadApplication).appComponent.inject(this)

        setupToolbar()
        setupNoteButton()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.initView(noteId)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
        presenter.detachView()
    }

    override fun closeScreen() {
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

    override fun setEditMode(isEditMode: Boolean) {
        groupNote.visible(true)
        if (isEditMode) {
            btAddNote.setText(R.string.note_edit)
        } else {
            btAddNote.setText(R.string.note_add)
        }
    }

    override fun showNote(note: Note) {
        etNoteName.setText(note.name)
        etNoteContent.setText(note.content)
        spNoteType.setSelection(note.markerId)
    }

    override fun setNoteNameEmptyError(isVisible: Boolean) {
        if (isVisible) {
            etNoteName.error = context?.getString(R.string.message_empty_note_name)
        } else {
            etNoteName.error = null
        }
    }

    private fun setupToolbar() {
        toolbarNote.setNavigationOnClickListener {
            activity?.let {
                findNavController(it, R.id.nav_host_fragment).navigateUp()
            }
        }
    }

    private fun setupNoteButton() {
        btAddNote.setOnClickListener {
            val name = etNoteName.text.toString()
            val content = etNoteContent.text.toString()
            val currentTime = System.currentTimeMillis()
            val markerId = spNoteType.selectedItemId.toInt()

            presenter.onClickNoteButton(noteId, name, content, currentTime, markerId)
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
