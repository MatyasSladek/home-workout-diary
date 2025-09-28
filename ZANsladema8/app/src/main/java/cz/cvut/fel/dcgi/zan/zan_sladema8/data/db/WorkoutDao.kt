package cz.cvut.fel.dcgi.zan.zan_sladema8.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.CompletedWorkout
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutPreferences
import org.threeten.bp.LocalDate

@Dao
interface WorkoutDao {

    @Query("SELECT * FROM workout_preferences WHERE id = 1")
    suspend fun getWorkoutPreferences(): WorkoutPreferences?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutPreferences(preferences: WorkoutPreferences)

    @Update
    suspend fun updateWorkoutPreferences(preferences: WorkoutPreferences)

    @Query("SELECT * FROM completed_workouts ORDER BY date DESC, timestamp DESC")
    suspend fun getAllCompletedWorkouts(): List<CompletedWorkout>

    @Query("SELECT * FROM completed_workouts WHERE date = :date")
    suspend fun getCompletedWorkoutsForDate(date: LocalDate): List<CompletedWorkout>

    @Query("SELECT * FROM completed_workouts WHERE id = :id")
    suspend fun getCompletedWorkoutById(id: String): CompletedWorkout?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedWorkout(workout: CompletedWorkout)

    @Update
    suspend fun updateCompletedWorkout(workout: CompletedWorkout)

    @Query("DELETE FROM completed_workouts WHERE id = :id")
    suspend fun deleteCompletedWorkout(id: String)

    @Query("SELECT * FROM completed_workouts WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    suspend fun getCompletedWorkoutsInRange(startDate: LocalDate, endDate: LocalDate): List<CompletedWorkout>
}