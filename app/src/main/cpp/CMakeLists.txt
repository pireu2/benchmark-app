
cmake_minimum_required(VERSION 3.22.1)


project("benchmarkapp")


add_library(${CMAKE_PROJECT_NAME} SHARED
        single_threaded.cpp
        multi_threaded.cpp
        )


target_link_libraries(${CMAKE_PROJECT_NAME}

    android
    log)
