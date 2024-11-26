#include <jni.h>
#include <chrono>
#include <cmath>
#include <vector>
#include <functional>
#include <random>

class SingleThreadedBenchmarks{
public:
    using BenchmarkFunction = void(*)();
    static volatile float progress;

    static BenchmarkFunction* getFunctions(){
        static BenchmarkFunction functions[] = { calculatePrimes, calculatePiDigits, sort };
        return functions;
    }

    static long long runBenchmarks(BenchmarkFunction functions[], int numFunctions) {
        if (numFunctions <= 0) {
            return 0;
        }
        progress = 0.0f;
        std::chrono::duration<double, std::milli> totalDuration(0);

        for (int j = 0; j < RUNS; j++) {
            for (int i = 0; i < numFunctions; i++) {
                auto start = std::chrono::high_resolution_clock::now();
                functions[i]();
                auto end = std::chrono::high_resolution_clock::now();
                totalDuration += (end - start);

                progress = static_cast<float>(j * numFunctions + i + 1) / static_cast<float>(numFunctions * RUNS);
            }
        }

        return static_cast<long long>(1e8 / totalDuration.count());
    }

private:
    static const unsigned int PRIME_LIMIT = 5000;
    static const unsigned int PI_DIGITS = 5000;
    static const unsigned int SORT_SIZE = 5000;
    static const unsigned int RUNS = 20;

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

        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_int_distribution<> dis(0, SORT_SIZE);

        for (int i = 0; i < SORT_SIZE; i++) {
            v.push_back(dis(gen));
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


volatile float SingleThreadedBenchmarks::progress = 0.0f;



extern "C" JNIEXPORT float JNICALL
Java_app_benchmarkapp_MainActivity_00024Companion_getSingleThreadedProgress(JNIEnv* env, jobject /* this */) {
    return SingleThreadedBenchmarks::progress;
}


extern "C" JNIEXPORT jlong JNICALL
Java_app_benchmarkapp_MainActivity_00024Companion_singleThreadedBenchmark(JNIEnv* env, jobject /* this */) {
    long long score = SingleThreadedBenchmarks::runBenchmarks(SingleThreadedBenchmarks::getFunctions(), 3);

    return static_cast<jlong>(score);
}

