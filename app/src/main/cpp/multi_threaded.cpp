#include <jni.h>
#include <chrono>
#include <cmath>
#include <vector>
#include <functional>
#include <random>




class MultiThreadedBenchmarks {
public:
    using BenchmarkFunction = void(*)();
    static volatile float progress;

    static BenchmarkFunction* getFunctions(){
        static BenchmarkFunction functions[] = {  };
        return functions;
    }

    static long long runBenchmarks(BenchmarkFunction functions[], int numFunctions) {
        if (numFunctions <= 0) {
            return 0;
        }
        progress = 0.0f;
        std::chrono::duration<double, std::milli> scores[numFunctions];

        for (int j = 0; j < RUNS; j++) {
            for (int i = 0; i < numFunctions; i++) {
                auto start = std::chrono::high_resolution_clock::now();
                functions[i]();
                auto end = std::chrono::high_resolution_clock::now();
                scores[i] += (end - start);

                progress = static_cast<float>(j * numFunctions + i + 1) / static_cast<float>(numFunctions * RUNS);
            }
        }


        double product = 1.0;
        for (int i = 0; i < numFunctions; i++) {
            product *= 1.0 / scores[i].count();
        }
        double geometricMean = pow(product, 1.0 / numFunctions);

        return static_cast<long long>(geometricMean * 1e6);
    }
private:
    static const unsigned int RUNS = 20;
    //Matrix multiplication

    //Parallel merge sort

    //Image processing
};

volatile float MultiThreadedBenchmarks::progress = 0.0f;


extern "C" JNIEXPORT float JNICALL
Java_app_benchmarkapp_MainActivity_00024Companion_getMultiThreadedProgress(JNIEnv* env, jobject /* this */) {
    return MultiThreadedBenchmarks::progress;
}


// Function to perform a CPU benchmark
extern "C" JNIEXPORT jlong JNICALL
Java_app_benchmarkapp_MainActivity_00024Companion_multiThreadedBenchmark(JNIEnv* env, jobject /* this */) {
    long long score = MultiThreadedBenchmarks::runBenchmarks(MultiThreadedBenchmarks::getFunctions(), 3);

    return static_cast<jlong>(score);
}

