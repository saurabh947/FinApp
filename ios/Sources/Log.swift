import os

/// App-level unified logging. Info level; subsystem is the bundle id so logs are
/// easy to filter (e.g. `log stream --level info --predicate 'subsystem == "com.finapp.ios"'`).
enum AppLog {
    static let log = Logger(subsystem: "com.finapp.ios", category: "app")

    /// Logs a screen visit, e.g. "Screen visited: Settings".
    static func screen(_ name: String) {
        log.info("Screen visited: \(name, privacy: .public)")
    }

    /// Logs an app event, e.g. a transaction being added.
    static func event(_ message: String) {
        log.info("\(message, privacy: .public)")
    }
}
