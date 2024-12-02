#pragma once

#include "BenchmarkBase.h"
#include <chrono>
#include <cmath>
#include <vector>
#include <functional>
#include <random>

namespace benchmark {

    class SingleThreadedBenchmark: public BenchmarkBase {
    public:
        BenchmarkFunctions getFunctions() override;

    private:
        static const unsigned int PRIME_LIMIT = 5000;
        static const unsigned int PI_DIGITS = 5000;
        static const unsigned int SORT_SIZE = 5000;


        static void calculatePrimes();
        static void calculatePiDigits();
        static void sort();

        static bool isPrime(int n);
        static unsigned int piDigit(int n);
    };

}
