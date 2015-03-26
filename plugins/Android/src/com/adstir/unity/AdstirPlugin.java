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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

import com.ad_stir.common.Dip;
import com.ad_stir.interstitial.AdstirInterstitial;
import com.ad_stir.interstitial.AdstirInterstitial.AdstirInterstitialDialogListener;
import com.ad_stir.interstitial.AdstirInterstitial.AdstirInterstitialListener;
import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.util.Log;
import android.util.TypedValue;
import android.util.DisplayMetrics;

public class AdstirPlugin {

	private ViewGroup view;
	private ViewGroup adLayout;
	private ViewGroup iconView;
	private ViewGroup iconAdLayout;

	public AdstirPlugin() {

	}

	public void _AdstirPlugin_show(final String media, final int spot,
			final int x, final int y, final int w, final int h) {
		final Activity a = UnityPlayer.currentActivity;
		a.runOnUiThread(new Runnable() {
			public void run() {
				if (adLayout == null) {
					adLayout = new FrameLayout(a);
					a.addContentView(adLayout, new FrameLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, Gravity.TOP
									| Gravity.LEFT));
				}
				if (view == null) {
					DisplayMetrics metrics = a.getResources()
							.getDisplayMetrics();
					int xdp = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, x, metrics);
					int ydp = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, y, metrics);
					int wdp = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, w, metrics);
					int hdp = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, h, metrics);
					view = new FrameLayout(a);
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							wdp, hdp, Gravity.TOP | Gravity.LEFT);
					lp.setMargins(xdp, ydp, 0, 0);
					view.setLayoutParams(lp);
					com.ad_stir.webview.AdstirMraidView adview = new com.ad_stir.webview.AdstirMraidView(
							a,
							media,
							spot,
							new com.ad_stir.webview.AdstirMraidView.AdSize(w, h),
							com.ad_stir.webview.AdstirMraidView.DEFAULT_INTERVAL);
					adview.setLayoutParams(new FrameLayout.LayoutParams(
							android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
							android.widget.FrameLayout.LayoutParams.WRAP_CONTENT));
					view.addView(adview);
					adLayout.addView(view);
				}
			}
		});
	}

	public void _AdstirPlugin_hide() {
		final Activity a = UnityPlayer.currentActivity;
		a.runOnUiThread(new Runnable() {
			public void run() {
				if (adLayout != null && view != null) {
					adLayout.removeView(view);
					view = null;
				}
			}
		});
	}

	public void _AdstirIconPlugin_show(final String media, final int spot,
			final int x, final int y, final int w, final int h, final int slot,
			final boolean isCenter, final int interval) {
		final Activity a = UnityPlayer.currentActivity;
		a.runOnUiThread(new Runnable() {
			public void run() {
				int vertical = LayoutParams.WRAP_CONTENT;
				int horizontal = isCenter ? LayoutParams.MATCH_PARENT
						: LayoutParams.WRAP_CONTENT;
				if (iconAdLayout == null) {
					iconAdLayout = new FrameLayout(a);
					a.addContentView(iconAdLayout,
							new FrameLayout.LayoutParams(horizontal, vertical,
									Gravity.TOP | Gravity.LEFT));
				}
				if (iconView == null) {
					DisplayMetrics metrics = a.getResources()
							.getDisplayMetrics();
					int xdp = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, x, metrics);
					int ydp = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, y, metrics);
					int wdp = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, w, metrics);
					int hdp = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, h, metrics);
					iconView = new FrameLayout(a);

					FrameLayout.LayoutParams lp;
					if (isCenter) {
						lp = new FrameLayout.LayoutParams(horizontal, vertical);
					} else {
						lp = new FrameLayout.LayoutParams(wdp, hdp);
					}
					lp.setMargins(xdp, ydp, 0, 0);
					iconView.setLayoutParams(lp);

					com.ad_stir.icon.IconView adview = new com.ad_stir.icon.IconView(
							a, media, spot, slot);
					adview.setInterval(interval);
					adview.setCenter(isCenter);
					adview.setLayoutParams(new FrameLayout.LayoutParams(
							android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
							android.widget.FrameLayout.LayoutParams.WRAP_CONTENT));
					iconView.addView(adview);

					iconAdLayout.addView(iconView);
				}
			}
		});
	}

	public void _AdstirIconPlugin_hide() {
		final Activity a = UnityPlayer.currentActivity;
		a.runOnUiThread(new Runnable() {
			public void run() {
				if (iconAdLayout != null && iconView != null) {
					iconAdLayout.removeView(iconView);
					iconView = null;
					iconAdLayout = null;
				}
			}
		});
	}

	public void _AdstirInterstitialPlugin_load(String goname,
			final String media, final int spot, final String attrs) {

		AdstirUnityInterstitial.sharedInstance().gameObject = goname;
		Activity a = UnityPlayer.currentActivity;

		a.runOnUiThread(new Runnable() {
			public void run() {
				AdstirInterstitial instance = new AdstirInterstitial(media, spot);
				AdstirUnityInterstitial.sharedInstance().instance = instance;

				// Set AdstirInterstitial Parameters
				if (attrs.length() > 0) {
					HashMap<String, String> params = AdStir.parseLTSV(attrs);
					if (params.size() > 0)
						AdstirUnityInterstitial.sharedInstance().setParameters(params);
				}

				instance.setListener(new AdstirInterstitialListener() {
					@Override
					public void onReceiveSetting() {
						String gameObject = AdstirUnityInterstitial.sharedInstance().gameObject;
						UnityPlayer.UnitySendMessage(gameObject, "AdStir_OnReceiveSetting", gameObject);
					}

					@Override
					public void onFailedToReceiveSetting() {
						String gameObject = AdstirUnityInterstitial.sharedInstance().gameObject;
						UnityPlayer.UnitySendMessage(gameObject, "AdStir_OnReceiveFailedSetting", gameObject);
					}
				});

				instance.setDialoglistener(new AdstirInterstitialDialogListener() {
					@Override
					public void onPositiveButtonClick() {
						String gameObject = AdstirUnityInterstitial.sharedInstance().gameObject;
						UnityPlayer.UnitySendMessage(gameObject, "AdStir_OnDialogPositiveButtonClick", gameObject);
					}

					@Override
					public void onNegativeButtonClick() {
						String gameObject = AdstirUnityInterstitial.sharedInstance().gameObject;
						UnityPlayer.UnitySendMessage(gameObject, "AdStir_OnDialogNegativeButtonClick", gameObject);
					}

					@Override
					public void onCancel() {
						String gameObject = AdstirUnityInterstitial.sharedInstance().gameObject;
						UnityPlayer.UnitySendMessage(gameObject, "AdStir_OnDialogCancel", gameObject);
					}
				});
				instance.load();
			}
		});
	}

	public void _AdstirInterstitialPlugin_show(final int showType) {

		final Activity a = UnityPlayer.currentActivity;
		a.runOnUiThread(new Runnable() {
			public void run() {
				AdstirInterstitial instance = AdstirUnityInterstitial
						.sharedInstance().instance;
				switch (showType) {
				default:
				case 1:
					instance.showTypeA(a);
					break;
				case 2:
					instance.showTypeB(a);
					break;
				case 3:
					instance.showTypeC(a);
					break;
				}
			}
		});
	}

	public void _AdstirInterstitialPlugin_hide() {
		AdstirInterstitial instance = AdstirUnityInterstitial.sharedInstance().instance;
		instance.setListener(null);
		instance.setDialoglistener(null);
		instance = null;
	}
}

class AdstirUnityInterstitial {
	public static AdstirUnityInterstitial sharedInstance;
	public AdstirInterstitial instance;
	public String gameObject;

	public static AdstirUnityInterstitial sharedInstance() {
		if (sharedInstance == null) {
			sharedInstance = new AdstirUnityInterstitial();
		}
		return sharedInstance;
	}

	private String[] allowKeys = { "dialogText", "positiveButtonText",
			"negativeButtonText", "dialogTextColor", "dialogBackgroundColor",
			/* "dialogBorderColor", */"positiveButtonTextColor",
			"positiveButtonBackgroundColor", /* "positiveButtonBorderColor", */
			"negativeButtonTextColor", "negativeButtonBackgroundColor" /*  
			,"negativeButtonBorderColor" */};

	public void setParameters(HashMap<String, String> params) {
		Activity a = UnityPlayer.currentActivity;
		AdstirInterstitial instance = AdstirUnityInterstitial.sharedInstance().instance;

		for (String key : allowKeys) {
			if (params.containsKey(key)) {
				try {
					String methodName = "set"
							+ Character.toUpperCase(key.charAt(0))
							+ key.substring(1);
					if (key.endsWith("Text")) {
						Method method = instance.getClass().getMethod(
								methodName, new Class[] { String.class });
						method.invoke(instance,
								new Object[] { params.get(key) });
					} else if (key.endsWith("Color")) {
						try {
							Method method = instance.getClass().getMethod(
									methodName, new Class[] { int.class });
							method.invoke(instance, new Object[] { AdStir
									.HextoColor(params.get(key)) });							
						} catch (NoSuchMethodException e) {
							methodName = methodName.replaceFirst("Color$", "");
							if(key.equals("dialogBackgroundColor")) {

								int b = Dip.dipToPx(a, 2);
								int r = Dip.dipToPx(a, 10);
								int p = Dip.dipToPx(a, 10);
								float[] outerR = new float[] { r, r, r, r, r, r, r, r };
								ShapeDrawable fillDrawable = new ShapeDrawable(new RoundRectShape(outerR, null, null));
								fillDrawable.getPaint().setColor(AdStir.HextoColor(params.get(key)));
								fillDrawable.setPadding(new Rect(0, p, 0, p));

								ShapeDrawable borderDrawable = new ShapeDrawable(new RoundRectShape(outerR, null, null));
								Paint paint = borderDrawable.getPaint();
								if(params.containsKey("dialogBorderColor")) {
									paint.setColor(AdStir.HextoColor(params.get("dialogBorderColor")));
								}else {
									paint.setColor(Color.TRANSPARENT);
								}
								borderDrawable.setPadding(new Rect(b, b, b, b));

								LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{ borderDrawable, fillDrawable});
							    Method method = instance.getClass().getMethod(
										methodName, new Class[] { Drawable.class });
								method.invoke(instance, new Object[] { layerDrawable });
								
							}else if(key.equals("positiveButtonBackgroundColor") || key.equals("negativeButtonBackgroundColor")) {
								
								int p = Dip.dipToPx(a, 5);
								int b = Dip.dipToPx(a, 1);
								int r = Dip.dipToPx(a, 5);
								float[] outerR = new float[] { r, r, r, r, r, r, r, r };
								ShapeDrawable fillDrawable = new ShapeDrawable(new RoundRectShape(outerR, null, null));
								fillDrawable.getPaint().setColor(AdStir.HextoColor(params.get(key)));
								fillDrawable.setPadding(new Rect(p, p, p, p));

								ShapeDrawable borderDrawable = new ShapeDrawable(new RoundRectShape(outerR, null, null));
								Paint paint = borderDrawable.getPaint();
								if(key.equals("positiveButtonBackgroundColor") && params.containsKey("positiveButtonBorderColor")) {
									paint.setColor(AdStir.HextoColor(params.get("positiveButtonBorderColor")));
								}else if(key.equals("negativeButtonBackgroundColor") && params.containsKey("negativeButtonBorderColor")) {
									paint.setColor(AdStir.HextoColor(params.get("negativeButtonBorderColor")));
								}else {
									paint.setColor(Color.TRANSPARENT);
								}
								borderDrawable.setPadding(b, b, b, b);
								
								LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{ borderDrawable, fillDrawable});
							    Method method = instance.getClass().getMethod(
										methodName, new Class[] { Drawable.class });
								method.invoke(instance, new Object[] { layerDrawable });
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class AdStir {

	public static HashMap<String, String> parseLTSV(String ltsv) {
		HashMap<String, String> params = new HashMap<String, String>();
		for (String block : ltsv.split("\t")) {
			String[] param = block.split(":");
			try {
				params.put(param[0], param[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				params.put(param[0], null);
			}
		}
		return params;
	}

	public static int HextoColor(String hex) {
		String colorString = hex.toUpperCase(Locale.getDefault());
		// #ARGB #RGBの場合、変換する
		int len = colorString.length();
		if (colorString.startsWith("#") && colorString.length() >= 4
				&& colorString.length() <= 5) {
			StringBuilder color_ = new StringBuilder().append("#");
			String c = null;
			for (int i = 1; i < len; i++) {
				c = colorString.substring(i, i + 1);
				color_.append(c).append(c);
			}
			colorString = color_.toString();
		}
		// #AARRGGBB #RRGGBBの場合
		int color = Color.parseColor(colorString);
		return color;
	}
}

