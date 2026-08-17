import SwiftUI

extension Color {
    init(hex: UInt32) {
        let r = Double((hex >> 16) & 0xFF) / 255.0
        let g = Double((hex >> 8) & 0xFF) / 255.0
        let b = Double(hex & 0xFF) / 255.0
        self.init(.sRGB, red: r, green: g, blue: b, opacity: 1.0)
    }
}

/// Colors for the active theme. Light = white background, Dark = dark grey (per Settings).
struct Palette {
    let background: Color
    let surface: Color
    let onSurface: Color
    let onSurfaceVariant: Color
    let primary: Color
    let onPrimary: Color
    let income: Color
    let expense: Color
    let incomeContainer: Color
    let expenseContainer: Color
    let summaryAmount: Color

    static func of(_ theme: ThemeMode) -> Palette {
        switch theme {
        case .light:
            return Palette(
                background: Color(hex: 0xFFFFFF),
                surface: Color(hex: 0xF2F4F3),
                onSurface: Color(hex: 0x1A1C1A),
                onSurfaceVariant: Color(hex: 0x49544B),
                primary: Color(hex: 0x1B5E20),
                onPrimary: .white,
                income: Color(hex: 0x2E7D32),
                expense: Color(hex: 0xC62828),
                incomeContainer: Color(hex: 0xDDF3DE),
                expenseContainer: Color(hex: 0xFBE2E2),
                summaryAmount: Color(hex: 0x1A1C1A)
            )
        case .dark:
            return Palette(
                background: Color(hex: 0x303030),
                surface: Color(hex: 0x3D3D3D),
                onSurface: Color(hex: 0xECECEC),
                onSurfaceVariant: Color(hex: 0xC2C8C2),
                primary: Color(hex: 0x2E7D32),
                onPrimary: .white,
                income: Color(hex: 0x81C995),
                expense: Color(hex: 0xEF9A9A),
                incomeContainer: Color(hex: 0xDDF3DE),
                expenseContainer: Color(hex: 0xFBE2E2),
                summaryAmount: Color(hex: 0x1A1C1A)
            )
        }
    }
}

/// Formats an amount in the given currency, e.g. 1234.5 -> "$1,234.50" or "£1,234.50".
func formatCurrency(_ amount: Double, _ currency: Currency) -> String {
    let f = NumberFormatter()
    f.numberStyle = .currency
    f.locale = Locale(identifier: currency.localeId)
    return f.string(from: NSNumber(value: amount)) ?? String(format: "%.2f", amount)
}

/// Short transaction date, e.g. "Jun 9".
func shortDate(_ date: Date) -> String {
    let f = DateFormatter()
    f.locale = Locale(identifier: "en_US")
    f.dateFormat = "MMM d"
    return f.string(from: date)
}

/// Routes pushed onto the navigation stack.
enum Route: Hashable {
    case add(TransactionType)
    case settings
    case detail(Int)
}

extension View {
    /// Green navigation bar with white title/buttons, matching the Android top bar.
    func finappNavBar(_ palette: Palette) -> some View {
        self
            .toolbarBackground(palette.primary, for: .navigationBar)
            .toolbarBackground(.visible, for: .navigationBar)
            .toolbarColorScheme(.dark, for: .navigationBar)
    }
}
