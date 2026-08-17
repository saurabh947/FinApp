import SwiftUI

/// Read-only detail of a single transaction.
struct TransactionDetailView: View {
    @EnvironmentObject var store: TransactionStore
    @EnvironmentObject var settings: SettingsStore
    let transactionId: Int

    var body: some View {
        let p = Palette.of(settings.theme)
        let tx = store.transactions.first { $0.id == transactionId }
        ZStack {
            p.background.ignoresSafeArea()
            if let tx {
                let isIncome = tx.type == .income
                let accent = isIncome ? p.income : p.expense
                let sign = isIncome ? "+" : "-"
                ScrollView {
                    VStack(spacing: 16) {
                        // Header
                        VStack(spacing: 12) {
                            ZStack {
                                Circle().fill(accent.opacity(0.14)).frame(width: 64, height: 64)
                                Image(systemName: tx.category.symbol)
                                    .font(.system(size: 26))
                                    .foregroundColor(accent)
                            }
                            Text(tx.title)
                                .font(.title2).bold()
                                .foregroundColor(p.onSurface)
                                .multilineTextAlignment(.center)
                            Text("\(sign)\(formatCurrency(tx.amount, settings.currency))")
                                .font(.largeTitle).bold()
                                .foregroundColor(accent)
                        }
                        .frame(maxWidth: .infinity)
                        .padding(24)
                        .background(RoundedRectangle(cornerRadius: 18).fill(p.surface))

                        // Details
                        VStack(spacing: 0) {
                            detailRow(p, "Type", isIncome ? "Income" : "Expense")
                            rowDivider(p)
                            detailRow(p, "Category", tx.category.label)
                            rowDivider(p)
                            detailRow(p, "Date", longDate(tx.date))
                            rowDivider(p)
                            detailRow(p, "Amount", formatCurrency(tx.amount, settings.currency))
                            rowDivider(p)
                            detailRow(p, "Transaction ID", String(tx.id))
                        }
                        .padding(.vertical, 4)
                        .background(RoundedRectangle(cornerRadius: 16).fill(p.surface))

                        // Notes — only shown when present
                        if !tx.notes.isEmpty {
                            VStack(alignment: .leading, spacing: 8) {
                                Text("Notes")
                                    .font(.subheadline)
                                    .foregroundColor(p.onSurfaceVariant)
                                Text(tx.notes)
                                    .font(.body)
                                    .foregroundColor(p.onSurface)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                            }
                            .padding(16)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .background(RoundedRectangle(cornerRadius: 16).fill(p.surface))
                        }
                    }
                    .padding(16)
                }
            } else {
                Text("Transaction not found").foregroundColor(p.onSurfaceVariant)
            }
        }
        .navigationTitle("Transaction")
        .navigationBarTitleDisplayMode(.inline)
        .finappNavBar(p)
        .onAppear { AppLog.screen("Transaction Detail") }
    }

    private func detailRow(_ p: Palette, _ label: String, _ value: String) -> some View {
        HStack(spacing: 12) {
            Text(label).font(.subheadline).foregroundColor(p.onSurfaceVariant)
            Spacer()
            Text(value).font(.body).fontWeight(.semibold).foregroundColor(p.onSurface)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
    }

    private func rowDivider(_ p: Palette) -> some View {
        Rectangle()
            .fill(p.onSurfaceVariant.opacity(0.15))
            .frame(height: 1)
            .padding(.horizontal, 16)
    }
}

private func longDate(_ date: Date) -> String {
    let f = DateFormatter()
    f.locale = Locale(identifier: "en_US")
    f.dateFormat = "MMMM d, yyyy"
    return f.string(from: date)
}
