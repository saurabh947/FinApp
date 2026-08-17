import Foundation

/// Whether a transaction adds to or subtracts from the balance.
enum TransactionType: String, CaseIterable, Identifiable, Hashable {
    case income
    case expense
    var id: String { rawValue }
}

/// A spending/earning category — knows its display label, type, and SF Symbol icon.
enum Category: String, CaseIterable, Identifiable {
    // Income
    case salary, freelance, investments, gifts, refunds
    // Expenses
    case rent, groceries, utilities, dining, transport, shopping, entertainment, health, subscriptions

    var id: String { rawValue }

    var label: String {
        switch self {
        case .salary: return "Salary"
        case .freelance: return "Freelance"
        case .investments: return "Investments"
        case .gifts: return "Gifts"
        case .refunds: return "Refunds"
        case .rent: return "Rent"
        case .groceries: return "Groceries"
        case .utilities: return "Utilities"
        case .dining: return "Dining"
        case .transport: return "Transport"
        case .shopping: return "Shopping"
        case .entertainment: return "Entertainment"
        case .health: return "Health"
        case .subscriptions: return "Subscriptions"
        }
    }

    var type: TransactionType {
        switch self {
        case .salary, .freelance, .investments, .gifts, .refunds: return .income
        default: return .expense
        }
    }

    var symbol: String {
        switch self {
        case .salary: return "dollarsign.circle.fill"
        case .freelance: return "briefcase.fill"
        case .investments: return "chart.line.uptrend.xyaxis"
        case .gifts: return "gift.fill"
        case .refunds: return "arrow.uturn.backward.circle.fill"
        case .rent: return "house.fill"
        case .groceries: return "cart.fill"
        case .utilities: return "bolt.fill"
        case .dining: return "fork.knife"
        case .transport: return "car.fill"
        case .shopping: return "bag.fill"
        case .entertainment: return "film.fill"
        case .health: return "heart.fill"
        case .subscriptions: return "creditcard.fill"
        }
    }

    static func forType(_ type: TransactionType) -> [Category] {
        allCases.filter { $0.type == type }
    }

    static func firstForType(_ type: TransactionType) -> Category {
        forType(type).first ?? .salary
    }
}

/// A single money movement. `amount` is always positive; `type` sets the direction.
struct Transaction: Identifiable {
    let id: Int
    var title: String
    var category: Category
    var amount: Double
    var date: Date
    var type: TransactionType
    var notes: String = ""
}

/// Display currency. `localeId` drives the symbol + number formatting.
enum Currency: String, CaseIterable, Identifiable, Hashable {
    case usd
    case gbp
    var id: String { rawValue }
    var label: String { self == .usd ? "USD" : "GBP" }
    var symbol: String { self == .usd ? "$" : "£" }
    var localeId: String { self == .usd ? "en_US" : "en_GB" }
}

/// Light or dark appearance.
enum ThemeMode: String, CaseIterable, Identifiable, Hashable {
    case light
    case dark
    var id: String { rawValue }
    var label: String { self == .light ? "Light" : "Dark" }
}
