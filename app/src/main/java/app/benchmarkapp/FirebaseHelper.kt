package app.benchmarkapp

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth


data class BenchmarkScore(
    val userId: String = "",
    val device: String = "",
    val singleThreadedScore: Long = 0,
    val multiThreadedScore: Long = 0,
    val ramScore: Long = 0,
    val storageScore: Long = 0,
    val gpuScore: Long = 0,
    val totalScore: Long = 0
)

object FirebaseHelper{
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    fun getUserId(): String?{
        return auth.currentUser?.uid
    }

    fun submitCurrentScore(){
        val userId = getUserId() ?: return
        val device = DeviceStats.deviceInfo?.model!!
        val singleThreadedScore = DeviceStats.getSingleThreadedScore() ?: return
        val multiThreadedScore = DeviceStats.getMultiThreadedScore() ?: return
        val ramScore = DeviceStats.getRamScore() ?: return
        val storageScore = DeviceStats.getStorageScore() ?: return
        val gpuScore = DeviceStats.getGpuScore() ?: return
        val totalScore = DeviceStats.getTotalScore() ?: return


        val score = BenchmarkScore(userId, device, singleThreadedScore, multiThreadedScore, ramScore, storageScore, gpuScore, totalScore)
        submitScore(userId, score)
    }

    fun submitScore(userId: String, score: BenchmarkScore){
        database.child("scores").child(userId).setValue(score)
    }

    fun getScores(callback: (List<BenchmarkScore>) -> Unit) {
        database.child("scores").get().addOnSuccessListener { dataSnapshot ->
            val scores = dataSnapshot.children.mapNotNull { it.getValue(BenchmarkScore::class.java) }
                .sortedByDescending { it.totalScore }
                .take(50)
            callback(scores)
        }
    }
}