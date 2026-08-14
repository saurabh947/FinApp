import SwiftUI

@main
struct FinAppApp: App {
    @StateObject private var transactions = TransactionStore()
    @StateObject private var settings = SettingsStore()

    var body: some Scene {
        WindowGroup {
            DashboardView()
                .environmentObject(transactions)
                .environmentObject(settings)
                // Theme is driven by the in-app Settings choice, not the system.
                .preferredColorScheme(settings.theme == .dark ? .dark : .light)
        }
    }
}
