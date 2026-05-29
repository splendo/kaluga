/*

Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import UIKit
import KalugaExampleShared
import KalugaMobileShared

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        DependencyInjectionKt.doInitKoin(customModules: [])

        let window = UIWindow(frame: UIScreen.main.bounds)
        window.rootViewController = UINavigationController(rootViewController: ExampleViewController())
        window.makeKeyAndVisible()
        self.window = window

        // Cold-launch from a universal link: UIKit hands the URL via launchOptions before the
        // Compose tree is up. Posting to the bus is safe — `AppRootScreen` reads the latest value
        // when it composes, so the link is delivered to LinksScreen on first nav.
        if let activity = launchOptions?[.userActivityDictionary] as? [AnyHashable: Any],
           let userActivity = activity.values.compactMap({ $0 as? NSUserActivity }).first,
           let url = userActivity.webpageURL?.absoluteString {
            DeepLinkBus.shared.postUrl(url: url)
        }
        if let url = launchOptions?[.url] as? URL {
            DeepLinkBus.shared.postUrl(url: url.absoluteString)
        }

        return true
    }

    func application(_ application: UIApplication,
                     continue userActivity: NSUserActivity,
                     restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        // Foreground/background universal-link arrival. CMP's `AppRootScreen` observes the bus and
        // navigates to LinksScreen automatically; LinksScreen runs the same
        // `LinksManager.handleIncomingLink(...)` validation the old `LinksViewModel` did.
        guard let stringUrl = userActivity.webpageURL?.absoluteString else {
            NSLog("Universal link arrived without a webpageURL")
            return false
        }
        DeepLinkBus.shared.postUrl(url: stringUrl)
        return true
    }

    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool {
        // Same path for custom-scheme `kalugaexample://` links (registered in Info.plist).
        DeepLinkBus.shared.postUrl(url: url.absoluteString)
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {}

    func applicationDidEnterBackground(_ application: UIApplication) {}

    func applicationWillEnterForeground(_ application: UIApplication) {}

    func applicationDidBecomeActive(_ application: UIApplication) {}

    func applicationWillTerminate(_ application: UIApplication) {}
}
