/*
 * Copyright (C) 2011 Keijiro Takahashi
 * Copyright (C) 2012 GREE, Inc.
 * Copyright (C) 2013-2014 UNITED, Inc.
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

#import <UIKit/UIKit.h>
#import <AdstirAds/AdstirAds.h>

@interface AdstirUnityInterstitial : NSObject
+ (AdstirUnityInterstitial*) sharedInstance;
+ (void) setParameters: (NSMutableDictionary*) params;
@property (nonatomic, strong) AdstirInterstitial* instance;
@property (nonatomic, retain) NSString* gameObject;
@end

@interface AdStir : NSObject
+ (NSMutableDictionary *) parseLTSV : (NSString *) ltsv;
+ (UIColor *) colorWithHexString: (NSString *) hexString;
+ (CGFloat) colorComponentFrom: (NSString *) string start: (NSUInteger) start length: (NSUInteger) length;
@end

@interface InterstitialDelegate : NSObject<AdstirInterstitialDialogDelegate, AdstirInterstitialDelegate>
- (void)adstirInterstitialDidReceiveSetting:(AdstirInterstitial *)inter;
- (void)adstirInterstitialDidFailedToReceiveSetting:(AdstirInterstitial *)inter;
- (void)adstirInterstitialDialogPositiveButtonClick:(AdstirInterstitial *)inter;
- (void)adstirInterstitialDialogNegativeButtonClick:(AdstirInterstitial *)inter;
- (void)adstirInterstitialDialogCancel:(AdstirInterstitial *)inter;
@end

@implementation AdstirUnityInterstitial : NSObject

+ (AdstirUnityInterstitial*) sharedInstance {
    static AdstirUnityInterstitial* sharedInstance;
    static dispatch_once_t once;
    dispatch_once( &once, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

+ (void) setParameters: (NSMutableDictionary*) params {
    AdstirInterstitial* instance = [AdstirUnityInterstitial sharedInstance].instance;
    NSArray* allKeys = params.allKeys;
    NSArray* allowKeys = [NSArray arrayWithObjects:@"dialogText", @"positiveButtonText", @"negativeButtonText", @"dialogTextColor", @"dialogBackgroundColor", @"dialogBorderColor", @"positiveButtonTextColor", @"positiveButtonBackgroundColor", @"positiveButtonBorderColor", @"negativeButtonTextColor", @"negativeButtonBackgroundColor", @"negativeButtonBorderColor", nil];
    for (NSString* key in allowKeys) {
        if([allKeys containsObject:key]) {
            SEL selector = NSSelectorFromString([NSString stringWithFormat: @"set%@%@:",
                                                 [[key substringWithRange:NSMakeRange(0, 1)] uppercaseString],
                                                 [key substringFromIndex:1]]);
            NSString* value = [params objectForKey:key];
            if([key hasSuffix:@"Color"]) {
                [instance performSelector:selector withObject:[AdStir colorWithHexString:value]];
            }else if([key hasSuffix:@"Text"]) {
                if([value isEqualToString:@""] || value == NULL) {
                    value = nil;
                }
                [instance performSelector:selector withObject:value];
            }
        }
    }
}

@end

@implementation AdStir: NSObject

+ (NSMutableDictionary *) parseLTSV : (NSString *) ltsv {
    NSMutableDictionary* params = [[[NSMutableDictionary alloc] init] autorelease];
    for (NSString* block in [ltsv componentsSeparatedByString:@"\t"]) {
        NSArray* param = [block componentsSeparatedByString:@":"];
        [params setValue:param[1] forKey:param[0]];
    }
    return params;
}

+ (UIColor *) colorWithHexString: (NSString *) hexString {
    NSString *colorString = [[hexString stringByReplacingOccurrencesOfString: @"#" withString: @""] uppercaseString];
    CGFloat alpha, red, blue, green;
    switch ([colorString length]) {
        case 3: // #RGB
            alpha = 1.0f;
            red   = [AdStir colorComponentFrom: colorString start: 0 length: 1];
            green = [AdStir colorComponentFrom: colorString start: 1 length: 1];
            blue  = [AdStir colorComponentFrom: colorString start: 2 length: 1];
            break;
        case 4: // #ARGB
            alpha = [AdStir colorComponentFrom: colorString start: 0 length: 1];
            red   = [AdStir colorComponentFrom: colorString start: 1 length: 1];
            green = [AdStir colorComponentFrom: colorString start: 2 length: 1];
            blue  = [AdStir colorComponentFrom: colorString start: 3 length: 1];
            break;
        case 6: // #RRGGBB
            alpha = 1.0f;
            red   = [AdStir colorComponentFrom: colorString start: 0 length: 2];
            green = [AdStir colorComponentFrom: colorString start: 2 length: 2];
            blue  = [AdStir colorComponentFrom: colorString start: 4 length: 2];
            break;
        case 8: // #AARRGGBB
            alpha = [AdStir colorComponentFrom: colorString start: 0 length: 2];
            red   = [AdStir colorComponentFrom: colorString start: 2 length: 2];
            green = [AdStir colorComponentFrom: colorString start: 4 length: 2];
            blue  = [AdStir colorComponentFrom: colorString start: 6 length: 2];
            break;
        default:
            [NSException raise:@"Invalid color value" format: @"Color value %@ is invalid.  It should be a hex value of the form #RBG, #ARGB, #RRGGBB, or #AARRGGBB", hexString];
            break;
    }
    return [UIColor colorWithRed: red green: green blue: blue alpha: alpha];
}

+ (CGFloat) colorComponentFrom: (NSString *) string start: (NSUInteger) start length: (NSUInteger) length {
    NSString *substring = [string substringWithRange: NSMakeRange(start, length)];
    NSString *fullHex = length == 2 ? substring : [NSString stringWithFormat: @"%@%@", substring, substring];
    unsigned hexComponent;
    [[NSScanner scannerWithString: fullHex] scanHexInt: &hexComponent];
    return hexComponent / 255.0;
}
@end


@implementation InterstitialDelegate: NSObject


- (void)adstirInterstitialDidReceiveSetting:(AdstirInterstitial *)inter {
    const char* gameObject = [[AdstirUnityInterstitial sharedInstance].gameObject UTF8String];
    UnitySendMessage(gameObject, "AdStir_OnReceiveSetting", gameObject);
}
- (void)adstirInterstitialDidFailedToReceiveSetting:(AdstirInterstitial *)inter {
    const char* gameObject = [[AdstirUnityInterstitial sharedInstance].gameObject UTF8String];
    UnitySendMessage(gameObject, "AdStir_OnReceiveFailedSetting", gameObject);
}
- (void)adstirInterstitialDialogPositiveButtonClick:(AdstirInterstitial *)inter {
    const char* gameObject = [[AdstirUnityInterstitial sharedInstance].gameObject UTF8String];
    UnitySendMessage(gameObject, "AdStir_OnDialogPositiveButtonClick", gameObject);
}
- (void)adstirInterstitialDialogNegativeButtonClick:(AdstirInterstitial *)inter {
    const char* gameObject = [[AdstirUnityInterstitial sharedInstance].gameObject UTF8String];
    UnitySendMessage(gameObject, "AdStir_OnDialogNegativeButtonClick", gameObject);
}
- (void)adstirInterstitialDialogCancel:(AdstirInterstitial *)inter {
    const char* gameObject = [[AdstirUnityInterstitial sharedInstance].gameObject UTF8String];
    UnitySendMessage(gameObject, "AdStir_OnDialogCancel", gameObject);
}

@end

extern UIViewController* UnityGetGLViewController();

extern "C" {
    
    void* _AdstirPlugin_show(const char *media, int spot, int x, int y, int w, int h)
    {
        AdstirAdSize size;
        if (320 == w && 50 == h) {
            size = kAdstirAdSize320x50;
        } else if(300 == w && 250 == h) {
            size = kAdstirAdSize300x250;
        } else if(300 == w && 100 == h) {
            size = kAdstirAdSize300x100;
        } else if(320 == w && 100 == h) {
            size = kAdstirAdSize320x100;
        } else {
            size.size.width = w, size.size.height = h;
            size.flags = 1;
        }
        
        AdstirMraidView *instance = [[AdstirMraidView alloc] initWithAdSize:size origin:CGPointMake(x, y) media:[NSString stringWithUTF8String:(const char*)media] spot:spot];
        [UnityGetGLViewController().view addSubview:instance];
        return (void*) instance;
    }
    
    void _AdstirPlugin_hide(void* viewinstance)
    {
        AdstirMraidView *instance = (AdstirMraidView *)viewinstance;
        instance.delegate = nil;
        [instance removeFromSuperview];
        [instance release];
    }
    
    void* _AdstirInterstitialPlugin_load(const char *gameObject, const char *media, int spot, const char *ltsv)
    {
        AdstirInterstitial *instance = [[[AdstirInterstitial alloc] init] autorelease];
        [AdstirUnityInterstitial sharedInstance].instance = instance;
        
        [AdstirUnityInterstitial sharedInstance].gameObject = [NSString stringWithUTF8String:(const char*) gameObject];
        instance.media = [NSString stringWithUTF8String:(const char*) media];
        instance.spot = spot;
        
        // Set AdstirInterstitial Parameters
        NSString* ltsvString = [NSString stringWithUTF8String:(const char*) ltsv];
        if (ltsvString.length > 0) {
            NSMutableDictionary* params = (NSMutableDictionary*) [AdStir parseLTSV:ltsvString];
            if([params count] > 0) [AdstirUnityInterstitial setParameters:params];
        }
        // Set Delegate
        InterstitialDelegate* delegate = [[InterstitialDelegate alloc] init];
        instance.delegate = delegate;
        instance.dialogDelegate = delegate;
        
        [instance load];
        return (void*) instance;
    }
    
    void _AdstirInterstitialPlugin_show(int showType)
    {
        AdstirInterstitial *instance = [AdstirUnityInterstitial sharedInstance].instance;
        switch (showType) {
            default:
            case 1:
                [instance showTypeA:UnityGetGLViewController()];
                break;
            case 2:
                [instance showTypeB:UnityGetGLViewController()];
                break;
            case 3:
                [instance showTypeC:UnityGetGLViewController()];
                break;
        }
    }
    
    void _AdstirInterstitialPlugin_hide()
    {
        AdstirInterstitial *instance = [AdstirUnityInterstitial sharedInstance].instance;
        instance.delegate = nil;
        instance.dialogDelegate = nil;
        [AdstirUnityInterstitial sharedInstance].instance = nil;
    }
    
    float _AdstirPlugin_width()
    {
        return UnityGetGLViewController().view.frame.size.width;
    }
    
    float _AdstirPlugin_height()
    {
        return UnityGetGLViewController().view.frame.size.height;
    }
}
