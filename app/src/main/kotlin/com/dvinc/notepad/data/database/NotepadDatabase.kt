/*
 * Copyright (c) 2018 by Denis Verentsov (decsent@Yandex.ru)
 * All rights reserved.
 */

package com.dvinc.notepad.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dvinc.notepad.data.database.dao.note.NoteDao
import com.dvinc.notepad.data.database.entity.note.NoteEntity

@Database(entities = arrayOf(NoteEntity::class), version = 1)
abstract class NotepadDatabase : RoomDatabase() {
    abstract fun notesDao(): NoteDao
}
