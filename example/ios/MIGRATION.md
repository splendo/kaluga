# iOS Xcode migration after the `:shared` → `:shared` + `:mobileshared` split

The Kotlin/CMP refactor changed which framework hosts which ViewModel, deleted a number of Kotlin
classes the iOS app used to import, and replaced the navigation root with a Compose Multiplatform
`UIViewController`. This document tells you exactly what to change in `ios/Demo.xcodeproj` so the
iOS app builds again.

Do all of this inside Xcode (or via a `.pbxproj` editor of your choice). The Kotlin side is already
done — running `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64 :mobileshared:linkDebugFrameworkIosSimulatorArm64`
produces both frameworks the iOS app needs to link.

## 1. Link the new `KalugaMobileShared.framework`

In the Xcode project, **General → Frameworks, Libraries, and Embedded Content**:

- Drag `example/mobileshared/build/bin/iosSimulatorArm64/debugFramework/KalugaMobileShared.framework`
  (or whichever variant matches the destination you build for) into the list.
- Make sure it's set to **Embed & Sign**, the same as `KalugaExampleShared.framework`.
- In **Build Settings → Framework Search Paths**, add the four parent directories
  (Debug/Release × Arm64 device/Sim) — mirror what's there for `KalugaExampleShared`.

## 2. Delete the Swift files that backed migrated features

These Swift files referenced Kotlin types that no longer exist (the ViewModels / Navigators were
removed in Phase 7 because the features are now rendered by CMP in `:shared`). Remove the files
**and** their references in the Xcode project navigator:

```
ios/Demo/ExampleViewController.swift             ← replaced by the CMP host below
ios/Demo/FeaturesList/FeaturesListViewController.swift
ios/Demo/Info/InfoViewController.swift
ios/Demo/DateTime/TimerView.swift
ios/Demo/Links/LinksViewController.swift
ios/Demo/Location/LocationViewController.swift
ios/Demo/Permissions/PermissionListViewController.swift
ios/Demo/Permissions/PermissionViewController.swift
ios/Demo/Scientific/ScientificView.swift
ios/Demo/Scientific/ScientificConverterView.swift
ios/Demo/Scientific/ScientificUnitSelectionView.swift
ios/Demo/System/SystemViewController.swift
ios/Demo/System/Network/NetworkViewController.swift
ios/Demo/Bluetooth/BluetoothListView.swift
ios/Demo/Bluetooth/Client/BluetoothDeviceListView.swift
ios/Demo/Bluetooth/Client/BluetoothDeviceView.swift
ios/Demo/Bluetooth/Server/BluetoothServerView.swift
```

If any of those folders end up empty after the deletions, you can remove the group from the project
navigator too.

If `UITypeSelection/SwiftUIOrUIKitSelectionViewController.swift` is still referenced from any
remaining screen (it picked between SwiftUI and UIKit demos), audit those callers — that
`SwiftUIOrUIKitSelectionViewModel` now lives under `mobileshared.viewmodel.ui.*`, so the file likely
still works after an `import` swap (see step 4) and you can keep it.

## 3. Replace `ExampleViewController.swift` with a CMP host

`ExampleViewController` was the storyboard root that hosted `FeaturesListViewController` and
`InfoViewController` as tabs. Replace its body with a thin wrapper around the Kotlin
`MainViewController` factory exported from `KalugaExampleShared`:

```swift
// ios/Demo/ExampleViewController.swift

import UIKit
import KalugaExampleShared
import KalugaMobileShared

/// Hosts the Compose Multiplatform root from `:shared`. Features that are not yet migrated to CMP
/// (Alerts, Architecture, Beacons, DateTimePicker, Keyboard, LoadingIndicator, Media, Resources)
/// arrive via `onUnmigratedFeatureSelected` and are launched as their existing native
/// `UIViewController`s from `:mobileshared`.
class ExampleViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let host = MainViewControllerKt.MainViewController { [weak self] feature in
            self?.launchMobileFeature(feature)
        }
        addChild(host)
        host.view.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(host.view)
        NSLayoutConstraint.activate([
            host.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            host.view.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            host.view.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            host.view.bottomAnchor.constraint(equalTo: view.bottomAnchor),
        ])
        host.didMove(toParent: self)
    }

    private func launchMobileFeature(_ feature: Feature) {
        let target: UIViewController
        switch feature {
        case .alerts: target = AlertsViewController()
        case .architecture: target = ArchitectureViewController()
        case .beacons: target = BeaconsViewController()
        case .datetimepicker: target = DateTimePickerViewController()
        case .keyboard: target = KeyboardManagerViewController()
        case .loadingindicator: target = LoadingViewController()
        case .media: target = MediaListViewController()
        case .resources: target = ResourcesListViewController()
        default:
            assertionFailure("Feature \(feature.name) is supposed to be handled inside CMP")
            return
        }
        navigationController?.pushViewController(target, animated: true)
    }
}
```

Note: this assumes the iOS app's window root is a `UINavigationController` wrapping
`ExampleViewController`. If the storyboard previously had a `UITabBarController` here, you can drop
that — the tab UI is inside the CMP `AppRootScreen` now.

## 4. Update `import` lines in the Swift files that stay

The ViewModels behind these screens moved from `KalugaExampleShared` to `KalugaMobileShared`. In each
of the files below, add or replace `import KalugaExampleShared` with `import KalugaMobileShared`
(import both if the file references both frameworks):

```
ios/Demo/Alerts/*.swift
ios/Demo/Architecture/*.swift
ios/Demo/Beacons/*.swift
ios/Demo/DateTimePicker/*.swift
ios/Demo/KeyboardManager/*.swift
ios/Demo/LoadingIndicator/*.swift
ios/Demo/Media/*.swift
ios/Demo/Resources/*.swift
ios/Demo/UITypeSelection/*.swift
```

Swap can be done with a single project-wide find-and-replace once both frameworks are linked.

## 5. AppDelegate — Koin bootstrap

`DependencyInjectionKt.doInitKoin(customModules:)` now lives in `KalugaMobileShared` (it bundles the
mobile-only Koin module on top of `:shared`'s). Update `AppDelegate.swift`:

```swift
import KalugaMobileShared       // was KalugaExampleShared
...
func application(_ application: UIApplication, didFinishLaunchingWithOptions ...) -> Bool {
    DependencyInjectionKt.doInitKoin(customModules: [])
    return true
}
```

`KalugaExampleShared` still exports its own `initKoin(...)` (called internally by
`KalugaMobileShared`), but the iOS app should use the mobile one so the mobileshared ViewModels are
registered.

## 6. Smoke test

1. `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64 :mobileshared:linkDebugFrameworkIosSimulatorArm64`
2. Build the **Demo** scheme in Xcode against an Apple-silicon iOS simulator.
3. On launch you should see the CMP `Features` / `About` tab bar from `AppRootScreen`. Tapping
   any of the macOS-capable features (Links, System → Network, DateTime, Permissions, Location,
   Bluetooth, Scientific, Info) drills into the CMP screen. Tapping a mobile-only feature pushes the
   existing native UIKit/SwiftUI screen (Alerts, Architecture, Beacons, DateTimePicker, Keyboard,
   LoadingIndicator, Media, Resources).

If anything fails to compile after the import swap, the most likely culprit is a leftover reference
to one of the deleted Kotlin types — re-check step 2 and the file is in the **Project navigator**.
