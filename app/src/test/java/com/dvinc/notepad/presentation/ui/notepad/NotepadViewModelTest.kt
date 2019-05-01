/*
 * Copyright (c) 2019 by Denis Verentsov (decsent@yandex.ru)
 * All rights reserved.
 */

package com.dvinc.notepad.presentation.ui.notepad

import com.dvinc.notepad.domain.usecase.notepad.NotepadUseCase
import com.dvinc.notepad.presentation.mapper.NotePresentationMapper
import com.dvinc.notepad.presentation.model.MarkerTypeUi
import com.dvinc.notepad.presentation.model.NoteUi
import com.dvinc.notepad.presentation.ui.ViewModelTest
import com.dvinc.notepad.presentation.ui.base.ViewCommand
import io.reactivex.Flowable
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

class NotepadViewModelTest : ViewModelTest() {

    @Mock
    private lateinit var notepadUseCase: NotepadUseCase

    @Mock
    private lateinit var noteMapper: NotePresentationMapper

    private lateinit var notepadViewModel: NotepadViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(notepadUseCase.getNotes()).thenReturn(Flowable.just(emptyList()))

        notepadViewModel = NotepadViewModel(notepadUseCase, noteMapper)
    }

    @Test
    fun `when click on note then go to Note screen by view command`() {
        // Given
        val noteUi = NoteUi(100L, "test", "content", "21.12", MarkerTypeUi.CRITICAL)

        // When
        notepadViewModel.onNoteItemClick(noteUi)

        // Then
        val expectedListWithSingleCommand = LinkedList<ViewCommand>(
            listOf(ViewCommand.OpenNoteScreen(noteUi.id))
        )
        assertThat(notepadViewModel.commands.value!!, `is`(equalTo(expectedListWithSingleCommand)))
    }
}
