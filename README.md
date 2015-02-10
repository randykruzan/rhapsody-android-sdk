# Rhapsody Android SDK

## Current Version
The current version of the Rhapsody Android SDK is 1.1.

## Introduction
The Rhapsody Android SDK was designed to provide a very easy way to integrate streaming music into your Android application. The SDK itself handles playback and is used in conjunction with the [Rhapsody Developer API](http://developer.rhapsody.com) to give your users access to over millions of tracks. The SDK plays full-length tracks for authenticated Rhapsody subscribers.

We have provided here the SDK ([rhapsodysdk.jar](https://github.com/Rhapsody/rhapsody-android-sdk/blob/1.1/rhapsodysdk.jar)) and two sample applications inside the SampleProject which provide examples you can use to build your own app with Rhapsody streaming music.

## Requirements
- AndroidStudio 1.0 or higher
- Android SDK 4.0 (API 14) or higher
- Android BuildTools 21.1.2 for the SampleProject (Recommend using latest available)
- android-support-v4.jar
- Picasso
- Retrofit
- gson

#### SDK Only
The Rhapsody Android SDK is available as a jar. You can download the [rhapsodysdk.jar](https://github.com/Rhapsody/rhapsody-android-sdk/blob/1.1/rhapsodysdk.jar) and add it to your Android Studio project gradle.build file:

```groovy
compile files('libs/rhapsodysdk.jar')
```

#### SampleProject

Using Import Project in Android Studio, select the SampleProject folder. After importing, run gradle sync, and you should be ready to build and deploy the sample apps to your device. 

You might be asked to install additional components such as build tools. You may either install them or change the required version in the build.gradle to one you have installed already.


#### AndroidManifest information
The Rhapsody Android SDK uses these permissions:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

##### Permissions explained
android.permission.INTERNET - We use this to access Internet to send and receive data.
android.permission.WAKE_LOCK - We use this permission to keep the device from turning off the network during playback if the app is backgrounded.
android.permission.READ_PHONE_STATE - We use this to properly handle audio during events such as phone calls.

Add the RhapsodyPlaybackService to the manifest:
```xml
<service android:name="com.rhapsody.player.RhapsodyPlaybackService" />
```
To receive key events (e.g. from lock screen controls, Bluetooth headsets, etc) add the com.rhapsody.player.MediaButtonReceiver to the manifest.
```xml
<receiver android:name="com.rhapsody.player.MediaButtonReceiver" >
	<intent-filter>
		<action android:name="android.intent.action.MEDIA_BUTTON" />
	</intent-filter>
</receiver>
```


#### Notifications
If you want to use transport controls in the notification:
- make your notification layouts (expanded, standard or both)
- implement AbstractNotificationProperties and set them:
```java
player.setNotificationProperties(myNotificationProperties)
```
- implement NotificationActionListener and set it:
```java
player.registerNotificationActionListener(myNotificationActionListener)
```

##### Notifications notes
- ExpandedNotification can only be used in SDK 16 and up
- Due to issues on certain devices, you may only see a standard notification even though you have defined a layout with transport controls.


#### AndroidManifest info
Add the RhapsodyPlaybackService to the manifest
```xml
<service android:name="com.rhapsody.player.RhapsodyPlaybackService" />
```
To receive key events (e.g. from lock screen controls, Bluetooth headsets, etc) add the com.rhapsody.player.MediaButtonReceiver to the manifest.
```xml
<receiver android:name="com.rhapsody.player.MediaButtonReceiver" >
	<intent-filter>
		<action android:name="android.intent.action.MEDIA_BUTTON" />
	</intent-filter>
</receiver>
```