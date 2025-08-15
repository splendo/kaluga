#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol KalugaUIPopoverPresentationControllerDelegate
- (void)prepareForPopoverPresentation:(UIPopoverPresentationController *)popoverPresentationController;
@end

@interface KalugaUIPopoverPresentationControllerWrapper : NSObject <UIPopoverPresentationControllerDelegate>

+ (instancetype)createByLinkingWithController:(UIPopoverPresentationController *)controller
        to:(id<KalugaUIPopoverPresentationControllerDelegate>)delegate;

- (instancetype)initWithDelegate:(id<KalugaUIPopoverPresentationControllerDelegate>)delegate NS_DESIGNATED_INITIALIZER;

@end

NS_ASSUME_NONNULL_END
