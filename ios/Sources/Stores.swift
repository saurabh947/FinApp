import Foundation
import Combine

/// Hard-coded sample data so the dashboard has something to show.
enum SeedData {
    static func make() -> [Transaction] {
        let cal = Calendar(identifier: .gregorian)
        func day(_ d: Int) -> Date {
            cal.date(from: DateComponents(year: 2026, month: 6, day: d)) ?? Date()
        }
        return [
            // Income
            Transaction(id: 1, title: "Monthly Salary", category: .salary, amount: 5200.00, date: day(1), type: .income),
            Transaction(id: 2, title: "Website Project", category: .freelance, amount: 1450.00, date: day(5), type: .income),
            Transaction(id: 3, title: "Dividend Payout", category: .investments, amount: 318.40, date: day(6), type: .income),
            Transaction(id: 4, title: "Birthday Gift", category: .gifts, amount: 200.00, date: day(7), type: .income),
            Transaction(id: 5, title: "Tax Refund", category: .refunds, amount: 540.50, date: day(8), type: .income),
            Transaction(id: 6, title: "Logo Design Gig", category: .freelance, amount: 375.00, date: day(9), type: .income),
            // Expenses
            Transaction(id: 20, title: "Apartment Rent", category: .rent, amount: 1800.00, date: day(1), type: .expense),
            Transaction(id: 21, title: "Weekly Groceries", category: .groceries, amount: 142.30, date: day(2), type: .expense),
            Transaction(id: 22, title: "Electricity Bill", category: .utilities, amount: 96.75, date: day(3), type: .expense),
            Transaction(id: 23, title: "Dinner with Friends", category: .dining, amount: 64.20, date: day(3), type: .expense),
            Transaction(id: 24, title: "Metro Card Top-up", category: .transport, amount: 40.00, date: day(4), type: .expense),
            Transaction(id: 25, title: "New Sneakers", category: .shopping, amount: 119.99, date: day(5), type: .expense),
            Transaction(id: 26, title: "Movie Night", category: .entertainment, amount: 28.50, date: day(6), type: .expense),
            Transaction(id: 27, title: "Pharmacy", category: .health, amount: 33.10, date: day(7), type: .expense),
            Transaction(id: 28, title: "Streaming Plan", category: .subscriptions, amount: 15.99, date: day(7), type: .expense),
            Transaction(id: 29, title: "Coffee Runs", category: .dining, amount: 22.80, date: day(8), type: .expense),
            Transaction(id: 30, title: "Weekly Groceries", category: .groceries, amount: 88.40, date: day(9), type: .expense),
            Transaction(id: 31, title: "Gym Membership", category: .health, amount: 45.00, date: day(9), type: .expense),
            Transaction(id: 32, title: "Internet Bill", category: .utilities, amount: 59.99, date: day(10), type: .expense),
            Transaction(id: 33, title: "Music Subscription", category: .subscriptions, amount: 10.99, date: day(10), type: .expense),
        ]
    }
}

/// In-memory transaction store (mirrors the Android TransactionRepository).
final class TransactionStore: ObservableObject {
    @Published private(set) var transactions: [Transaction] = SeedData.make()
    private var nextId: Int = 34

    func add(title: String, amount: Double, category: Category, date: Date, type: TransactionType, notes: String = "") {
        transactions.append(
            Transaction(id: nextId, title: title, category: category, amount: amount, date: date, type: type, notes: notes)
        )
        nextId += 1
        AppLog.event("Transaction added: \(type.rawValue) \"\(title)\" \(amount)")
    }

    var income: [Transaction] { transactions.filter { $0.type == .income }.sorted { $0.date > $1.date } }
    var expenses: [Transaction] { transactions.filter { $0.type == .expense }.sorted { $0.date > $1.date } }
    var totalIncome: Double { income.reduce(0) { $0 + $1.amount } }
    var totalExpenses: Double { expenses.reduce(0) { $0 + $1.amount } }
    var balance: Double { totalIncome - totalExpenses }
}

/// In-memory app settings (mirrors the Android SettingsRepository).
final class SettingsStore: ObservableObject {
    @Published var currency: Currency = .usd
    @Published var theme: ThemeMode = .light
}
