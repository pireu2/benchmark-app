
#include "BenchmarkBase.h"

namespace benchmark {

    long long BenchmarkBase::runBenchmarks() {
        int numFunctions = this->getFunctions().numFunctions;
        BenchmarkFunction *functions = this->getFunctions().functions;

        if (numFunctions <= 0) {
            return 0;
        }
        setProgress(0.0f);
        std::chrono::duration<double, std::milli> totalDuration(0);



        for (int j = 0; j < RUNS; j++) {
            for (int i = 0; i < numFunctions; i++) {
                auto start = std::chrono::high_resolution_clock::now();
                functions[i]();
                auto end = std::chrono::high_resolution_clock::now();
                totalDuration += (end - start);


                setProgress(static_cast<float>(j * numFunctions + i + 1) / static_cast<float>(numFunctions * RUNS));
            }
        }


        return static_cast<long long>(1e8 / totalDuration.count());
    }

}