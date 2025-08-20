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

@objc(KalugaUIImagePickerControllerDelegate)
public protocol KalugaUIImagePickerControllerDelegate {
    @objc func willShow(navigationController: UINavigationController, viewController: UIViewController, animated: Bool)
    
    @objc func didShow(navigationController: UINavigationController, viewController: UIViewController, animated: Bool)
    
    @objc func animationController(
        navigationController: UINavigationController,
        forOperation: UINavigationController.Operation,
        fromViewController: UIViewController,
        toViewController: UIViewController
    ) -> UIViewControllerAnimatedTransitioning?
    
    @objc func interactionController(
        navigationController: UINavigationController,
        transitioning: UIViewControllerAnimatedTransitioning
    ) -> UIViewControllerInteractiveTransitioning?
    
    @objc func supportedInterfaceOrientations(navigationController: UINavigationController) -> UIInterfaceOrientationMask
    
    @objc func preferredInterfaceOrientationForPresentation(navigationController: UINavigationController) -> UIInterfaceOrientation
    
    @objc func finishPickingMedia(picker: UIImagePickerController, info: [UIImagePickerController.InfoKey : Any])
    
    @objc func didCancel(picker: UIImagePickerController)
}

@objc(KalugaUIImagePickerControllerWrapper)
public class KalugaUIImagePickerControllerWrapper : NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    @objc public static func createByLinking(controller: UIImagePickerController, to delegate: KalugaUIImagePickerControllerDelegate) -> KalugaUIImagePickerControllerWrapper {
        let wrapper = KalugaUIImagePickerControllerWrapper(delegate: delegate) {
            controller.delegate = nil
        }
        controller.delegate = wrapper
        return wrapper
    }
    
    @objc public init(delegate: KalugaUIImagePickerControllerDelegate, unlinkAction: @escaping () -> Void) {
        self.delegate = delegate
        self.unlinkAction = unlinkAction
    }
    
    let delegate: KalugaUIImagePickerControllerDelegate
    var unlinkAction: (() -> Void)?
    
    public func navigationController(_ navigationController: UINavigationController, willShow viewController: UIViewController, animated: Bool) {
        delegate.willShow(navigationController: navigationController, viewController: viewController, animated: animated)
    }

    public func navigationController(_ navigationController: UINavigationController, didShow viewController: UIViewController, animated: Bool) {
        delegate.didShow(navigationController: navigationController, viewController: viewController, animated: animated)
    }

    public func navigationControllerSupportedInterfaceOrientations(_ navigationController: UINavigationController) -> UIInterfaceOrientationMask {
        return delegate.supportedInterfaceOrientations(navigationController: navigationController)
    }

    public func navigationControllerPreferredInterfaceOrientationForPresentation(_ navigationController: UINavigationController) -> UIInterfaceOrientation {
        return delegate.preferredInterfaceOrientationForPresentation(navigationController: navigationController)
    }

    public func navigationController(_ navigationController: UINavigationController, transitioning transitioning: any UIViewControllerAnimatedTransitioning) -> (any UIViewControllerInteractiveTransitioning)? {
        return delegate.interactionController(navigationController: navigationController, transitioning: transitioning)
    }

    public func navigationController(_ navigationController: UINavigationController, animationControllerFor operation: UINavigationController.Operation, from fromVC: UIViewController, to toVC: UIViewController) -> (any UIViewControllerAnimatedTransitioning)? {
        return delegate.animationController(navigationController: navigationController, forOperation: operation, fromViewController: fromVC, toViewController: toVC)
    }
    
    public func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        delegate.finishPickingMedia(picker: picker, info: info)
    }

    public func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        delegate.didCancel(picker: picker)
    }
    
    @objc public func unlink() {
        if let unlinkAction = unlinkAction {
            unlinkAction()
        }
        unlinkAction = nil
    }
}
