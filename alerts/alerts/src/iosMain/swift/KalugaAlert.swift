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

@objc(KalugaUIPopoverPresentationControllerDelegate)
public protocol KalugaUIPopoverPresentationControllerDelegate {
    @objc func prepareForPopoverPresentation(_ popoverPresentationController: UIPopoverPresentationController)
}

@objc(KalugaUIPopoverPresentationControllerWrapper)
public class KalugaUIPopoverPresentationControllerWrapper : NSObject, UIPopoverPresentationControllerDelegate {
    
    @objc public static func createByLinking(controller: UIPopoverPresentationController, to delegate: KalugaUIPopoverPresentationControllerDelegate) -> KalugaUIPopoverPresentationControllerWrapper {
        let wrapper = KalugaUIPopoverPresentationControllerWrapper(delegate: delegate) {
            controller.delegate = nil
        }
        controller.delegate = wrapper
        return wrapper
    }
    
    @objc public init(delegate: KalugaUIPopoverPresentationControllerDelegate, unlinkAction: @escaping () -> Void) {
        self.delegate = delegate
        self.unlinkAction = unlinkAction
    }
    
    let delegate: KalugaUIPopoverPresentationControllerDelegate
    var unlinkAction: (() -> Void)?
    
    public func prepareForPopoverPresentation(_ popoverPresentationController: UIPopoverPresentationController) {
        delegate.prepareForPopoverPresentation(popoverPresentationController)
    }
    
    @objc public func unlink() {
        if let unlinkAction = unlinkAction {
            unlinkAction()
        }
        unlinkAction = nil
    }
}
