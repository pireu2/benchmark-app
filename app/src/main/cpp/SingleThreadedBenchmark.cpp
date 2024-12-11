#include "SingleThreadedBenchmark.h"

namespace benchmark {

    BenchmarkFunctions SingleThreadedBenchmark::getFunctions() {
        static BenchmarkFunction functions[] = { calculatePrimes, calculatePiDigits, sort };
        static std::vector<std::string> names = { "Calculate Primes", "Calculate Pi Digits", "Sort" };
        return {functions, names, 3};
    }

    void SingleThreadedBenchmark::calculatePrimes(int scalingFactor) {
        unsigned int size = PRIME_LIMIT * scalingFactor;

        for(int i = 0; i < size; i++){
            isPrime(i);
        }
    }

    void SingleThreadedBenchmark::calculatePiDigits(int scalingFactor) {
        unsigned int size = PI_DIGITS * scalingFactor;

        std::vector<unsigned int> piDigits;
        piDigits.reserve(size);
        for (int i = 0; i < size; ++i) {
            piDigits.push_back(piDigit(i));
        }
    }

    void SingleThreadedBenchmark::sort(int scalingFactor) {
        unsigned int size = SORT_SIZE * scalingFactor;

        std::vector<int> v;
        v.reserve(size);

        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_int_distribution<> dis(0, size);

        for (int i = 0; i < size; i++) {
            v.push_back(dis(gen));
        }

        bool swapped;
        for (int i = 0; i < size - 1; i++) {
            swapped = false;
            for (int j = size - 1; j > i; j--) {
                if (v[j] < v[j - 1]) {
                    std::swap(v[j], v[j - 1]);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    bool SingleThreadedBenchmark::isPrime(int n) {
        if (n <= 1) {
            return false;
        }

        for (int i = 2; i <= n / 2; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    unsigned int SingleThreadedBenchmark::piDigit(int n) {
        double x = 0.0;
        for(int k = 0; k <= n; k++){
            x += (1.0 / pow(16, k)) *
                 (4.0 / (8 * k + 1) - 2.0 / (8 * k + 4) - 1.0 / (8 * k + 5) - 1.0 / (8 * k + 6));
            x = x - floor(x);
        }
        return static_cast<unsigned int>(x * 16);
    }
}