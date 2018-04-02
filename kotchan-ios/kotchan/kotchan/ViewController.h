#import <UIKit/UIKit.h>
#import <GLKit/GLKit.h>

@interface ViewController : GLKViewController

- (IBAction)handlePanGesture:(UIPanGestureRecognizer *)sender;
@property (strong, nonatomic) IBOutlet UIButton *gameCenterButton;

@end
