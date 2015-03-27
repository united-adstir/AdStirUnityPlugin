/*
 * Copyright (C) 2011 Keijiro Takahashi
 * Copyright (C) 2012 GREE, Inc.
 * Copyright (C) 2013 UNITED, Inc.
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

using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;

public class AdstirIconPlugin : MonoBehaviour
{

	public void OnEnable()
	{
//		this.ShowAd("MEDIA-ID",SPOT-ID,0,0,320,75,アイコン数[整数],センタリング指定,広告リフレッシュ秒[整数]);
//		this.ShowAd("MEDIA-ID",SPOT-ID,0,0,320,75,4,true,60);
	}

	public void OnDisable()
	{
//		this.HideAd();
	}




#if UNITY_IPHONE
	IntPtr view = IntPtr.Zero;
#elif UNITY_ANDROID
	AndroidJavaObject view;
#endif
	
#if UNITY_IPHONE
	[DllImport("__Internal")]
	private static extern IntPtr _AdstirIconPlugin_show(string media, int spot, int x, int y, int w, int h, int slot, bool isCenter, int interval);
	[DllImport("__Internal")]
	private static extern void _AdstirIconPlugin_hide(IntPtr instance);
#endif

	public void ShowAd(string media, int spot, int x, int y, int w, int h, int slot, bool isCenter, int interval)
	{
#if UNITY_IPHONE
		if (view != IntPtr.Zero) return;
		view = _AdstirIconPlugin_show(media,spot,x,y,w,h,slot,isCenter,interval);
#elif UNITY_ANDROID
		if (view != null) return;
		view = new AndroidJavaObject("com.adstir.unity.AdstirPlugin");
		view.Call("_AdstirIconPlugin_show", media,spot,x,y,w,h,slot,isCenter,interval);
#endif
	}

	public void HideAd()
	{
#if UNITY_IPHONE
		if (view == IntPtr.Zero) return;
		_AdstirIconPlugin_hide(view);
		view = IntPtr.Zero;
#elif UNITY_ANDROID
		if (view == null) return;
		view.Call("_AdstirIconPlugin_hide");
		view = null;
#endif
	}


	public float GetDisplayWidth()
	{
#if UNITY_IPHONE
#elif UNITY_ANDROID
		AndroidJavaClass adstirclass = new AndroidJavaClass("com.adstir.unity.AdstirPlugin");
		return adstirclass.CallStatic<float>("_AdstirPlugin_width");
#endif
		return 0;
	}
	
	public float GetDisplayHeight()
	{
#if UNITY_IPHONE
#elif UNITY_ANDROID
		AndroidJavaClass adstirclass = new AndroidJavaClass("com.adstir.unity.AdstirPlugin");
		return adstirclass.CallStatic<float>("_AdstirPlugin_height");
#endif
		return 0;
	}


}
