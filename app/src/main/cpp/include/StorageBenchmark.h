#pragma once

#include "BenchmarkBase.h"
#include <fstream>
#include <vector>

namespace benchmark{



    class StorageBenchmark: public BenchmarkBase {
    public:
        BenchmarkFunctions getFunctions() override;
    private:
        static const unsigned int FILE_SIZE = 1024 * 1024 * 50; // 50 MB

        static void fileWriteTest();
        static void fileReadTest();
        static void fileDeleteTest();
    };
}



