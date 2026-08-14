# FinApp — iOS (SwiftUI)

A 1:1 SwiftUI port of the Android FinApp, with the same screens, data, and behaviour:

- **Dashboard** — Total Income / Total Expenses cards, Net Balance, Income/Expenses tabs,
  transaction list, an "Add Income/Expense" button, and a settings gear.
- **Add Transaction** — Income/Expense toggle, name, amount (currency-prefixed), category
  menu, date picker, Save.
- **Settings** — Currency (USD / GBP, reformats every amount) and Theme (Light = white
  background, Dark = dark grey). Both apply instantly.

Seed data and settings are in-memory (mirroring the Android repositories), so they reset on relaunch.

## Run it

Requires Xcode + a **booted** iOS Simulator. Then:

```bash
./build.sh
```

This compiles the SwiftUI sources straight into a simulator `.app` bundle (no Xcode project
needed), installs it on the booted simulator, and launches it. Bundle id: `com.finapp.ios`.

Under the hood:

```bash
xcrun --sdk iphonesimulator swiftc -sdk "$(xcrun --sdk iphonesimulator --show-sdk-path)" \
    -target arm64-apple-ios17.0-simulator -parse-as-library -o build/FinApp.app/FinApp Sources/*.swift
cp Info.plist build/FinApp.app/
xcrun simctl install booted build/FinApp.app
xcrun simctl launch booted com.finapp.ios
```

## Structure

```
ios/
├── Info.plist                 # Bundle metadata (id, name, launch screen)
├── build.sh                   # Compile -> .app -> install -> launch
└── Sources/
    ├── FinAppApp.swift        # @main App; injects stores + theme
    ├── Models.swift           # Transaction, Category, TransactionType, Currency, ThemeMode
    ├── Stores.swift           # TransactionStore + SettingsStore (ObservableObject) + SeedData
    ├── Theme.swift            # Palette (light/dark), currency formatting, Route
    ├── DashboardView.swift    # Cards, tabs, list, FAB, settings gear
    ├── AddTransactionView.swift
    └── SettingsView.swift
```

## Opening in Xcode

The sources are a plain SwiftUI app. To work on it in Xcode, create a new **iOS App**
project (SwiftUI lifecycle) and add the files in `Sources/` (delete Xcode's generated
`ContentView.swift`/`App.swift`). The `build.sh` path above is just for fast CLI builds onto
the simulator without a project file.
