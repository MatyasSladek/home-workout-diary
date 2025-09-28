package cz.cvut.fel.dcgi.zan.zan_sladema8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jakewharton.threetenabp.AndroidThreeTen
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.AppRouter
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.theme.DiaryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        enableEdgeToEdge()
        setContent {
            DiaryAppTheme {
                AppRouter()
            }
        }
    }
}
