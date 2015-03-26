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

public class AdstirInterstitialPlugin : MonoBehaviour
{
	public enum ShowType {
		A = 1, // popup
		B,     // dialog 
		C,     // fullscreen
	}
	public ShowType showType = ShowType.A;
	
	public void OnEnable()
	{
		// 		name = "AdstirInterstitialPlugin";
		//		Dictionary<string, string> attrs = new Dictionary<string, string>();
		//		attrs.Add("dialogText", "ダイアログテキスト");
		// 		attrs.Add("positiveButtonText", null);
		//		attrs.Add("negativeButtonText", "NG");
		//		attrs.Add("dialogTextColor", "#FF0000");
		//		attrs.Add("dialogBackgroundColor", "#FFF");
		//		attrs.Add("dialogBorderColor", "#888");
		//		attrs.Add("positiveButtonTextColor", "#0000FF");
		//		attrs.Add("positiveButtonBackgroundColor", "#8000");
		//		attrs.Add("positiveButtonBorderColor", "#80FF0000");
		//		attrs.Add("negativeButtonTextColor", "#FFFFFF");
		//		attrs.Add("negativeButtonBackgroundColor", "#8000");
		//		attrs.Add("negativeButtonBorderColor", "#80FF0000");
		//		this.LoadAd(name, "MEDIA-ID", SPOT-ID, attrs);
	}
	
	public void OnDisable()
	{
		//		this.HideAd();
	}
	
	public void AdStir_OnReceiveSetting()
	{
		Debug.Log ("OnReceiveSetting");
	}
	
	public void AdStir_OnReceiveFailedSetting()
	{
		Debug.Log ("OnReceiveFailedSetting");
	}
	
	public void AdStir_OnDialogPositiveButtonClick()
	{
		Debug.Log ("OnDialogPositiveButtonClick");
	}
	
	public void AdStir_OnDialogNegativeButtonClick()
	{
		Debug.Log ("OnDialogNegativeButtonClick");
	}
	
	public void AdStir_OnDialogCancel(string message)
	{
		Debug.Log ("OnDialogCancel");
	}
	
	#if UNITY_IPHONE
	IntPtr view = IntPtr.Zero;
	#elif UNITY_ANDROID
	AndroidJavaObject view;
	#endif
	
	#if UNITY_IPHONE
	[DllImport("__Internal")]
	private static extern IntPtr _AdstirInterstitialPlugin_load(string goname, string media, int spot, string ltsv);
	[DllImport("__Internal")]
	private static extern void _AdstirInterstitialPlugin_show(int showType);
	[DllImport("__Internal")]
	private static extern void _AdstirInterstitialPlugin_hide();
	#endif
	
	public void LoadAd(string goname, string media, int spot, Dictionary<string, string> attrs)
	{
		#if UNITY_IPHONE
		if (view != IntPtr.Zero) return;
		string ltsv = AdStir.LTSV.fromDictionary(attrs);
		view = _AdstirInterstitialPlugin_load(goname, media, spot, ltsv);
		#elif UNITY_ANDROID
		if (view != null) return;
		string ltsv = AdStir.LTSV.fromDictionary(attrs);
		view = new AndroidJavaObject("com.adstir.unity.AdstirPlugin");
		view.Call("_AdstirInterstitialPlugin_load", goname, media, spot, ltsv);
		#endif
	}
	
	public void ShowAd()
	{
		#if UNITY_IPHONE
		_AdstirInterstitialPlugin_show((int) showType);
		#elif UNITY_ANDROID
		view = new AndroidJavaObject("com.adstir.unity.AdstirPlugin");
		view.Call("_AdstirInterstitialPlugin_show", (int) showType);
		#endif
	}
	
	public void HideAd()
	{
		#if UNITY_IPHONE
		if (view == IntPtr.Zero) return;
		_AdstirInterstitialPlugin_hide();
		view = IntPtr.Zero;
		#elif UNITY_ANDROID
		if (view == null) return;
		view.Call("_AdstirInterstitialPlugin_hide");
		view = null;
		#endif
	}
	
}
