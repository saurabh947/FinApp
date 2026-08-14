import SwiftUI

struct SettingsView: View {
    @EnvironmentObject var settings: SettingsStore

    var body: some View {
        let p = Palette.of(settings.theme)
        ZStack {
            p.background.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    settingCard(p, title: "Currency",
                                subtitle: "Used across the dashboard for income and expenses") {
                        Picker("", selection: $settings.currency) {
                            ForEach(Currency.allCases) { c in
                                Text("\(c.label)  \(c.symbol)").tag(c)
                            }
                        }
                        .pickerStyle(.segmented)
                    }

                    settingCard(p, title: "Theme",
                                subtitle: "Light uses a white background, dark uses a dark grey background") {
                        Picker("", selection: $settings.theme) {
                            ForEach(ThemeMode.allCases) { t in
                                Text(t.label).tag(t)
                            }
                        }
                        .pickerStyle(.segmented)
                    }
                }
                .padding(16)
            }
        }
        .navigationTitle("Settings")
        .navigationBarTitleDisplayMode(.inline)
        .finappNavBar(p)
        .onAppear { AppLog.screen("Settings") }
    }

    private func settingCard<Content: View>(_ p: Palette, title: String, subtitle: String,
                                            @ViewBuilder _ content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            VStack(alignment: .leading, spacing: 2) {
                Text(title).font(.headline).fontWeight(.semibold).foregroundColor(p.onSurface)
                Text(subtitle).font(.caption).foregroundColor(p.onSurfaceVariant)
            }
            content()
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 16).fill(p.surface))
    }
}
