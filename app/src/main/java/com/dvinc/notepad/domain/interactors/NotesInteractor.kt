/*
 * Copyright (c) 2018 by Denis Verentsov
 * Date: 5/2/2018
 * Email: decsent@yandex.ru
 * All rights reserved.
 */

package com.dvinc.notepad.domain.interactors

import com.dvinc.notepad.common.extension.applyIoToMainSchedulers
import com.dvinc.notepad.data.database.entity.NoteEntity
import com.dvinc.notepad.domain.mappers.NoteMapper
import com.dvinc.notepad.domain.model.Note
import com.dvinc.notepad.domain.model.NoteMarker
import com.dvinc.notepad.domain.repositories.MarkersRepository
import com.dvinc.notepad.domain.repositories.NotesRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/*
 * This interactor is under construction.
 * The main idea is obtain all needed data from different sources and prepare it for presenter and view.
 */
class NotesInteractor
@Inject constructor(
        private val notesRepository: NotesRepository,
        private val markersRepository: MarkersRepository,
        private val noteMapper: NoteMapper
){

    fun getNoteById(id: Long): Single<Note> {
        return notesRepository.getNoteById(id)
                .zipWith(markersRepository.getMarkers(), BiFunction<NoteEntity, List<NoteMarker>, Note>
                { entity, markers ->
                    noteMapper.mapEntityToNote(entity, markers)
                })
                .applyIoToMainSchedulers()
    }

    fun getNoteMarkers(): Single<List<NoteMarker>> {
        return markersRepository.getMarkers()
    }

    fun addNoteInfo(
            noteId: Long?,
            name: String,
            content: String,
            time: Long,
            markerId: Int
    ): Completable {
        return if (noteId != null && noteId != 0L) {
            notesRepository.updateNote(
                    noteMapper.createEntity(name, content, time, markerId, noteId))
                    .applyIoToMainSchedulers()
        } else {
            notesRepository.addNote(
                    noteMapper.createEntity(name, content, time, markerId))
                    .applyIoToMainSchedulers()
        }
    }
}
