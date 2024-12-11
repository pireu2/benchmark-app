#include "RamBenchmark.h"

namespace benchmark {
    BenchmarkFunctions RamBenchmark::getFunctions() {
        static BenchmarkFunction functions[] = { memoryAccessSpeedTest, memoryBandwidthTest };
        static std::vector<std::string> names = { "Memory Access Speed", "Memory Bandwidth" };

        return {functions, names, 2};
    }


    void RamBenchmark::memoryAccessSpeedTest(int scalingFactor) {
        unsigned int size = SIZE * scalingFactor;

        std::vector<int> memory(size / sizeof(int), 0);


        for (size_t i = 0; i < memory.size(); ++i) {
            memory[i] = static_cast<int>(i);
        }

        volatile int sum = 0;
        for (int i : memory) {
            sum += i;
        }
    }

    void RamBenchmark::memoryBandwidthTest(int scalingFactor) {
        unsigned int size = SIZE * scalingFactor;
        std::vector<char> src(size, 'a');
        std::vector<char> dst(size);

        std::memcpy(dst.data(), src.data(), size);
    }
}