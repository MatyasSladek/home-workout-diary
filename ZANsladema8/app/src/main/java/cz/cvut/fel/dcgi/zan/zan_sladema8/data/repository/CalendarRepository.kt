package cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository

import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.Note
import org.threeten.bp.LocalDate

interface CalendarRepository {
    suspend fun getNotes(): List<Note>
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(noteId: String)
    suspend fun getNotesForDate(date: LocalDate): List<Note>
}