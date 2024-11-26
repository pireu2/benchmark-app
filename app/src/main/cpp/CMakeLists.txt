
cmake_minimum_required(VERSION 3.22.1)


project("benchmarkapp")


add_library(${CMAKE_PROJECT_NAME} SHARED
        cpu_single_threaded_benchmark.cpp
        cpu_multi_threaded_benchmark.cpp
        ram_benchmark.cpp
        )


target_link_libraries(${CMAKE_PROJECT_NAME}
    android
    log)
