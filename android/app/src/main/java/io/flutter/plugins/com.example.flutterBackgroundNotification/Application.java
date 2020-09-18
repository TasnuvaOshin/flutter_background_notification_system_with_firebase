package com.example.flutterBackgroundNotification;

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