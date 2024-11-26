
#include <jni.h>
#include <chrono>
#include <cmath>
#include <vector>


class RamBenchmarks{
public:
    using BenchmarkFunction = void(*)();
    static volatile float progress;

    static BenchmarkFunction* getFunctions(){
        static BenchmarkFunction functions[] = { memoryAllocationTest, memoryAccessSpeedTest, memoryBandwidthTest };
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
    static const unsigned int RUNS = 20;
    static const unsigned int SIZE = 1024 * 1024 * 50; // 50 MB

    static void memoryAllocationTest() {
        const int numAllocations = 10;
        std::vector<void*> allocations;

        for (int i = 0; i < numAllocations; ++i) {
            void* block = malloc(SIZE);
            if (block) {
                allocations.push_back(block);
            }
        }

        for (void* block : allocations) {
            free(block);
        }
    }

    static void memoryAccessSpeedTest() {
        std::vector<int> memory(SIZE / sizeof(int), 0);

        for (size_t i = 0; i < memory.size(); ++i) {
            memory[i] = static_cast<int>(i);
        }

        volatile int sum = 0;
        for (int i : memory) {
            sum += i;
        }
    }

    static void memoryBandwidthTest() {
        std::vector<char> src(SIZE, 'a');
        std::vector<char> dst(SIZE);

        std::memcpy(dst.data(), src.data(), SIZE);
    }
};

volatile float RamBenchmarks::progress = 0.0f;

extern "C"
JNIEXPORT jlong JNICALL
Java_app_benchmarkapp_MainActivity_00024Companion_ramBenchmark(JNIEnv *env, jobject thiz) {
    long long score = RamBenchmarks::runBenchmarks(RamBenchmarks::getFunctions(), 3);

    return static_cast<jlong>(score);
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_app_benchmarkapp_MainActivity_00024Companion_getRamProgress(JNIEnv *env, jobject thiz) {
    return RamBenchmarks::progress;
}
