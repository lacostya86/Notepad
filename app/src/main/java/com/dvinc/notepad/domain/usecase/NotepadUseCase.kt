/*
 * Copyright (c) 2018 by Denis Verentsov
 * Date: 6/8/2018
 * Email: decsent@yandex.ru
 * All rights reserved.
 */

package com.dvinc.notepad.domain.usecase

import com.dvinc.notepad.common.extension.applyIoToMainSchedulers
import com.dvinc.notepad.domain.model.Note
import com.dvinc.notepad.domain.repository.NoteRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class NotepadUseCase @Inject constructor(
        private val noteRepository: NoteRepository
) {

    fun getNotes(): Flowable<List<Note>> {
        return noteRepository.getNotes()
                .applyIoToMainSchedulers()
    }

    fun deleteNote(noteId: Int): Completable {
        return noteRepository.deleteNoteById(noteId)
                .applyIoToMainSchedulers()
    }
}