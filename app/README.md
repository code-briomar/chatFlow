# Chat Flow - Make Your Chats Flow
<hr>

## Table of Contents
1. [Introduction](#1-introduction)
2. [Project Structure](#2-project-structure)
3. [Build and Setup Instructions](#3-build-and-setup-instructions)
4. [Code Walkthrough](#4-code-walkthrough)
5. [API Integration](#5-api-integration)
6. [Testing](#6-testing)
7. [Contribution Guidelines](#7-contribution-guidelines)
8. [Troubleshooting](#8-troubleshooting)
9. [Data Security](#9-data-security)
10. [Appendices](#10-appendices)

## 1. Introduction
### Overview
ChatFlow is an app designed to provide users with witty, sarcastic, and sometimes passive-aggressive responses to images or screenshots they upload. This includes tweets they want more context on, memes they don't understand, chats with "dry" texters, or anything that could benefit from a humorous or insightful perspective.

This document provides guidance on the app's source code, covering its structure, setup, and best practices for development.

### Scope
This documentation covers the setup, architecture, key components, and contribution guidelines for the ChatFlow codebase. It serves as a complete guide for developers looking to work on or contribute to the project.

---

## 2. Project Structure
### Directory Layout
```markdown
app/
├── manifests/
│   └── AndroidManifest.xml
├── java/
│   └── com.briomar.chatflow/
│       ├── ChatFlow.java
│       ├── FileUtils.java
│       ├── NetworkMonitor.java
│       ├── NetworkRequest.java
│       └── UserResponse.java
│   └── com.briomar.chatflow (androidTest)/
│   └── com.briomar.chatflow (test)/
├── java (generated)/
├── assets/
├── res/
│   ├── drawable/
│   ├── font/
│   ├── layout/
│   ├── mipmap/
│   ├── values/
│   └── xml/
└── res (generated)/

Gradle Scripts/
├── build.gradle (Project: chatFlow)
├── build.gradle (Module: app)
├── proguard-rules.pro
├── gradle.properties
├── gradle-wrapper.properties
├── local.properties
└── settings.gradle
```

### Key Components

- **ChatFlow.java**: Manages the user interface for the main chat screen.
- **FileUtils.java**: Handles file operations, including extracting paths from user-selected screenshots.
- **NetworkMonitor.java**: Continuously monitors the user's internet connection to ensure connectivity.
- **NetworkRequest.java**: Responsible for handling API requests to fetch responses based on uploaded content.
- **UserResponse.java**: Processes and formats responses for the user based on the content they uploaded.

---

## 3. Build and Setup Instructions
### Prerequisites
- Android Studio
- Java Development Kit (JDK) 8 or higher
- Internet connection for API integration

### Steps
1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Build and run the app on an emulator or physical device.

[//]: # (4. Set up API keys in `NetworkRequest.java` &#40;if needed&#41;.)

---

## 4. Code Walkthrough
- **UI Components**: The main UI components are defined in `ChatFlow.java` and the `res/layout` directory.
- **Networking**: `NetworkRequest.java` and `NetworkMonitor.java` work together to manage internet requests and monitor connectivity.
- **File Handling**: `FileUtils.java` is responsible for managing file paths and handling uploads.

---

## 5. API Integration
Details on integrating APIs, including the endpoints, request formats, and handling responses. Ensure API keys (if any) are secured and not hardcoded in source files.

---

## 6. Testing
### Unit Testing
- Use JUnit for unit testing classes like `FileUtils` and `NetworkRequest`.

### Instrumentation Testing
- Use Android's instrumentation testing to ensure UI components in `ChatFlow.java` work as expected.

---

## 7. Contribution Guidelines
1. Fork the repository and create a new branch for your feature or bug fix.
2. Follow coding conventions as outlined in the `CONTRIBUTING.md` file.
3. Submit a pull request with a description of the changes.

---

## 8. Troubleshooting
### Common Issues
- **Gradle Sync Errors**: Ensure you have the latest version of Android Studio and Gradle.
- **API Connectivity Issues**: Check `NetworkMonitor.java` to ensure the app detects internet connection status accurately.

---

## 9. Data Security
- User-uploaded content is only processed for immediate responses and not stored.
- Follow best practices for securing sensitive data, especially API keys.

---

## 10. Appendices
Additional resources, diagrams, or reference material for deeper understanding.

---

Let me know if any other part of the documentation needs clarification or if you'd like to add more details!