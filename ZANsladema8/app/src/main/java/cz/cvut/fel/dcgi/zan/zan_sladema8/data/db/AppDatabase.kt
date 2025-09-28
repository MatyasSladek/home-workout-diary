package cz.cvut.fel.dcgi.zan.zan_sladema8.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.CompletedWorkout
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutPreferences

@Database(
    entities = [NoteEntity::class, WorkoutPreferences::class, CompletedWorkout::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}