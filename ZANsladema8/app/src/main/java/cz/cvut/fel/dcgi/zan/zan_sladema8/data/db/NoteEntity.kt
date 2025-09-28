package cz.cvut.fel.dcgi.zan.zan_sladema8.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.Note
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Entity(tableName = "notes")
@TypeConverters(DateConverters::class)
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val date: LocalDate,
    val title: String,
    val content: String,
    val timestamp: Long
)

class DateConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, formatter) }
    }

    @TypeConverter
    fun fromWorkoutType(workoutType: cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutType?): String? {
        return workoutType?.name
    }

    @TypeConverter
    fun toWorkoutType(workoutTypeName: String?): cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutType? {
        return workoutTypeName?.let {
            try {
                cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutType.valueOf(it)
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = this.id,
        date = this.date,
        title = this.title,
        content = this.content,
        timestamp = this.timestamp
    )
}

fun NoteEntity.toNote(): Note {
    return Note(
        id = this.id,
        date = this.date,
        title = this.title,
        content = this.content,
        timestamp = this.timestamp
    )
}