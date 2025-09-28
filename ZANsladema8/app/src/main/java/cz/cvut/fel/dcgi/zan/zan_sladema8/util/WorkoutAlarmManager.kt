package cz.cvut.fel.dcgi.zan.zan_sladema8.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutSuggestion
import cz.cvut.fel.dcgi.zan.zan_sladema8.reciever.WorkoutAlarmReceiver
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId

class WorkoutAlarmManager(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleWorkoutReminder(
        workoutSuggestion: WorkoutSuggestion,
        reminderTime: LocalTime = LocalTime.of(8, 0)
    ) {
        if (!PermissionManager.hasAlarmPermission(context)) {
            return
        }

        val intent = Intent(context, WorkoutAlarmReceiver::class.java).apply {
            putExtra("workout_type", workoutSuggestion.workoutType.name)
            putExtra("workout_date", workoutSuggestion.date.toString())
            putExtra("exercises", workoutSuggestion.workoutType.exercises.joinToString(","))
        }

        val requestCode = generateRequestCode(workoutSuggestion.date, workoutSuggestion.workoutType.name)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmDateTime = LocalDateTime.of(workoutSuggestion.date, reminderTime)
        val alarmTimeMillis = alarmDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (alarmTimeMillis > System.currentTimeMillis()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmTimeMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        alarmTimeMillis,
                        pendingIntent
                    )
                }
            } catch (e: SecurityException) {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    alarmTimeMillis,
                    pendingIntent
                )
            }
        }
    }

    fun cancelWorkoutReminder(date: LocalDate, workoutTypeName: String) {
        val intent = Intent(context, WorkoutAlarmReceiver::class.java)
        val requestCode = generateRequestCode(date, workoutTypeName)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

    fun scheduleWeeklyWorkoutReminders(
        workoutSuggestions: List<WorkoutSuggestion>,
        reminderTime: LocalTime = LocalTime.of(8, 0)
    ) {
        workoutSuggestions.forEach { workout ->
            if (!workout.isCompleted) {
                scheduleWorkoutReminder(workout, reminderTime)
            }
        }
    }

    private fun generateRequestCode(date: LocalDate, workoutType: String): Int {
        return "${date.toString()}_$workoutType".hashCode()
    }
}