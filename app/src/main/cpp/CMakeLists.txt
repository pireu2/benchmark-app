
cmake_minimum_required(VERSION 3.22.1)


project("benchmarkapp")

include_directories(${CMAKE_SOURCE_DIR}/include)

add_library(${CMAKE_PROJECT_NAME} SHARED
        benchmarks.cpp
        BenchmarkBase.cpp
        SingleThreadedBenchmark.cpp
        MultiThreadedBenchmark.cpp
        RamBenchmark.cpp
        StorageBenchmark.cpp
        )


target_link_libraries(${CMAKE_PROJECT_NAME}
    android
    log)
