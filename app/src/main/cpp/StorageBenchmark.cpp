
#include "StorageBenchmark.h"

namespace benchmark{

    BenchmarkFunctions StorageBenchmark::getFunctions() {
        static BenchmarkFunction functions[] = { fileWriteTest, fileReadTest, fileDeleteTest };
        return {functions, 3};
    }

    void StorageBenchmark::fileWriteTest() {
        std::ofstream file("testfile.bin", std::ios::binary);
        std::vector<char> data(FILE_SIZE, 'a');
        file.write(data.data(), data.size());
        file.close();
    }

    void StorageBenchmark::fileReadTest() {
        std::ifstream file("testfile.bin", std::ios::binary);
        std::vector<char> data(FILE_SIZE);
        file.read(data.data(), data.size());
        file.close();
    }

    void StorageBenchmark::fileDeleteTest() {
        std::remove("testfile.bin");
    }
}
