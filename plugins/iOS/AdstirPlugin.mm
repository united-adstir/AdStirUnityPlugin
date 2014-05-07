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
#import "AdstirMraidView.h"

extern UIViewController* UnityGetGLViewController();

extern "C"{
    void* _AdstirPlugin_show(const char *media,const char *spot, int x, int y, int w, int h){
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
        
        AdstirMraidView *instance = [[AdstirMraidView alloc] initWithAdSize:size origin:CGPointMake(x, y) media:[NSString stringWithUTF8String:(const char*)media] spot:[[NSString stringWithUTF8String:(const char*)spot] intValue]];
        [UnityGetGLViewController().view addSubview:instance];
        return (void*) instance;
    }
    
    void _AdstirPlugin_hide(void* viewinstance)
    {
        AdstirMraidView *instance = (AdstirMraidView *)viewinstance;
        [instance removeFromSuperview];
        [instance release];
    }
}
