import SwiftUI

struct AddTransactionView: View {
    @EnvironmentObject var store: TransactionStore
    @EnvironmentObject var settings: SettingsStore
    @Environment(\.dismiss) private var dismiss

    let initialType: TransactionType
    @State private var type: TransactionType
    @State private var name = ""
    @State private var amountText = ""
    @State private var category: Category
    @State private var date = Date()
    @State private var notes = ""

    init(initialType: TransactionType) {
        self.initialType = initialType
        _type = State(initialValue: initialType)
        _category = State(initialValue: Category.firstForType(initialType))
    }

    private var amountValue: Double? { Double(amountText) }
    private var canSave: Bool {
        !name.trimmingCharacters(in: .whitespaces).isEmpty && (amountValue ?? 0) > 0
    }

    var body: some View {
        let p = Palette.of(settings.theme)
        ZStack {
            p.background.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    // Income / Expense toggle
                    Picker("", selection: $type) {
                        Text("Income").tag(TransactionType.income)
                        Text("Expense").tag(TransactionType.expense)
                    }
                    .pickerStyle(.segmented)
                    .onChange(of: type) { _, newType in
                        category = Category.firstForType(newType)
                    }

                    labeledField(p, "Name") {
                        TextField("Name", text: $name)
                            .foregroundColor(p.onSurface)
                            .padding(12)
                            .background(fieldBorder(p))
                    }

                    labeledField(p, "Amount") {
                        HStack(spacing: 6) {
                            Text(settings.currency.symbol).foregroundColor(p.onSurfaceVariant)
                            TextField("0", text: $amountText)
                                .keyboardType(.decimalPad)
                                .foregroundColor(p.onSurface)
                        }
                        .padding(12)
                        .background(fieldBorder(p))
                    }

                    labeledField(p, "Category") {
                        Menu {
                            ForEach(Category.forType(type)) { c in
                                Button(c.label) { category = c }
                            }
                        } label: {
                            HStack {
                                Text(category.label).foregroundColor(p.onSurface)
                                Spacer()
                                Image(systemName: "chevron.down").foregroundColor(p.onSurfaceVariant)
                            }
                            .padding(12)
                            .background(fieldBorder(p))
                        }
                    }

                    labeledField(p, "Date") {
                        DatePicker("", selection: $date, displayedComponents: .date)
                            .labelsHidden()
                            .datePickerStyle(.compact)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }

                    labeledField(p, "Notes") {
                        TextField("Add notes (optional)", text: $notes, axis: .vertical)
                            .foregroundColor(p.onSurface)
                            .lineLimit(3...6)
                            .padding(12)
                            .background(fieldBorder(p))
                    }

                    Button {
                        if let amt = amountValue, canSave {
                            store.add(title: name.trimmingCharacters(in: .whitespaces),
                                      amount: amt, category: category, date: date, type: type,
                                      notes: notes.trimmingCharacters(in: .whitespacesAndNewlines))
                            dismiss()
                        }
                    } label: {
                        Text("Save").bold()
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 15)
                            .foregroundColor(.white)
                            .background(RoundedRectangle(cornerRadius: 14)
                                .fill(canSave ? p.primary : Color.gray.opacity(0.45)))
                    }
                    .disabled(!canSave)
                    .padding(.top, 8)
                }
                .padding(16)
            }
        }
        .navigationTitle("Add Transaction")
        .navigationBarTitleDisplayMode(.inline)
        .finappNavBar(p)
        .onAppear { AppLog.screen(type == .income ? "Add Income" : "Add Expense") }
    }

    private func labeledField<Content: View>(_ p: Palette, _ label: String,
                                             @ViewBuilder _ content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(label).font(.caption).foregroundColor(p.onSurfaceVariant)
            content()
        }
    }

    private func fieldBorder(_ p: Palette) -> some View {
        RoundedRectangle(cornerRadius: 10).stroke(p.onSurfaceVariant.opacity(0.5), lineWidth: 1)
    }
}
