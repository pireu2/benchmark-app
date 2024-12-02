#include "SingleThreadedBenchmark.h"

namespace benchmark {

    BenchmarkFunctions SingleThreadedBenchmark::getFunctions() {
        static BenchmarkFunction functions[] = { calculatePrimes, calculatePiDigits, sort };
        return {functions, 3};
    }

    void SingleThreadedBenchmark::calculatePrimes() {
        std::vector<int> primes;

        for(int i = 0; i < PRIME_LIMIT; i++){
            if(isPrime(i)){
                primes.push_back(i);
            }
        }
    }

    void SingleThreadedBenchmark::calculatePiDigits() {
        std::vector<unsigned int> piDigits;
        piDigits.reserve(PI_DIGITS);
        for (int i = 0; i < PI_DIGITS; ++i) {
            piDigits.push_back(piDigit(i));
        }
    }

    void SingleThreadedBenchmark::sort() {
        std::vector<int> v;
        v.reserve(SORT_SIZE);

        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_int_distribution<> dis(0, SORT_SIZE);

        for (int i = 0; i < SORT_SIZE; i++) {
            v.push_back(dis(gen));
        }

        bool swapped;
        for (int i = 0; i < SORT_SIZE - 1; i++) {
            swapped = false;
            for (int j = SORT_SIZE - 1; j > i; j--) {
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
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
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