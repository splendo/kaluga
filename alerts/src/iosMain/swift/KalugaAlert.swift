//
//  SwiftClass.swift
//  KotlinCallbackFreeze
//
//  Created by Gijs van Veen on 15/08/2025.
//

import Foundation
import UIKit

@objc(KalugaUIPopoverPresentationControllerDelegate)
public protocol KalugaUIPopoverPresentationControllerDelegate {
    @objc func prepareForPopoverPresentation(_ popoverPresentationController: UIPopoverPresentationController)
}

@objc(KalugaUIPopoverPresentationControllerWrapper)
public class KalugaUIPopoverPresentationControllerWrapper : NSObject, UIPopoverPresentationControllerDelegate {
    
    @objc public static func createByLinking(controller: UIPopoverPresentationController, to delegate: KalugaUIPopoverPresentationControllerDelegate) -> KalugaUIPopoverPresentationControllerWrapper {
        let wrapper = KalugaUIPopoverPresentationControllerWrapper(delegate: delegate)
        controller.delegate = wrapper
        return wrapper
    }
    
    @objc public init(delegate: KalugaUIPopoverPresentationControllerDelegate) {
        self.delegate = delegate
    }
    
    let delegate: KalugaUIPopoverPresentationControllerDelegate
    
    public func prepareForPopoverPresentation(_ popoverPresentationController: UIPopoverPresentationController) {
        delegate.prepareForPopoverPresentation(popoverPresentationController)
    }
}
