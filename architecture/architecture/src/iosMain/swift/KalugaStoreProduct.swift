/*

Copyright 2025 Splendo Consulting B.V. The Netherlands

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

import Foundation
import UIKit
import StoreKit

@objc(KalugaStoreProductViewControllerDelegate)
public protocol KalugaStoreProductViewControllerDelegate {
    @objc func didFinish(_ viewController: SKStoreProductViewController)
}

@objc(KalugaStoreProductViewControllerWrapper)
public class KalugaStoreProductViewControllerWrapper : NSObject, SKStoreProductViewControllerDelegate {
    
    @objc public static func createByLinking(controller: SKStoreProductViewController, to delegate: KalugaStoreProductViewControllerDelegate) -> KalugaStoreProductViewControllerWrapper {
        let wrapper = KalugaStoreProductViewControllerWrapper(delegate: delegate) {
            controller.delegate = nil
        }
        controller.delegate = wrapper
        return wrapper
    }
    
    @objc public init(delegate: KalugaStoreProductViewControllerDelegate, unlinkAction: @escaping () -> Void) {
        self.delegate = delegate
        self.unlinkAction = unlinkAction
    }
    
    let delegate: KalugaStoreProductViewControllerDelegate
    var unlinkAction: (() -> Void)?
    
    public func productViewControllerDidFinish(_ viewController: SKStoreProductViewController) {
        delegate.didFinish(viewController)
    }
    
    @objc public func unlink() {
        if let unlinkAction = unlinkAction {
            unlinkAction()
        }
        unlinkAction = nil
    }
}


