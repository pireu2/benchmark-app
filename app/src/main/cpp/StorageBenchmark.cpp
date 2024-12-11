
#include "StorageBenchmark.h"

namespace benchmark{

    BenchmarkFunctions StorageBenchmark::getFunctions() {
        static BenchmarkFunction functions[] = { fileWriteTest, fileReadTest };
        static std::vector<std::string> names = { "File Write", "File Read" };
        return {functions, names, 2};
    }

    void StorageBenchmark::fileWriteTest(int scalingFactor) {
        unsigned int size = FILE_SIZE * scalingFactor;

        std::ofstream file("testfile.bin", std::ios::binary);
        std::vector<char> data(size, 'a');
        file.write(data.data(), data.size());
        file.close();
    }

    void StorageBenchmark::fileReadTest(int scalingFactor) {
        unsigned int size = FILE_SIZE * scalingFactor;

        std::ifstream file("testfile.bin", std::ios::binary);
        std::vector<char> data(size);
        file.read(data.data(), data.size());
        file.close();
    }

}
