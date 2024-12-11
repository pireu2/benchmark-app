
#include "BenchmarkBase.h"

namespace benchmark {

    long long BenchmarkBase::runBenchmarks(const std::string& path) {
        BenchmarkFunctions functions = this->getFunctions();
        int numFunctions = functions.numFunctions;
        BenchmarkFunction *f = functions.functions;

        if (numFunctions <= 0) {
            return 0;
        }
        setProgress(0.0f);
        std::chrono::duration<double, std::milli> totalDuration(0);
        std::vector<Results> results;

        unsigned int totalIterations = RUNS * numFunctions * 10;
        unsigned int currentIteration = 0;

        for(unsigned int scalingFactor = 1; scalingFactor <= 10; scalingFactor++){
            for (int j = 0; j < numFunctions; j++) {
                std::chrono::duration<double, std::milli> currDuration(0);
                for (int i = 0; i < RUNS; i++) {
                    auto start = std::chrono::high_resolution_clock::now();
                    f[j](static_cast<int>(scalingFactor));
                    auto end = std::chrono::high_resolution_clock::now();
                    totalDuration += (end - start);
                    currDuration += (end - start);


                    currentIteration++;
                    setProgress(static_cast<float>(currentIteration) / static_cast<float>(totalIterations));
                }
                results.push_back({functions.names[j], static_cast<unsigned int>(currDuration.count()), scalingFactor});
            }
        }



        saveResults(path + "results.txt", results);
        return static_cast<long long>(1e8 / totalDuration.count());
    }

    void BenchmarkBase::saveResults(const std::string& filename, std::vector<Results> results) {
        std::ofstream file(filename, std::ios::out);
        if(file.is_open()){
            for (const auto& result : results) {
                file << "Benchmark: "<< result.benchmarkName << ", Size: " << result.size << ", Time: " << result.time   << "\n";
            }
            file.close();
        }
    }

}