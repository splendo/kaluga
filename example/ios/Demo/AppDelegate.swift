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

        return true
    }

    func application(_ application: UIApplication,
                     continue userActivity: NSUserActivity,
                     restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        // LinksViewController is gone — Links is now a CMP screen inside `MainViewController`'s
        // nav graph, and the CMP `LinksScreen` doesn't yet accept an incoming URL. We still
        // validate the universal link via kaluga.links so the deep-link path is exercised, and we
        // log the parsed payload. Routing the user to the Links screen on cold-launch can be
        // wired up by adding a `startRoute` parameter to the `MainViewController` factory.
        guard let stringUrl = userActivity.webpageURL?.absoluteString else {
            NSLog("Universal link arrived without a webpageURL")
            return false
        }
        let manager = DefaultLinksManager.Builder().create()
        if let validated = manager.validateLink(url: stringUrl) {
            NSLog("Kaluga deep link accepted: \(validated)")
            return true
        }
        NSLog("Kaluga deep link rejected: \(stringUrl)")
        return false
    }

    func applicationWillResignActive(_ application: UIApplication) {}

    func applicationDidEnterBackground(_ application: UIApplication) {}

    func applicationWillEnterForeground(_ application: UIApplication) {}

    func applicationDidBecomeActive(_ application: UIApplication) {}

    func applicationWillTerminate(_ application: UIApplication) {}
}
