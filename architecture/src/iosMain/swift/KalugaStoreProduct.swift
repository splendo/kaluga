//
//  SwiftClass.swift
//  KotlinCallbackFreeze
//
//  Created by Gijs van Veen on 15/08/2025.
//

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
        let wrapper = KalugaStoreProductViewControllerWrapper(delegate: delegate)
        controller.delegate = wrapper
        return wrapper
    }
    
    @objc public init(delegate: KalugaStoreProductViewControllerDelegate) {
        self.delegate = delegate
    }
    
    let delegate: KalugaStoreProductViewControllerDelegate
    
    public func productViewControllerDidFinish(_ viewController: SKStoreProductViewController) {
        
    }
}


