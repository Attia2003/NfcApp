📇 NFC Reader App (Android)
A Kotlin + Jetpack Compose Android app for reading MIFARE Classic NFC cards. Scans cardholder data, displays it on-screen, and sends an arrival event to a backend service.

🔍 What It Does

Detects NFC tags in foreground mode via NfcAdapter in MainActivity
Reads card blocks from a MIFARE Classic tag using ReadDataSource
Builds a CardData domain model from the raw scanned fields
Drives UI state through NfcViewModel (Idle → Loading → Success / Error)
Triggers a backend "arrival" call after a successful scan
Accepts keyboard-style external reader input (buffered until Enter) as a fallback path


🗺️ NFC Card Field Mapping
NfcRepositoryImpl reads the following fields from the card:
FieldSectorBlock OffsetfirstName10secondName31id11department12section (used as phone number in backend call)20office21title22joinDate30

🏗️ Architecture
The app uses a three-layer architecture with unidirectional data flow:
Presentation  →  NfcReadViewModel + Compose UI (state flow)
Domain        →  Use Cases + Repository Contracts + Models
Data          →  NfcRepositoryImpl + ReadDataSource + Retrofit

📁 Project Structure
app/src/main/java/com/example/nfcapp/
├── MainActivity.kt
├── NfcApp.kt
├── core/
│   ├── data/
│   │   ├── repository/NfcRepositoryImpl.kt
│   │   └── source/ReadDataSource.kt
│   ├── domain/
│   │   ├── model/CardData.kt
│   │   ├── repository/NfcReadRepository.kt
│   │   └── usecase/
│   │       ├── ReadClassicCardUseCase.kt
│   │       └── SendArrivalUseCase.kt
│   └── presentation/
│       ├── NfcReadViewModel.kt
│       └── ui/
│           ├── NfcScreen.kt
│           ├── InfoCard.kt
│           └── StatusCard.kt
└── util/mergeNames.kt

🛠️ Tech Stack
LayerTechnologyLanguageKotlinUIJetpack Compose + Material 3DIHiltNetworkingRetrofit + OkHttpArchitectureViewModel + Use Cases + Repository (layered)

⚙️ Requirements

Android Studio – Recent stable release
Android SDK

minSdk = 27
targetSdk = 36
compileSdk = 36


Physical Android device with NFC support (must be MIFARE Classic capable — most emulators do not support this)
