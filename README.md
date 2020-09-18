# flutterBackgroundNotification

## Getting Started
I hope you already know how to setup firebase in your flutter project if you dont know you can follow the link https://firebase.google.com/docs/flutter/setup and then Follow the bellow setps for  background notification . 
Follow The Steps

1) Add this to your pubspec.yaml
```
dependencies:
  flutter:
    sdk: flutter


  # The following adds the Cupertino Icons font to your application.
  # Use with the CupertinoIcons class for iOS style icons.
  cupertino_icons: ^0.1.3
  firebase_messaging: ^7.0.0
```

2) Go to android-> app ->main -> java 
```
Create a Folder with your package name 
then create a file name Application.java 

Example : android-> app ->main -> java -> yourpackagename -> Application.java

```
3) Go to Application.java file and add these codes 

```
package yourpackagename;

import io.flutter.app.FlutterApplication;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugins.firebasemessaging.FirebaseMessagingPlugin;
import io.flutter.plugin.common.PluginRegistry.PluginRegistrantCallback;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugins.firebasemessaging.FlutterFirebaseMessagingService;

public class Application extends FlutterApplication implements PluginRegistrantCallback {
  
  public static final String CHANNEL_HIGH = "default";
  @Override
  public void onCreate() {
    super.onCreate();
    FlutterFirebaseMessagingService.setPluginRegistrant(this);
  }

  @Override
  public void registerWith(PluginRegistry registry) {
   FirebaseMessagingPlugin.registerWith(registry.registrarFor("io.flutter.plugins.firebasemessaging.FirebaseMessagingPlugin"));
  }

  private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelHigh = new NotificationChannel(
                    CHANNEL_HIGH,
                    "Notificación Importante",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channelHigh.setDescription("Canal Alto");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelHigh);

        }
    }
}

```

4) Go to android-> app ->manifest and add these lines 
```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.flutterBackgroundNotification">
    <!-- io.flutter.app.FlutterApplication is an android.app.Application that
         calls FlutterMain.startInitialization(this); in its onCreate method.
         In most cases you can leave this as-is, but you if you want to provide
         additional functionality it is fine to subclass or reimplement
         FlutterApplication and put your custom class here. -->
    <application
        android:name=".Application"
        android:label="flutterBackgroundNotification"
        android:icon="@mipmap/ic_launcher">
                <meta-data
    android:name="com.google.firebase.messaging.default_notification_channel_id"
    android:value="default"/>
        <activity
            android:name=".MainActivity"
            android:launchMode="singleTop"
            android:theme="@style/LaunchTheme"
            android:configChanges="orientation|keyboardHidden|keyboard|screenSize|smallestScreenSize|locale|layoutDirection|fontScale|screenLayout|density|uiMode"
            android:hardwareAccelerated="true"
            android:windowSoftInputMode="adjustResize">
            <!-- Specifies an Android theme to apply to this Activity as soon as
                 the Android process has started. This theme is visible to the user
                 while the Flutter UI initializes. After that, this theme continues
                 to determine the Window background behind the Flutter UI. -->
        
  <meta-data
    android:name="com.google.firebase.messaging.default_notification_channel_id"
    android:value="default"/>
            <meta-data
              android:name="io.flutter.embedding.android.NormalTheme"
              android:resource="@style/NormalTheme"
              />
            <!-- Displays an Android View that continues showing the launch screen
                 Drawable until Flutter paints its first frame, then this splash
                 screen fades out. A splash screen is useful to avoid any visual
                 gap between the end of Android's launch screen and the painting of
                 Flutter's first frame. -->
            <meta-data
              android:name="io.flutter.embedding.android.SplashScreenDrawable"
              android:resource="@drawable/launch_background"
              />
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
             <intent-filter>
      <action android:name="FLUTTER_NOTIFICATION_CLICK" />
      <category android:name="android.intent.category.DEFAULT" />
  </intent-filter>
        </activity>
        <!-- Don't delete the meta-data below.
             This is used by the Flutter tool to generate GeneratedPluginRegistrant.java -->
        <meta-data
            android:name="flutterEmbedding"
            android:value="2" />
    </application>
</manifest>


```



5) Finally go to lib->main.dart and add your main code thats it now u will get background notification 
```
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
              visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final FirebaseMessaging _firebaseMessaging = FirebaseMessaging();

  int _counter = 0;

  void _incrementCounter() {
    setState(() {
          _counter++;
    });
  }

  @override
  void initState() {
    super.initState();
    _firebaseMessaging.configure(
      onMessage: (Map<String, dynamic> message) async {
        print("onMessage: $message");
      },
      onBackgroundMessage: _myBackgroundMessageHandler,
      onLaunch: (Map<String, dynamic> message) async {
        print("onLaunch: $message");
      },
      onResume: (Map<String, dynamic> message) async {
        print("onResume: $message");
      },
    );
    _firebaseMessaging.requestNotificationPermissions(
        const IosNotificationSettings(
            sound: true, badge: true, alert: true, provisional: true));
    _firebaseMessaging.onIosSettingsRegistered
        .listen((IosNotificationSettings settings) {
      print("Settings registered: $settings");
    });
    _firebaseMessaging.getToken().then((String token) {
      assert(token != null);
      setState(() {
        print("Push Messaging token: $token");
      });
      print("text");
    });
  }

  static Future<dynamic> _myBackgroundMessageHandler(
      Map<String, dynamic> message) async {
    if (message.containsKey('data')) {
      // Handle data message
      var data = message['data'] ?? message;
      String orderId = data['orderId'];
      String itemId = data['itemId'];
      print("fcmtest :" + orderId);
      print("fcmtest :" + itemId);
    }
  

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
```

## Tips

In your Application.java if  this part not work you can escape it to run the app .
```
 private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelHigh = new NotificationChannel(
                    CHANNEL_HIGH,
                    "Notificación Importante",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channelHigh.setDescription("Canal Alto");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelHigh);

        }
    }
```

Check the Manifest File and do not forget to add 
```
  <meta-data
    android:name="com.google.firebase.messaging.default_notification_channel_id"
    android:value="default"/>
```


Do Not Forget to Add Notification Chanell when u send the notification 

https://github.com/TasnuvaOshin/flutter_background_notification_system_with_firebase/blob/master/Screenshot%202020-09-18%20at%202.30.07%20PM.png

Check out the Sending Format 


## Now Run the app -> then make it background then  send notification from firebase  see in the debug console you are getting the data in the background 

- For More Check the Repository 
- Connect Me - tasnuva.oshin12@gmail.com
- thanks

