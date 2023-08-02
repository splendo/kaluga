//
//  Copyright 2023 Splendo Consulting B.V. The Netherlands
// 
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//

import UIKit

enum MainStoryboard {
    private static let storyboard = UIStoryboard(name: "Main", bundle: nil)

    static func instantiateAlertsViewController() -> AlertsViewController {
        return instantiate(identifier: "AlertsViewController")
    }

    static func instantiateArchitectureViewController() -> ArchitectureViewController {
        return instantiate(identifier: "Architecture")
    }

    static func instantiateArchitectureDetailsViewController() -> ArchitectureDetailsViewController {
        return instantiate(identifier: "ArchitectureDetails")
    }

    static func instantiateBottomSheetViewController() -> BottomSheetViewController {
        return instantiate(identifier: "BottomSheet")
    }

    static func instantiateBottomSheetSubPageViewController() -> BottomSheetSubPageViewController {
        instantiate(identifier: "BottomSheetSubPage")
    }

    static func instantiateDateTimePickerViewController() -> DateTimePickerViewController {
        return instantiate(identifier: "DateTimePicker")
    }

    static func instantiateFeaturesListViewController() -> FeaturesListViewController {
        return instantiate(identifier: "FeaturesList")
    }

    static func instantiateInfoViewController() -> InfoViewController {
        return instantiate(identifier: "InfoViewController")
    }

    static func instantiateKeyboardManagerViewController() -> KeyboardManagerViewController {
        return instantiate(identifier: "KeyboardManagerViewController")
    }

    static func instantiateLinksViewController() -> LinksViewController {
        return instantiate(identifier: "LinksViewController")
    }

    static func instantiateLoadingViewController() -> LoadingViewController {
        return instantiate(identifier: "LoadingView")
    }

    static func instantiatePermissionViewController() -> PermissionViewController {
        return instantiate(identifier: "Permission")
    }

    static func instantiateResourcesListViewController() -> ResourcesListViewController {
        return instantiate(identifier: "ResourcesList")
    }

    static func instantiateSwiftUIOrUIKitSelectionViewController() -> SwiftUIOrUIKitSelectionViewController {
        return instantiate(identifier: "SwiftOrUIKit")
    }

    private static func instantiate<VC: UIViewController>(identifier: String) -> VC {
        // swiftlint:disable:next force_cast
        storyboard.instantiateViewController(withIdentifier: identifier) as! VC
    }
}
