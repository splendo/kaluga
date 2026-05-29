/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import AppKit
import KalugaExampleShared

/// Compose Multiplatform on macOS owns the NSApplication lifecycle and creates its own NSWindow,
/// so the entry point is a plain AppKit app rather than SwiftUI's `@main App`. The delegate boots
/// Koin and then asks the Kotlin layer to render the main window.
class AppDelegate: NSObject, NSApplicationDelegate {

    func applicationDidFinishLaunching(_ notification: Notification) {
        DependencyInjectionKt.doInitKoin(customModules: [])
        MainViewControllerKt.startMainWindow { feature in
            NSLog("Feature \(feature.name) is not available on macOS")
        }
    }

    func applicationShouldTerminateAfterLastWindowClosed(_ sender: NSApplication) -> Bool {
        return true
    }
}

let app = NSApplication.shared
let delegate = AppDelegate()
app.delegate = delegate
app.setActivationPolicy(.regular)
app.activate(ignoringOtherApps: true)
app.run()
