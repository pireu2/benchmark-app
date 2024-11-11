#include <jni.h>
#include <chrono>
#include <cmath>
#include <vector>
#include <functional>
#include <random>

#define PRIME_LIMIT 10000
#define PI_DIGITS 10000
#define SORT_SIZE 10000
#define RUNS 10


using BenchmarkFunction = void(*)();

class SingleThreadedBenchmarks{
public:
    static void calculatePrimes() {
        std::vector<int> primes;

        for(int i = 0; i < PRIME_LIMIT; i++){
            if(isPrime(i)){
                primes.push_back(i);
            }
        }

    }

    static void calculatePiDigits(){
        std::vector<unsigned int> piDigits;
        piDigits.reserve(PI_DIGITS);
        for (int i = 0; i < PI_DIGITS; ++i) {
            piDigits.push_back(piDigit(i));
        }
    }

    static void sort() {
        std::vector<int> v;
        v.reserve(SORT_SIZE);
        for (int i = 0; i < SORT_SIZE; i++) {
            v.push_back(rand());
        }

        bool swapped;
        for (int i = 0; i < SORT_SIZE - 1; i++) {
            swapped = false;
            for (int j = SORT_SIZE - 1; j > i; j--) {
                if (v[j] < v[j - 1]) {
                    std::swap(v[j], v[j - 1]);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    static BenchmarkFunction* getFunctions(){
        static BenchmarkFunction functions[] = { calculatePrimes, calculatePiDigits, sort };
        return functions;
    }

private:
    static bool isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }

        return true;
    }

    //BBP formula
    static unsigned int piDigit(int n){
        double x = 0.0;
        for(int k = 0; k <= n; k++){
            x += (1.0 / pow(16, k)) *
                 (4.0 / (8 * k + 1) - 2.0 / (8 * k + 4) - 1.0 / (8 * k + 5) - 1.0 / (8 * k + 6));
            x = x - floor(x);
        }
        return static_cast<unsigned int>(x * 16);
    }
};


long long runBenchmarks(BenchmarkFunction functions[], int numFunctions){
    if(numFunctions <= 0){
        return 0;
    }
    std::chrono::duration<double, std::milli> scores[numFunctions];

    for(int i = 0; i < numFunctions; i++){
        std::chrono::duration<double, std::milli> score = std::chrono::duration<double, std::milli>::zero();
        for(int j = 0; j < RUNS; j++){
            auto start = std::chrono::high_resolution_clock::now();
            functions[i]();
            auto end = std::chrono::high_resolution_clock::now();
            score += (end - start);
        }

        scores[i] = score;
    }
    //geometric mean of all durations
    long long totalScore = 0;
    for(int i = 0; i < numFunctions; i++){
        totalScore += scores[i].count() * scores[i].count();
    }

    return static_cast<long long>(sqrt(totalScore));
}


// Function to perform a CPU benchmark
extern "C" JNIEXPORT jlong JNICALL
Java_app_benchmarkapp_MainActivity_00024Companion_cpuBenchmark(JNIEnv* env, jobject /* this */) {
    auto start = std::chrono::high_resolution_clock::now();

    // Perform the CPU-intensive task
    long long score = runBenchmarks(SingleThreadedBenchmarks::getFunctions(), 3);

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> duration = end - start;

    // Return the duration in milliseconds
    return static_cast<jlong>(score);
}
