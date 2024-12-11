//
// Created by sebi on 02/12/2024.
//

#pragma once

#include <chrono>
#include <utility>
#include <vector>
#include <fstream>
#include <string>


namespace benchmark {

    using BenchmarkFunction = void(*)(int scalingFactor);

    struct BenchmarkFunctions {
        BenchmarkFunction* functions;
        std::vector<std::string> names;
        int numFunctions;
    };

    struct Results{
        std::string benchmarkName;
        unsigned int time;
        unsigned int size;
    };

    class BenchmarkBase {
    public:
        [[nodiscard]] float getProgress() const { return progress; }
        void setProgress(float value) { progress = value; }

        virtual BenchmarkFunctions getFunctions() = 0;

        long long runBenchmarks(const std::string& path);
        static void saveResults(const std::string& filename, std::vector<Results> results);

    protected:
        static const unsigned int RUNS = 5;
        volatile float progress = 0.0f;
    };
}

