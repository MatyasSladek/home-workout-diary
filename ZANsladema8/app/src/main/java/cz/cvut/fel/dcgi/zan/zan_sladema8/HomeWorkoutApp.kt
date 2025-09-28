package cz.cvut.fel.dcgi.zan.zan_sladema8

import android.app.Application

class HomeWorkoutApp: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContainer.init(this)
    }

}

