package cz.cvut.fel.dcgi.zan.zan_sladema8.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import cz.cvut.fel.dcgi.zan.zan_sladema8.util.WorkoutAlarmManager

class WorkoutActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val workoutType = intent.getStringExtra("workout_type") ?: return
        val workoutDate = intent.getStringExtra("workout_date") ?: return

        when (intent.action) {
            "COMPLETE_WORKOUT" -> {
                NotificationManagerCompat.from(context).cancel(1001)
                val completeIntent = Intent("WORKOUT_COMPLETED").apply {
                    putExtra("workout_type", workoutType)
                    putExtra("workout_date", workoutDate)
                }
                context.sendBroadcast(completeIntent)
            }

            "SNOOZE_WORKOUT" -> {
                NotificationManagerCompat.from(context).cancel(1001)
                val alarmManager = WorkoutAlarmManager(context)
            }
        }
    }
}