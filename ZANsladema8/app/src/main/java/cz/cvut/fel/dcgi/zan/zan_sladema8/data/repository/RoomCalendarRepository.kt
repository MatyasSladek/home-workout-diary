package cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository

import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.Note
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.db.NoteDao
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.db.toEntity
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.db.toNote
import org.threeten.bp.LocalDate

class RoomCalendarRepository(
    private val noteDao: NoteDao
) : CalendarRepository {

    override suspend fun getNotes(): List<Note> {
        return noteDao.getAllNotes().map { it.toNote() }
    }

    override suspend fun addNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(noteId: String) {
        noteDao.deleteNoteById(noteId)
    }

    override suspend fun getNotesForDate(date: LocalDate): List<Note> {
        return noteDao.getNotesForDate(date).map { it.toNote() }
    }
}