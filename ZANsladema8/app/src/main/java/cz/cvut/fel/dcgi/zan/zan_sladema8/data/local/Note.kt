package cz.cvut.fel.dcgi.zan.zan_sladema8.data.local

import org.threeten.bp.LocalDate

data class Note(
    val id: String = java.util.UUID.randomUUID().toString(),
    val date: LocalDate,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)