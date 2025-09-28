package cz.cvut.fel.dcgi.zan.zan_sladema8.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cz.cvut.fel.dcgi.zan.zan_sladema8.util.NotificationHelper

class WorkoutAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val workoutType = intent.getStringExtra("workout_type") ?: return
        val workoutDate = intent.getStringExtra("workout_date") ?: return
        val exercises = intent.getStringExtra("exercises") ?: ""

        val notificationHelper = NotificationHelper(context)
        notificationHelper.showWorkoutReminder(
            workoutType = workoutType,
            exercises = exercises,
            date = workoutDate
        )
    }
}