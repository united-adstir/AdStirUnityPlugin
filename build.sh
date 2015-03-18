#!/bin/sh

UNITY="/Applications/Unity/Unity.app/Contents"
UNITYBIN="$UNITY/MacOS/Unity"
UNITYLIBS="$UNITY/PlaybackEngines/AndroidPlayer/release/bin/classes.jar"
cd `dirname $0`
BASEDIR=`pwd`

rm -rf build
mkdir build

# Android make JAR

cd "$BASEDIR/plugins/Android"

cp -a $UNITYLIBS libs
cp -a "$BASEDIR/AdStirSDK/adstirwebview.jar" libs

android update project -p .
ant release

cd "$BASEDIR"

# Android

mkdir -p "$BASEDIR/build/Assets/Plugins/Android"
cp -a "$BASEDIR/plugins/Android/bin/classes.jar" "$BASEDIR/build/Assets/Plugins/Android/AdstirPlugin.jar"
cp -a "$BASEDIR/plugins/Android/Adstir-AndroidManifest-Merge.xml" "$BASEDIR/build/Assets/Plugins/Android/Adstir-AndroidManifest-Merge.xml"
cp -a "$BASEDIR/plugins/Android/Adstir-AndroidManifest-Full.xml" "$BASEDIR/build/Assets/Plugins/Android/Adstir-AndroidManifest-Full.xml"
cp -a "$BASEDIR/AdStirSDK/adstirwebview.jar" "$BASEDIR/build/Assets/Plugins/Android/adstirwebview.jar"

# iOS

mkdir -p "$BASEDIR/build/Assets/Plugins/iOS"
cp -a "$BASEDIR/plugins/iOS/AdstirPlugin.mm" "$BASEDIR/build/Assets/Plugins/iOS/AdstirPlugin.mm"
cp -a "$BASEDIR/AdStirSDK/AdstirAds.framework" "$BASEDIR/build/Assets/Plugins/iOS/AdstirAds.framework"

# 共通

cp -a "$BASEDIR/plugins/AdstirPlugin.cs" "$BASEDIR/build/Assets/Plugins/AdstirPlugin.cs"

cd "$BASEDIR/build"
$UNITYBIN -projectPath "$BASEDIR/build" -quit -batchmode -exportPackage Assets Adstir.unityPackage -cleanedLogFile build.log

mkdir -p "$BASEDIR/dist"
cp "$BASEDIR/build/Adstir.unityPackage" "$BASEDIR/dist/Adstir.unityPackage"

