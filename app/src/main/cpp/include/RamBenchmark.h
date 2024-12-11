#pragma once

#include "BenchmarkBase.h"

namespace benchmark {

    class RamBenchmark: public BenchmarkBase {
    public:
        BenchmarkFunctions getFunctions() override;
    private:
        static const unsigned int SIZE = 1024 * 1024 * 10; // 10MB

        static void memoryAccessSpeedTest(int scalingFactor);
        static void memoryBandwidthTest(int scalingFactor);
    };

}


