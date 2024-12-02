#pragma once

#include "BenchmarkBase.h"
#include <cmath>
#include <vector>
#include <functional>
#include <random>
#include <thread>

namespace benchmark {

    class MultiThreadedBenchmark: public BenchmarkBase {
    public:
        static unsigned int numThreads;
        BenchmarkFunctions getFunctions() override;

    private:
        static const unsigned int MATRIX_SIZE = 350;
        static const unsigned int ARRAY_SIZE = 10000;
        static const unsigned int IMAGE_SIZE = 1000;

        using Matrix = std::vector<std::vector<int>>;
        using Image = std::vector<std::vector<float>>;
        using Kernel = std::vector<std::vector<float>>;

        static void matrixMultiplicationBenchmark();
        static void parallelMergeSortBenchmark();
        static void imageProcessingBenchmark();

        static void matrixMultiply(const Matrix& A, const Matrix& B, Matrix& C);
        static void multiplyRowByMatrix(const Matrix& A, const Matrix& B, Matrix& C, int row);

        static void parallelMergeSort(std::vector<int>& arr, int left, int right, int depth);
        static void mergeSort(std::vector<int>& arr, int left, int right);
        static void merge(std::vector<int>& arr, int left, int mid, int right);

        static void applyKernel(const Image& input, Image& output, const Kernel& kernel, int startRow, int endRow);
    };

}
