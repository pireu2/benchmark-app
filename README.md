# BenchmarkApp

BenchmarkApp is an Android application designed to benchmark various aspects of your device. This project is built using Kotlin and Gradle.

## Context

The aim of this project is to develop an Android mobile application that benchmarks all performance characteristics of a mobile device. This includes evaluating CPU performance, GPU efficiency, memory access speed, and storage I/O, as well as displaying detailed hardware configuration information.

With the increasing reliance on mobile devices for various computational tasks, from gaming to productivity, having a tool that can measure the performance metrics of a device becomes crucial for developers and users alike. This benchmarking app will help in comparing different devices, analyzing hardware performance, and identifying potential bottlenecks in mobile computing.

## Objectives

- Develop an Android application that can test multiple aspects of a mobile device’s performance.
- Create CPU benchmarks, including execution time, multi-core performance, and thread management.
- Implement GPU benchmarking to measure rendering time and graphics processing capabilities.
- Include memory benchmarking to assess memory access speed and read/write operations.
- Display detailed hardware information such as processor type, GPU model, RAM size, and storage.
- Provide a user-friendly interface for easy navigation and benchmarking execution.
- Ensure cross-device compatibility to run on a variety of Android devices.

## Project Structure

```
BenchmarkApp/
├── .gitignore
├── .gradle/
├── .idea/
├── .kotlin/
├── app/
├── build.gradle.kts
├── gradle/
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
└── settings.gradle.kts
```

## Getting Started

### Prerequisites

- Android Studio
- Java Development Kit (JDK) 11 or higher
- Gradle

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/pireu2/benchmark-app
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.

### Building the Project

To build the project, use the following command:

```sh
./gradlew build
```

## Accomplishments

This project includes the following features and accomplishments:

- **Device Benchmarking**: Measures various performance metrics of the device, including CPU, memory, and storage.
- **Detailed Hardware Information**: Displays detailed hardware information such as processor type, GPU model, RAM size, and storage.
- **Multi and Single threaded CPU Benchmarks**: using C++ for accurate performance measurement.
- **GPU Benchmarks**: using OpenGL for evaluating graphics processing capabilities.
- **RAM and Storage Benchmarks**: using C++ to assess memory access speed and storage I/O performance.
- **User Interface**: A clean and intuitive user interface designed using Android's latest UI components.
- **Kotlin Coroutines**: Utilizes Kotlin Coroutines for asynchronous operations to ensure smooth performance.
- **Unit Testing**: Comprehensive unit tests to ensure the reliability and correctness of the application.

## Project Configuration

### Gradle Configuration

The project uses Kotlin DSL for Gradle configuration. Key configuration files include:

- [build.gradle.kts](build.gradle.kts)
- [settings.gradle.kts](settings.gradle.kts)
- [gradle.properties](gradle.properties)

### Android Configuration

The main Android configuration is located in [app/build.gradle.kts](build.gradle.kts). Key settings include:

- `namespace`: `app.benchmarkapp`
- `compileSdk`: 34
- `minSdk`: 24
- `targetSdk`: 34
- `versionCode`: 1
- `versionName`: "1.0"

### ProGuard Configuration

ProGuard rules are defined in [app/proguard-rules.pro](proguard-rules.pro).

## Code Style

The project follows the official Kotlin code style. Code style settings are defined in [.idea/codeStyles/Project.xml](.idea/codeStyles/Project.xml).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
