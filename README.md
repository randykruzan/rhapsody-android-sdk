# Rhapsody Android SDK

## Current Version
The current version of the Rhapsody Android SDK is 1.1

## Introduction
The Rhapsody Android SDK was designed to provide a very easy way to integrate streaming music into your Android application. The SDK itself handles playback and is used in conjunction with the [Rhapsody Developer API](http://developer.rhapsody.com) to give your users access to over 18 million tracks. The SDK plays full-length tracks for authenticated Rhapsody subscribers.

We have provided here the SDK (rhapsodysdk.jar) and a sample application which provides examples you can use to build your own app with Rhapsody streaming music.


## Rhapsody SDK

#### Android Version Support
The Rhapsody Android SDK supports Android 4.0 (API 14) and up.

#### SDK Requirements
- Android SDK 14 or higher

#### Download
The Rhapsody Android SDK is available as a jar. You can download the rhapsodysdk.jar and add it to your Android Studio project gradle.build file:

```groovy
compile files('libs/rhapsodysdk.jar')
```

## Rhapsody SDK Sample App

#### Sample App Requirements
- AndroidStudio 1.0 or higher
- Android SDK 14 or higher
- Android BuildTools 21.1.2 (Recommend using latest available)


Android Studio will pull down these additional dependencies:
- Picasso
- Retrofit
- gson


#### Quickstart

Using Import Project in Android Studio, select the SampleProject folder. After importing, run a Gradle Sync, and you should be ready to build and deploy the sample apps to your device. 

You might be asked to install additional components such as build tools. You may either install them or change the required version in the build.gradle to one you have installed already.


#### Playing a track
1. include following permissions
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
2. add playback service <service android:name="com.rhapsody.player.RhapsodyPlaybackService" />
3. Rhapsody.register with your api key and secret (see https://developer.rhapsody.com/)

#### Loggin in
4. Setup OAuth(see https://developer.rhapsody.com/api#authentication) and use rhapsody.getLoginUrl() to get to a login form
5. token = new AuthToken(accessToken, refreshToken, expiresIn)
6. rhapsody.getSessionManager().openSession(token, mySessionCallback)
7. You probably want to listen to playback state changes and errors: rhapsody.getPlayer().addStateListener(myPlayerStateListener)
8. Play a track with player.play(trackId)

#### Extras
If you want to use transport controls in the notification:
. make your notification layouts (expanded, standard or both)
. implement AbstractNotificationProperties
. player.setNotificationProperties(myNotificationProperties)
. implement NotificationActionListener
. player.registerNotificationActionListener(myNotificationActionListener)

##### Notes
- play/pause and close are handled by the sdk, you handle previous/next and open player actions
- ExpandedNotification can only be used in SDK 16 and up
- Do to issues on certain devices, you may only see a standard notification even though you have defined a layout with transport controls.


#### AndroidManifest info
- Add the RhapsodyPlaybackService to the manifest
<service android:name="com.rhapsody.player.RhapsodyPlaybackService" />
- To receive key events (e.g. from lock screen controls, Bluetooth headsets, etc) add the com.rhapsody.player.MediaButtonReceiver to the manifest.
<receiver android:name="com.rhapsody.player.MediaButtonReceiver" >
	<intent-filter>
		<action android:name="android.intent.action.MEDIA_BUTTON" />
	</intent-filter>
</receiver>
