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

package com.adstir.unity;

import com.unity3d.player.UnityPlayer;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.util.TypedValue;
import android.util.DisplayMetrics;

public class AdstirPlugin
{
	private ViewGroup view;
	private ViewGroup adLayout;

	public AdstirPlugin()
	{
	}

	public void show(final String media,final int spot,final int x,final int y,final int w,final int h) {
		final Activity a = UnityPlayer.currentActivity;
		a.runOnUiThread(new Runnable() {public void run() {
			if (adLayout == null){
				adLayout = new FrameLayout(a);
				a.addContentView(adLayout, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.TOP|Gravity.LEFT));
			}
			if (view == null){
				DisplayMetrics metrics = a.getResources().getDisplayMetrics();
				int xdp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, metrics);
				int ydp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y, metrics);
				int wdp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w, metrics);
				int hdp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, metrics);
				view = new FrameLayout(a);
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(wdp, hdp, Gravity.TOP|Gravity.LEFT);
				lp.setMargins(xdp,ydp,0,0);
				view.setLayoutParams(lp);
				com.ad_stir.webview.AdstirMraidView adview = new com.ad_stir.webview.AdstirMraidView(a,media,spot,new com.ad_stir.webview.AdstirMraidView.AdSize(w,h),com.ad_stir.webview.AdstirMraidView.DEFAULT_INTERVAL);
				adview.setLayoutParams(new FrameLayout.LayoutParams(android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,android.widget.FrameLayout.LayoutParams.WRAP_CONTENT));
				view.addView(adview);
				adLayout.addView(view);
			}
		}});
	}

	public void hide() {
		final Activity a = UnityPlayer.currentActivity;
		a.runOnUiThread(new Runnable() {public void run() {
			if (adLayout != null && view != null) {
				adLayout.removeView(view);
				view = null;
			}
		}});
	}

}
