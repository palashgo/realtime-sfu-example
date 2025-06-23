# Sample App

A modern Android application built with Jetpack Compose that demonstrates real-time communication capabilities using [Cloudflare's RealtimeKit](https://realtime.cloudflare.com/).

## 📋 Prerequisites

Before running this project, ensure you have:

- **Android Studio**: Latest stable version (Hedgehog or newer recommended)
- **JDK**: Java 21 or higher
- **Android SDK**: API level 26 (minimum) to 35 (target)
- **Gradle**: 8.0 or higher (handled by Gradle wrapper)

## 🛠️ Setup & Installation

1. Clone the repo
2. Create [a RealtimeKit meeting](https://docs.realtime.cloudflare.com/api?v=v2#/operations/create_meeting)
3. Add a participant [to generate token](https://docs.realtime.cloudflare.com/api?v=v2#/operations/add_participant)
4. Open in Android Studio, Run the app and paste the token
5. Follow instructions in ../server-demo/ 

## 📁 Project Structure

```
app/
├── src/main/
│   ├── java/com/cloudflare/anthropic_sample/
│   │   ├── MainActivity.kt
│   │   ├── ui/
│   │   │   ├── FlatButton.kt
│   │   │   ├── setup/
│   │   │   │   ├── SetupScreen.kt
│   │   │   │   └── SetupViewModel.kt
│   │   │   ├── meeting/
│   │   │   │   ├── MeetingScreen.kt
│   │   │   │   └── MeetingViewModel.kt
│   │   │   └── theme/
│   │   │       ├── Color.kt
│   │   │       ├── Theme.kt
│   │   │       └── Type.kt
│   │   └── utils/
│   ├── res/
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## 🔄 App Flow Diagram

```mermaid
graph TD
    A[App Launch] --> B[MainActivity]
    B --> C[Setup Screen]
    C --> D{Valid Input?}
    D -->|No| C
    D -->|Yes| E[Meeting Screen]
    E --> F[RealtimeKit Communication]
    
    subgraph "UI Components"
        G[SetupScreen.kt]
        H[MeetingScreen.kt]
    end
    
    subgraph "ViewModels"
        J[SetupViewModel]
        K[MeetingViewModel]
    end
    
    C --> G
    E --> H
    G --> J
    H --> K

    F --> J
    F --> K
    
    style A fill:#E07B39,stroke:#8B4513,color:#fff
    style E fill:#E07B39,stroke:#8B4513,color:#fff
    style F fill:#D2B48C,stroke:#8B4513
```

## 📱 Supported Android Versions

- **Minimum**: Android 8.0 (API 26)
- **Target**: Android 14 (API 35)
- **Recommended**: Android 10+ for best experience
