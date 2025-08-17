//
//  SwiftClass.swift
//  KotlinCallbackFreeze
//
//  Created by Gijs van Veen on 15/08/2025.
//

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
        AnimationController: UIViewControllerAnimatedTransitioning
    ) -> UIViewControllerInteractiveTransitioning
    
    @objc func supportedInterfaceOrientations(navigationController: UINavigationController) -> UIInterfaceOrientationMask
    
    @objc func preferredInterfaceOrientationForPresentation(navigationController: UINavigationController) -> UIInterfaceOrientation
    
    @objc func finishPickingMedia(picker: UIImagePickerController, info: [UIImagePickerController.InfoKey : Any])
    
    @objc func didCancel(picker: UIImagePickerController)
}

@objc(KalugaUIImagePickerControllerWrapper)
public class KalugaUIImagePickerControllerWrapper : NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    @objc public static func createByLinking(controller: UIImagePickerController, to delegate: KalugaUIImagePickerControllerDelegate) -> KalugaUIImagePickerControllerWrapper {
        let wrapper = KalugaUIImagePickerControllerWrapper(delegate: delegate)
        controller.delegate = wrapper
        return wrapper
    }
    
    @objc public init(delegate: KalugaUIImagePickerControllerDelegate) {
        self.delegate = delegate
    }
    
    let delegate: KalugaUIImagePickerControllerDelegate
    
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

    public func navigationController(_ navigationController: UINavigationController, interactionControllerFor animationController: any UIViewControllerAnimatedTransitioning) -> (any UIViewControllerInteractiveTransitioning)? {
        return delegate.interactionController(navigationController: navigationController, AnimationController: animationController)
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
}


