package cz.cvut.fel.dcgi.zan.zan_sladema8.data.local

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
)