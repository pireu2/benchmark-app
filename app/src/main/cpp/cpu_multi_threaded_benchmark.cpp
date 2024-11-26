#include <jni.h>
#include <chrono>
#include <cmath>
#include <vector>
#include <functional>
#include <random>
#include <thread>




class MultiThreadedBenchmarks {
public:
    using BenchmarkFunction = void(*)();
    static volatile float progress;
    static unsigned int numThreads;

    static BenchmarkFunction* getFunctions(){
        static BenchmarkFunction functions[] = {matrixMultiplicationBenchmark, parallelMergeSortBenchmark, imageProcessingBenchmark};
        return functions;
    }

    static long long runBenchmarks(BenchmarkFunction functions[], int numFunctions) {
        if (numFunctions <= 0) {
            return 0;
        }
        progress = 0.0f;
        std::chrono::duration<double, std::milli> totalDuration(0);

        for (int j = 0; j < RUNS; j++) {
            for (int i = 0; i < numFunctions; i++) {
                auto start = std::chrono::high_resolution_clock::now();
                functions[i]();
                auto end = std::chrono::high_resolution_clock::now();
                totalDuration += (end - start);


                progress = static_cast<float>(j * numFunctions + i + 1) / static_cast<float>(numFunctions * RUNS);
            }
        }


        return static_cast<long long>(1e8 / totalDuration.count());
    }

private:
    static const unsigned int RUNS = 20;
    static const unsigned int MATRIX_SIZE = 350;
    static const unsigned int ARRAY_SIZE = 10000;
    static const unsigned int IMAGE_SIZE = 1000;


    //Matrix multiplication

    using Matrix = std::vector<std::vector<int>>;

    static void multiplyRowByMatrix(const Matrix& A, const Matrix& B, Matrix& C, int row){
        unsigned int n = B.size();
        unsigned int m = B[0].size();

        for(int j = 0; j < m; j++){
            C[row][j] = 0;
            for (int k = 0; k < n; ++k) {
                C[row][j] += A[row][k] * B[k][j];
            }
        }
    }

    static void matrixMultiply(const Matrix& A, const Matrix& B, Matrix& C){
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

    static void matrixMultiplicationBenchmark(){
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

    //Parallel merge sort

    static void merge(std::vector<int>& arr, int left, int mid, int right){
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

    static void mergeSort(std::vector<int>& arr, int left, int right){
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    static void parallelMergeSort(std::vector<int>& arr, int left, int right, int depth){
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

    static void parallelMergeSortBenchmark() {
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

    //Image processing

    using Image = std::vector<std::vector<float>>;
    using Kernel = std::vector<std::vector<float>>;

    static void applyKernel(const Image& input, Image& output, const Kernel& kernel, int startRow, int endRow){
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

    static void imageProcessingBenchmark(){
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
};

volatile float MultiThreadedBenchmarks::progress = 0.0f;
unsigned int MultiThreadedBenchmarks::numThreads = 0;


extern "C" JNIEXPORT float JNICALL
Java_app_benchmarkapp_MainActivity_00024Companion_getMultiThreadedProgress(JNIEnv* env, jobject /* this */, jint numThreads) {
    MultiThreadedBenchmarks::numThreads = static_cast<unsigned int>(numThreads);
    return MultiThreadedBenchmarks::progress;
}


// Function to perform a CPU benchmark
extern "C" JNIEXPORT jlong JNICALL
Java_app_benchmarkapp_MainActivity_00024Companion_multiThreadedBenchmark(JNIEnv* env, jobject /* this */, jint numThreads) {
    MultiThreadedBenchmarks::numThreads = static_cast<unsigned int>(numThreads);

    long long score = MultiThreadedBenchmarks::runBenchmarks(MultiThreadedBenchmarks::getFunctions(), 3);

    return static_cast<jlong>(score);
}

