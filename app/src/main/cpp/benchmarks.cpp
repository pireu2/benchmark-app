#include <jni.h>
#include "BenchmarkBase.h"
#include "SingleThreadedBenchmark.h"
#include "MultiThreadedBenchmark.h"
#include "RamBenchmark.h"
#include "StorageBenchmark.h"

namespace benchmark{

    SingleThreadedBenchmark singleThreadedBenchmark;
    MultiThreadedBenchmark multiThreadedBenchmark;
    RamBenchmark ramBenchmark;
    StorageBenchmark storageBenchmark;

    extern "C" JNIEXPORT float JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_getSingleThreadedProgress(JNIEnv* env, jobject /* this */) {
        return singleThreadedBenchmark.getProgress();
    }


    extern "C" JNIEXPORT jlong JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_singleThreadedBenchmark(JNIEnv* env, jobject /* this */) {
        long long score = singleThreadedBenchmark.runBenchmarks();

        return static_cast<jlong>(score);
    }

    extern "C" JNIEXPORT float JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_getMultiThreadedProgress(JNIEnv* env, jobject /* this */, jint numThreads) {
        return multiThreadedBenchmark.getProgress();
    }


    extern "C" JNIEXPORT jlong JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_multiThreadedBenchmark(JNIEnv* env, jobject /* this */, jint numThreads) {
        MultiThreadedBenchmark::numThreads = static_cast<unsigned int>(numThreads);

        long long score = multiThreadedBenchmark.runBenchmarks();

        return static_cast<jlong>(score);
    }

    extern "C"
    JNIEXPORT jlong JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_ramBenchmark(JNIEnv *env, jobject thiz) {
        long long score = ramBenchmark.runBenchmarks();

        return static_cast<jlong>(score);
    }

    extern "C"
    JNIEXPORT jfloat JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_getRamProgress(JNIEnv *env, jobject thiz) {
        return ramBenchmark.getProgress();
    }

    extern "C" JNIEXPORT jlong JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_storageBenchmark(JNIEnv *env, jobject thiz) {
        long long score = storageBenchmark.runBenchmarks();
        return static_cast<jlong>(score);
    }

    extern "C" JNIEXPORT jfloat JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_getStorageProgress(JNIEnv *env, jobject thiz) {
        return storageBenchmark.getProgress();
    }

}

