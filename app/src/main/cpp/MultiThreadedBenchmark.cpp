#include "MultiThreadedBenchmark.h"

namespace benchmark {
    unsigned int MultiThreadedBenchmark::numThreads = 0;

    BenchmarkFunctions MultiThreadedBenchmark::getFunctions() {
        static BenchmarkFunction functions[] = { matrixMultiplicationBenchmark, parallelMergeSortBenchmark, imageProcessingBenchmark };
        return {functions, 3};
    }


    void MultiThreadedBenchmark::multiplyRowByMatrix(const Matrix &A, const Matrix &B, Matrix &C, int row) {
        unsigned int n = B.size();
        unsigned int m = B[0].size();

        for(int j = 0; j < m; j++){
            C[row][j] = 0;
            for (int k = 0; k < n; ++k) {
                C[row][j] += A[row][k] * B[k][j];
            }
        }
    }

    void MultiThreadedBenchmark::matrixMultiply(const Matrix &A, const Matrix &B, Matrix &C) {
        unsigned int rows = A.size();
        std::vector<std::thread> threads;


        auto worker = [&](int startRow, int endRow){
            for(int i = startRow; i < endRow; i++){
                multiplyRowByMatrix(A, B, C, i);
            }
        };

        unsigned int rowsPerThread = rows / numThreads;
        for(int i = 0; i < numThreads; i++){
            unsigned int startRow = i * rowsPerThread;
            unsigned int endRow = (i == numThreads - 1) ? rows : startRow + rowsPerThread;
            threads.emplace_back(std::thread(worker, startRow, endRow));
        }
        for(auto& t : threads){
            t.join();
        }
    }

    void MultiThreadedBenchmark::matrixMultiplicationBenchmark() {
        if(numThreads == 0){
            return;
        }
        Matrix A(MATRIX_SIZE, std::vector<int>(MATRIX_SIZE));
        Matrix B(MATRIX_SIZE, std::vector<int>(MATRIX_SIZE));
        Matrix C(MATRIX_SIZE, std::vector<int>(MATRIX_SIZE));

        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_int_distribution<int> dis(1, 100);

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                A[i][j] = dis(gen);
                B[i][j] = dis(gen);
            }
        }

        matrixMultiply(A, B, C);
    }

    void MultiThreadedBenchmark::merge(std::vector<int> &arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        std::vector<int> L(n1);
        std::vector<int> R(n2);

        for (int i = 0; i < n1; i++)
            L[i] = arr[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    void MultiThreadedBenchmark::mergeSort(std::vector<int> &arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    void MultiThreadedBenchmark::parallelMergeSort(std::vector<int> &arr, int left, int right,
                                                   int depth) {
        if (left < right) {
            if (depth <= 0) {
                mergeSort(arr, left, right);
            } else {
                int mid = left + (right - left) / 2;
                std::vector<std::thread> threads;
                threads.emplace_back(parallelMergeSort, std::ref(arr), left, mid, depth - 1);
                threads.emplace_back(parallelMergeSort, std::ref(arr), mid + 1, right, depth - 1);
                for (auto& t : threads) {
                    t.join();
                }
                merge(arr, left, mid, right);
            }
        }
    }

    void MultiThreadedBenchmark::parallelMergeSortBenchmark() {
        if (numThreads == 0) {
            return;
        }
        std::vector<int> arr(ARRAY_SIZE);
        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_int_distribution<int> dis(1, 100);

        for (int i = 0; i < ARRAY_SIZE; i++) {
            arr[i] = dis(gen);
        }

        int maxDepth = std::ceil(std::log2(numThreads));
        parallelMergeSort(arr, 0, ARRAY_SIZE - 1, maxDepth);
    }

    void MultiThreadedBenchmark::applyKernel(const Image &input, Image &output, const Kernel &kernel, int startRow, int endRow) {
        int kernelSize = static_cast<int>(kernel.size());
        int offset = kernelSize / 2;
        int numRows = static_cast<int>(input.size());
        int numCols = static_cast<int>(input[0].size());

        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < numCols; j++) {
                float sum = 0.0f;
                for (int ki = -offset; ki <= offset; ki++) {
                    for (int kj = -offset; kj <= offset; kj++) {
                        int ni = i + ki;
                        int nj = j + kj;
                        if (ni >= 0 && ni < numRows && nj >= 0 && nj < numCols) {
                            sum += input[ni][nj] * kernel[ki + offset][kj + offset];
                        }
                    }
                }
                output[i][j] = sum;
            }
        }
    }

    void MultiThreadedBenchmark::imageProcessingBenchmark() {
        if(numThreads == 0){
            return;
        }

        Image input(IMAGE_SIZE, std::vector<float>(IMAGE_SIZE));
        Image output(IMAGE_SIZE, std::vector<float>(IMAGE_SIZE));
        Kernel kernel = {
                {0.0625, 0.125, 0.0625},
                {0.125, 0.25, 0.125},
                {0.0625, 0.125, 0.0625}
        };

        std::random_device rd;
        std::mt19937 gen(rd());
        std::uniform_real_distribution<float> dis(0.0f, 1.0f);

        for (int i = 0; i < IMAGE_SIZE; i++) {
            for (int j = 0; j < IMAGE_SIZE; j++) {
                input[i][j] = dis(gen);
            }
        }

        std::vector<std::thread> threads;
        int rowsPerThread = IMAGE_SIZE / numThreads;
        for (unsigned int i = 0; i < numThreads; ++i) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numThreads - 1) ? IMAGE_SIZE : startRow + rowsPerThread;
            threads.emplace_back(applyKernel, std::cref(input), std::ref(output), std::cref(kernel), startRow, endRow);
        }

        for (auto& t : threads) {
            t.join();
        }
    }
}