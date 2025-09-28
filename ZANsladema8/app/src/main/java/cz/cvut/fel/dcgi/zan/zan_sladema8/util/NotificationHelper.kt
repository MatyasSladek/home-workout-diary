package cz.cvut.fel.dcgi.zan.zan_sladema8.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.cvut.fel.dcgi.zan.zan_sladema8.MainActivity
import cz.cvut.fel.dcgi.zan.zan_sladema8.R
import cz.cvut.fel.dcgi.zan.zan_sladema8.reciever.WorkoutActionReceiver

class NotificationHelper(private val context: Context) {

    companion object {
        private const val WORKOUT_CHANNEL_ID = "workout_reminders"
        private const val WORKOUT_CHANNEL_NAME = "Workout Reminders"
        private const val WORKOUT_CHANNEL_DESCRIPTION = "Notifications for workout reminders"
        private const val NOTIFICATION_ID = 1001
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                WORKOUT_CHANNEL_ID,
                WORKOUT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = WORKOUT_CHANNEL_DESCRIPTION
                enableVibration(true)
                setShowBadge(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showWorkoutReminder(workoutType: String, exercises: String, date: String) {
        if (!PermissionManager.hasNotificationPermission(context)) {
            return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("open_calendar", true)
            putExtra("workout_date", date)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val exerciseList = exercises.split(",").take(3).joinToString(", ")
        val moreExercises = if (exercises.split(",").size > 3) " and more..." else ""

        val notification = NotificationCompat.Builder(context, WORKOUT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_fitness)
            .setContentTitle("Time for your $workoutType workout!")
            .setContentText("Today's exercises: $exerciseList$moreExercises")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Ready for your $workoutType workout?\n\nToday's exercises:\n${exercises.replace(",", "\n• ")}")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_check,
                "Mark Complete",
                createCompleteWorkoutPendingIntent(workoutType, date)
            )
            .addAction(
                R.drawable.outline_notifications_paused_24,
                "Remind Later",
                createSnoozeWorkoutPendingIntent(workoutType, date)
            )
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        } catch (_: SecurityException) {

        }
    }

    private fun createCompleteWorkoutPendingIntent(workoutType: String, date: String): PendingIntent {
        val intent = Intent(context, WorkoutActionReceiver::class.java).apply {
            action = "COMPLETE_WORKOUT"
            putExtra("workout_type", workoutType)
            putExtra("workout_date", date)
        }

        return PendingIntent.getBroadcast(
            context,
            workoutType.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createSnoozeWorkoutPendingIntent(workoutType: String, date: String): PendingIntent {
        val intent = Intent(context, WorkoutActionReceiver::class.java).apply {
            action = "SNOOZE_WORKOUT"
            putExtra("workout_type", workoutType)
            putExtra("workout_date", date)
        }

        return PendingIntent.getBroadcast(
            context,
            workoutType.hashCode() + 1000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}