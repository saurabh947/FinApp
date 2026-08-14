import SwiftUI

struct DashboardView: View {
    @EnvironmentObject var store: TransactionStore
    @EnvironmentObject var settings: SettingsStore
    @State private var path = NavigationPath()
    @State private var selectedTab = 0   // 0 = Income, 1 = Expenses

    var body: some View {
        let p = Palette.of(settings.theme)
        NavigationStack(path: $path) {
            ZStack(alignment: .bottomTrailing) {
                p.background.ignoresSafeArea()

                VStack(spacing: 0) {
                    summarySection(p)
                    tabRow(p)
                    transactionList(p)
                }

                fab(p)
            }
            .navigationTitle("FinApp")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .principal) {
                    Text("FinApp").font(.headline).bold().foregroundColor(p.onPrimary)
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button { path.append(Route.settings) } label: {
                        Image(systemName: "gearshape.fill")
                    }
                }
            }
            .finappNavBar(p)
            .navigationDestination(for: Route.self) { route in
                switch route {
                case .add(let type): AddTransactionView(initialType: type)
                case .settings: SettingsView()
                }
            }
            .onAppear { AppLog.screen("Dashboard") }
        }
    }

    // MARK: - Summary

    private func summarySection(_ p: Palette) -> some View {
        VStack(spacing: 12) {
            HStack(spacing: 12) {
                summaryCard(p, label: "Total Income", amount: store.totalIncome,
                            symbol: "chart.line.uptrend.xyaxis",
                            container: p.incomeContainer, accent: p.income)
                summaryCard(p, label: "Total Expenses", amount: store.totalExpenses,
                            symbol: "chart.line.downtrend.xyaxis",
                            container: p.expenseContainer, accent: p.expense)
            }
            balanceBar(p)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }

    private func summaryCard(_ p: Palette, label: String, amount: Double, symbol: String,
                             container: Color, accent: Color) -> some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(spacing: 8) {
                ZStack {
                    Circle().fill(accent.opacity(0.18)).frame(width: 36, height: 36)
                    Image(systemName: symbol).font(.system(size: 15, weight: .bold)).foregroundColor(accent)
                }
                Text(label).font(.subheadline).foregroundColor(accent)
            }
            Text(formatCurrency(amount, settings.currency))
                .font(.title2).bold().foregroundColor(p.summaryAmount)
                .lineLimit(1).minimumScaleFactor(0.6)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 18).fill(container))
    }

    private func balanceBar(_ p: Palette) -> some View {
        HStack(spacing: 10) {
            Image(systemName: "creditcard.fill").foregroundColor(p.onSurfaceVariant)
            Text("Net Balance").font(.subheadline).foregroundColor(p.onSurface)
            Spacer()
            Text(formatCurrency(store.balance, settings.currency))
                .font(.headline).bold()
                .foregroundColor(store.balance >= 0 ? p.income : p.expense)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .frame(maxWidth: .infinity)
        .background(RoundedRectangle(cornerRadius: 16).fill(p.surface))
    }

    // MARK: - Tabs + list

    private func tabRow(_ p: Palette) -> some View {
        HStack(spacing: 0) {
            ForEach(0..<2, id: \.self) { i in
                let title = i == 0 ? "Income" : "Expenses"
                VStack(spacing: 8) {
                    Text(title)
                        .font(.subheadline)
                        .fontWeight(selectedTab == i ? .bold : .regular)
                        .foregroundColor(selectedTab == i ? p.primary : p.onSurfaceVariant)
                    Rectangle()
                        .fill(selectedTab == i ? p.primary : Color.clear)
                        .frame(height: 3)
                }
                .padding(.top, 10)
                .frame(maxWidth: .infinity)
                .contentShape(Rectangle())
                .onTapGesture { selectedTab = i }
            }
        }
        .background(p.background)
    }

    @ViewBuilder
    private func transactionList(_ p: Palette) -> some View {
        let items = selectedTab == 0 ? store.income : store.expenses
        let isIncome = selectedTab == 0
        if items.isEmpty {
            VStack {
                Spacer()
                Text("No transactions yet").foregroundColor(p.onSurfaceVariant)
                Spacer()
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        } else {
            ScrollView {
                LazyVStack(spacing: 10) {
                    ForEach(items) { tx in
                        transactionRow(p, tx: tx, isIncome: isIncome)
                    }
                }
                .padding(.horizontal, 16)
                .padding(.top, 16)
                .padding(.bottom, 96)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
    }

    private func transactionRow(_ p: Palette, tx: Transaction, isIncome: Bool) -> some View {
        let accent = isIncome ? p.income : p.expense
        let sign = isIncome ? "+" : "-"
        return HStack(spacing: 12) {
            ZStack {
                Circle().fill(accent.opacity(0.14)).frame(width: 44, height: 44)
                Image(systemName: tx.category.symbol).font(.system(size: 18)).foregroundColor(accent)
            }
            VStack(alignment: .leading, spacing: 2) {
                Text(tx.title).font(.body).fontWeight(.semibold)
                    .foregroundColor(p.onSurface).lineLimit(1)
                Text("\(tx.category.label)  ·  \(shortDate(tx.date))")
                    .font(.caption).foregroundColor(p.onSurfaceVariant)
            }
            Spacer(minLength: 8)
            Text("\(sign)\(formatCurrency(tx.amount, settings.currency))")
                .font(.headline).bold().foregroundColor(accent)
        }
        .padding(14)
        .frame(maxWidth: .infinity)
        .background(RoundedRectangle(cornerRadius: 16).fill(p.surface))
    }

    // MARK: - FAB

    private func fab(_ p: Palette) -> some View {
        Button {
            path.append(Route.add(selectedTab == 0 ? .income : .expense))
        } label: {
            HStack(spacing: 8) {
                Image(systemName: "plus")
                Text(selectedTab == 0 ? "Add Income" : "Add Expense").bold()
            }
            .foregroundColor(p.onPrimary)
            .padding(.horizontal, 20)
            .padding(.vertical, 16)
            .background(Capsule().fill(p.primary))
            .shadow(color: .black.opacity(0.25), radius: 5, x: 0, y: 2)
        }
        .padding(20)
    }
}
