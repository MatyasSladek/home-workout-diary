package cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository

import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WeatherResponse
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.remote.NetworkModule
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.remote.WeatherApiService
import cz.cvut.fel.dcgi.zan.zan_sladema8.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WeatherRepository(
    private val apiService: WeatherApiService = NetworkModule.weatherApiService
) {

    private val apiKey = BuildConfig.WEATHER_API_KEY

    fun getCurrentWeather(latitude: Double, longitude: Double): Flow<Resource<WeatherResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getCurrentWeather(latitude, longitude, apiKey)

            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    emit(Resource.Success(weatherResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("IO error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    fun getCurrentWeatherByCity(cityName: String): Flow<Resource<WeatherResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getCurrentWeatherByCity(cityName, apiKey)

            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    emit(Resource.Success(weatherResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("IO error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }
}