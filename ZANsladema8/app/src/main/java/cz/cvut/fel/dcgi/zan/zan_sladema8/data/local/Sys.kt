package cz.cvut.fel.dcgi.zan.zan_sladema8.data.local

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)