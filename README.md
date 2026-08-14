# FinApp — Personal Finance Aggregator

A small Android app that aggregates a consumer's income and expenses into a single
dashboard. Built with **Kotlin** + **Jetpack Compose** (Material 3).

## What it does

The launch screen is a **Dashboard** with:

- **Two summary cards** — *Total Income* and *Total Expenses* — plus a *Net Balance* strip.
- **Two tabs** below the cards — *Income* and *Expenses* — each listing the relevant
  transactions (category icon, title, category, date, and signed amount).
- **Add income/expense** — a contextual floating button opens a form
  ([`AddTransactionScreen.kt`](app/src/main/java/com/finapp/ui/AddTransactionScreen.kt))
  with type, name, amount, category, and date fields. Saving adds the entry to the
  list and updates the totals immediately.
- **Settings** — a gear icon (top-right) opens
  [`SettingsScreen.kt`](app/src/main/java/com/finapp/ui/SettingsScreen.kt) to choose the
  **currency** (USD / GBP — reformats every amount on the dashboard) and the **theme**
  (Light = white background, Dark = dark grey). Both apply instantly.

The entire UI is **Jetpack Compose** (Material 3); there are no XML layouts or themes —
the only XML is the Android manifest and the launcher icon.

The app ships with **seeded test data** (see
[`SeedData.kt`](app/src/main/java/com/finapp/data/SeedData.kt)). Added transactions and the
chosen currency/theme live in in-memory repositories
([`TransactionRepository`](app/src/main/java/com/finapp/data/TransactionRepository.kt) and
[`SettingsRepository`](app/src/main/java/com/finapp/data/SettingsRepository.kt), exposed as
`StateFlow`); there is no database/network layer yet, so they reset on app restart.

## Project structure

```
app/src/main/java/com/finapp/
├── MainActivity.kt              # Entry point; applies theme + currency, hosts the nav graph
├── data/
│   ├── Transaction.kt           # Transaction model + Category / TransactionType enums
│   ├── SeedData.kt              # Hard-coded sample transactions
│   ├── TransactionRepository.kt # In-memory store (StateFlow); add new transactions here
│   └── SettingsRepository.kt    # In-memory currency + theme settings (StateFlow)
├── ui/
│   ├── FinAppNavHost.kt         # Navigation: dashboard / add / settings
│   ├── DashboardViewModel.kt    # Observes the repository; income/expenses + totals
│   ├── DashboardScreen.kt       # Cards, tabs, lists, add FAB, settings gear
│   ├── AddTransactionScreen.kt  # Form: type, name, amount, category, date + Save
│   ├── SettingsScreen.kt        # Currency (USD/GBP) and theme (Light/Dark)
│   └── theme/                   # Material 3 colors / theme / type (light + dark)
└── util/
    └── Money.kt                 # Currency-aware formatting + LocalCurrency
```

## Building & running

This project is configured for the tooling already installed on this machine:

- **Android SDK:** `/Users/sab/Documents/Study/Android/SDK` (set in `local.properties`)
- **JDK:** the build is pinned to **JDK 21** via `org.gradle.java.home` in
  `gradle.properties`, because the machine's default `java` is JDK 25, which the
  Android Gradle Plugin does not yet support.

### From the command line

```bash
# JDK 21 is pinned in gradle.properties, but the gradlew launcher still needs a
# compatible JVM, so point JAVA_HOME at it for the terminal:
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home

./gradlew assembleDebug                     # build the debug APK
./gradlew installDebug                       # install on a running emulator/device
```

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

### From Android Studio

Just open the project folder. Android Studio uses its own bundled JDK, so the JDK 25
issue does not apply there.

## Tech stack

| Component        | Version       |
|------------------|---------------|
| Gradle           | 8.13          |
| Android Gradle Plugin | 8.9.1    |
| Kotlin           | 2.1.0         |
| Compose BOM      | 2024.12.01    |
| Navigation Compose | 2.8.5       |
| compileSdk / targetSdk | 35      |
| minSdk           | 26            |

## Next steps / ideas

- Persist transactions with Room so additions survive an app restart.
- Edit and delete existing transactions (e.g. swipe actions).
- Group the dashboard by month and add a date range filter.
- Charts (spending by category), budgets, and multi-account aggregation.
