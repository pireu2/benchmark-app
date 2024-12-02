#include "RamBenchmark.h"

namespace benchmark {
    BenchmarkFunctions RamBenchmark::getFunctions() {
        static BenchmarkFunction functions[] = { memoryAllocationTest, memoryAccessSpeedTest, memoryBandwidthTest };
        return {functions, 3};
    }

    void RamBenchmark::memoryAllocationTest() {
        const int numAllocations = 10;
        std::vector<void*> allocations;

        for (int i = 0; i < numAllocations; ++i) {
            void* block = malloc(SIZE);
            if (block) {
                allocations.push_back(block);
            }
        }

        for (void* block : allocations) {
            free(block);
        }
    }

    void RamBenchmark::memoryAccessSpeedTest() {
        std::vector<int> memory(SIZE / sizeof(int), 0);

        for (size_t i = 0; i < memory.size(); ++i) {
            memory[i] = static_cast<int>(i);
        }

        volatile int sum = 0;
        for (int i : memory) {
            sum += i;
        }
    }

    void RamBenchmark::memoryBandwidthTest() {
        std::vector<char> src(SIZE, 'a');
        std::vector<char> dst(SIZE);

        std::memcpy(dst.data(), src.data(), SIZE);
    }
}