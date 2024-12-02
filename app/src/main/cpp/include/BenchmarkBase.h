//
// Created by sebi on 02/12/2024.
//

#pragma once

#include <chrono>


namespace benchmark {

    using BenchmarkFunction = void(*)();

    struct BenchmarkFunctions {
        BenchmarkFunction* functions;
        int numFunctions;
    };

    class BenchmarkBase {
    public:
        [[nodiscard]] float getProgress() const { return progress; }
        void setProgress(float value) { progress = value; }

        virtual BenchmarkFunctions getFunctions() = 0;

        long long runBenchmarks();

    protected:
        static const unsigned int RUNS = 20;
        volatile float progress = 0.0f;
    };
}

