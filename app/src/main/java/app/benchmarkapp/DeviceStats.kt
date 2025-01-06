package app.benchmarkapp

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object DeviceStats {
    var deviceInfo: DeviceInfoProvider.DeviceInfo ? = null
    var disableNavigation by  mutableStateOf(false)
    var cacheDirPath: String? = null

    private var singleThreadedScore: Long? = null
    private var multiThreadedScore: Long? = null
    private var ramScore: Long? = null
    private var storageScore: Long? = null
    private var gpuScore: Long? = null
    private var totalScore: Long? = null
    private const val PREFS_NAME = "benchmark_scores"
    private const val SINGLE_THREADED_SCORE_KEY = "single_threaded_score"
    private const val MULTI_THREADED_SCORE_KEY = "multi_threaded_score"
    private const val RAM_SCORE_KEY = "ram_score"
    private const val STORAGE_SCORE_KEY = "storage_score"
    private const val GPU_SCORE_KEY = "gpu_score"
    private const val TOTAL_SCORE_KEY = "total_score"

    private lateinit var sharedPreferences: SharedPreferences



    private fun loadScores(){
        singleThreadedScore = sharedPreferences.getLong(SINGLE_THREADED_SCORE_KEY, 0L).takeIf { it != 0L }
        multiThreadedScore = sharedPreferences.getLong(MULTI_THREADED_SCORE_KEY, 0L).takeIf { it != 0L }
        ramScore = sharedPreferences.getLong(RAM_SCORE_KEY, 0L).takeIf { it != 0L }
        storageScore = sharedPreferences.getLong(STORAGE_SCORE_KEY, 0L).takeIf { it != 0L }
        gpuScore = sharedPreferences.getLong(GPU_SCORE_KEY, 0L).takeIf { it != 0L }
        totalScore = sharedPreferences.getLong(TOTAL_SCORE_KEY, 0L).takeIf { it != 0L }
    }

    private fun saveScores() {
        with(sharedPreferences.edit()) {
            putLong(SINGLE_THREADED_SCORE_KEY, singleThreadedScore ?: 0L)
            putLong(MULTI_THREADED_SCORE_KEY, multiThreadedScore ?: 0L)
            putLong(RAM_SCORE_KEY, ramScore ?: 0L)
            putLong(STORAGE_SCORE_KEY, storageScore ?: 0L)
            putLong(GPU_SCORE_KEY, gpuScore ?: 0L)
            putLong(TOTAL_SCORE_KEY, totalScore ?: 0L)
            apply()
        }
    }

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadScores()
    }

    fun setSingleThreadedScore(score: Long) {
        singleThreadedScore = score
        saveScores()
    }

    fun setMultiThreadedScore(score: Long) {
        multiThreadedScore = score
        saveScores()
    }

    fun setRamScore(score: Long) {
        ramScore = score
        saveScores()
    }

    fun setStorageScore(score: Long) {
        storageScore = score
        saveScores()
    }

    fun setGpuScore(score: Long) {
        gpuScore = score
        saveScores()
    }

    fun getSingleThreadedScore(): Long? = singleThreadedScore

    fun getMultiThreadedScore(): Long? = multiThreadedScore

    fun getRamScore(): Long? = ramScore

    fun getStorageScore(): Long? = storageScore

    fun getGpuScore(): Long? = gpuScore

    fun getTotalScore(): Long? {
        if (singleThreadedScore == null || multiThreadedScore == null || ramScore == null || storageScore == null || gpuScore == null) {
            return null
        }
        totalScore = singleThreadedScore!! + multiThreadedScore!! + ramScore!! + storageScore!! + gpuScore!!
        return totalScore
    }
}