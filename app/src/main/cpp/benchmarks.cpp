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
    std::string cacheDirPath;

    extern "C" JNIEXPORT float JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_getSingleThreadedProgress(JNIEnv* env, jobject /* this */) {
        return singleThreadedBenchmark.getProgress();
    }


    extern "C" JNIEXPORT jlong JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_singleThreadedBenchmark(JNIEnv* env, jobject /* this */) {
        long long score = singleThreadedBenchmark.runBenchmarks(cacheDirPath);

        return static_cast<jlong>(score);
    }

    extern "C" JNIEXPORT float JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_getMultiThreadedProgress(JNIEnv* env, jobject /* this */, jint numThreads) {
        return multiThreadedBenchmark.getProgress();
    }


    extern "C" JNIEXPORT jlong JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_multiThreadedBenchmark(JNIEnv* env, jobject /* this */, jint numThreads) {
        MultiThreadedBenchmark::numThreads = static_cast<unsigned int>(numThreads);

        long long score = multiThreadedBenchmark.runBenchmarks(cacheDirPath);

        return static_cast<jlong>(score);
    }

    extern "C"
    JNIEXPORT jlong JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_ramBenchmark(JNIEnv *env, jobject thiz) {
        long long score = ramBenchmark.runBenchmarks(cacheDirPath);

        return static_cast<jlong>(score);
    }

    extern "C"
    JNIEXPORT jfloat JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_getRamProgress(JNIEnv *env, jobject thiz) {
        return ramBenchmark.getProgress();
    }

    extern "C" JNIEXPORT jlong JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_storageBenchmark(JNIEnv *env, jobject thiz) {
        long long score = storageBenchmark.runBenchmarks(cacheDirPath);
        return static_cast<jlong>(score);
    }

    extern "C" JNIEXPORT jfloat JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_getStorageProgress(JNIEnv *env, jobject thiz) {
        return storageBenchmark.getProgress();
    }

    extern "C"
    JNIEXPORT void JNICALL
    Java_app_benchmarkapp_MainActivity_00024Companion_setCacheDirPath(JNIEnv *env, jobject thiz, jstring path) {
        const char *nativePath = env->GetStringUTFChars(path, nullptr);
        cacheDirPath = std::string(nativePath);
        env->ReleaseStringUTFChars(path, nativePath);
    }

}


