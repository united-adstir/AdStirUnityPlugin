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

public class AdstirPlugin : MonoBehaviour
{
	
	public enum Layout {
		Top = 1,
		TopLeft,
		TopRight,
		Center,
		CenterLeft,
		CenterRight,
		Bottom,
		BottomLeft,
		BottomRight,
	}
	
	public void OnEnable()
	{
		// this.ShowAd("MEDIA-ID",SPOT-ID,0,0,320,50);
	}
	
	public void OnDisable()
	{
		// this.HideAd();
	}	
	
	#if UNITY_IPHONE
	IntPtr view = IntPtr.Zero;
	#elif UNITY_ANDROID
	AndroidJavaObject view;
	#endif
	
	#if UNITY_IPHONE
	[DllImport("__Internal")]
	private static extern IntPtr _AdstirPlugin_show(string media, int spot, int x, int y, int w, int h);
	[DllImport("__Internal")]
	private static extern void _AdstirPlugin_hide(IntPtr instance);
	[DllImport("__Internal")]
	private static extern float _AdstirPlugin_width();
	[DllImport("__Internal")]
	private static extern float _AdstirPlugin_height();
	#endif
	
	public void ShowAd(string media, int spot, int x, int y, int w, int h)
	{
		#if UNITY_IPHONE
		if (view != IntPtr.Zero) return;
		view = _AdstirPlugin_show(media,spot,x,y,w,h);
		#elif UNITY_ANDROID
		if (view != null) return;
		view = new AndroidJavaObject("com.adstir.unity.AdstirPlugin");
		view.Call("_AdstirPlugin_show", media,spot,x,y,w,h);
		#endif
	}
	
	public void ShowAd(string media, int spot, int w, int h, Layout layout)
	{
		float screenWidth = GetDisplayWidth ();
		float screenHeight = GetDisplayHeight ();
		float x = 0, y = 0;
		
		switch (layout) {
			
		case Layout.Bottom:
			x = screenWidth / 2 - w / 2;
			y = screenHeight - h;
			break;
			
		case Layout.BottomLeft:
			x = 0;
			y = screenHeight - h;
			break;
			
		case Layout.BottomRight:
			x = screenWidth - w;
			y = screenHeight - h;
			break;
			
		case Layout.Center:
			x = screenWidth / 2 - w / 2;
			y = screenHeight / 2 - h / 2;
			break;
			
		case Layout.CenterLeft:
			x = 0;
			y = screenHeight / 2 - h / 2;
			break;
			
		case Layout.CenterRight:
			x = screenWidth - w;
			y = screenHeight / 2 - h / 2;
			break;
			
		case Layout.Top:
			x = screenWidth / 2 - w / 2;
			y = 0;
			break;
			
		case Layout.TopLeft:
			x = 0;
			y = 0;
			break;
			
		case Layout.TopRight:
			x = screenWidth - w;
			y = 0;
			break;
			
		}
		
		ShowAd(media, spot, (int) x, (int) y, w, h);
	}
	
	public void HideAd()
	{
		#if UNITY_IPHONE
		if (view == IntPtr.Zero) return;
		_AdstirPlugin_hide(view);
		view = IntPtr.Zero;
		#elif UNITY_ANDROID
		if (view == null) return;
		view.Call("_AdstirPlugin_hide");
		view = null;
		#endif
	}
	
	public float GetDisplayWidth()
	{
		#if UNITY_IPHONE
		return _AdstirPlugin_width();
		#elif UNITY_ANDROID
		AndroidJavaClass adstirclass = new AndroidJavaClass("com.adstir.unity.AdstirPlugin");
		return adstirclass.CallStatic<float>("_AdstirPlugin_width");
		#endif
		return 0;
	}
	
	public float GetDisplayHeight()
	{
		#if UNITY_IPHONE
		return _AdstirPlugin_height();
		#elif UNITY_ANDROID
		AndroidJavaClass adstirclass = new AndroidJavaClass("com.adstir.unity.AdstirPlugin");
		return adstirclass.CallStatic<float>("_AdstirPlugin_height");
		#endif
		return 0;
	}
	
}
