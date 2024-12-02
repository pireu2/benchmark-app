#pragma once

#include "BenchmarkBase.h"

namespace benchmark {

    class RamBenchmark: public BenchmarkBase {
    public:
        BenchmarkFunctions getFunctions() override;
    private:
        static const unsigned int SIZE = 1024 * 1024 * 50;

        static void memoryAllocationTest();
        static void memoryAccessSpeedTest();
        static void memoryBandwidthTest();
    };

}


