package com.robertodoering.harpy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.StandardMessageCodec;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;
import com.myxyz.chat.ChatCallBackImp;
import com.epicgames.unreal.ChatBridge;

public class MainActivity extends FlutterActivity {
  public static final String TAG = "MainActivity";
  private static final String CHANNEL_NAME = "com.wanshuang2001.harpy/messages";
  private BasicMessageChannel<Object> messageChannel;

  static public ChatCallBackImp chatcallbackimp;

  @Override
  public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {

    if(chatcallbackimp==null)
    {
      chatcallbackimp = new ChatCallBackImp();
      ChatBridge.getInstance().SetChatCallBack(chatcallbackimp);
    }

    GeneratedPluginRegistrant.registerWith(flutterEngine);

    // Initialize the message channel and handle method calls from Flutter
    setupMessageChannel(flutterEngine);

    handleMethodCalls(flutterEngine);

    final View view = findViewById(android.R.id.content);

    if (view != null) {
      view.setSystemUiVisibility(
        // Tells the system that the window wishes the content to
        // be laid out at the most extreme scenario
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
          // Tells the system that the window wishes the content to
          // be laid out as if the navigation bar was hidden
          View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
      );
    }
  }

  private void handleMethodCalls(@NonNull FlutterEngine flutterEngine) {
    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "com.robertodoering.harpy")
      .setMethodCallHandler(
        (call, result) -> {
          if (call.method.equals("showOpenByDefault")) {
            showOpenByDefault();
            result.success(true);
          } else if (call.method.equals("hasUnapprovedDomains")) {
            result.success(hasUnapprovedDomains());
          } else {
            result.notImplemented();
          }
        }
      );
  }

  private void showOpenByDefault() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
      final Context context = getContext();
      Intent intent;

      if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
        // samsung crashes when trying to open the 'open by default' settings page :^)
        // so we open samsung's 'apps that can open links' settings page instead
        // https://stackoverflow.com/questions/70953672/android-12-deep-link-association-by-user-fails-because-of-crash-in-samsung-setti
        intent = new Intent("android.settings.MANAGE_DOMAIN_URLS");
      }
      else {
        intent = new Intent(
          Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
          Uri.parse("package:" + context.getPackageName())
        );
      }

      context.startActivity(intent);
    }
  }

  private boolean hasUnapprovedDomains() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
      final Context context = getContext();
      final DomainVerificationManager manager = context.getSystemService(DomainVerificationManager.class);

      DomainVerificationUserState userState;

      try {
        userState = manager.getDomainVerificationUserState(context.getPackageName());
      } catch (PackageManager.NameNotFoundException e) {
        return false;
      }

      return userState.getHostToStateMap()
        .values()
        .stream()
        .anyMatch((stateValue) ->
          stateValue != DomainVerificationUserState.DOMAIN_STATE_VERIFIED &&
          stateValue != DomainVerificationUserState.DOMAIN_STATE_SELECTED
        );
    }

    return false;
  }

  //------------------------------------------------------------------------------------------------
  private void setupMessageChannel(FlutterEngine flutterEngine) {
    // Initialize the message channel
    messageChannel = new BasicMessageChannel<Object>(
      flutterEngine.getDartExecutor().getBinaryMessenger(),
      CHANNEL_NAME,
      StandardMessageCodec.INSTANCE
    );

    // Handle method calls from Flutter
    messageChannel.setMessageHandler(new BasicMessageChannel.MessageHandler<Object>() {

      public void onMessage(Object message, BasicMessageChannel.Reply<Object> reply) {
        handleFlutterMessage(message,reply);
      }
    });
  }

  private void handleFlutterMessage(Object message, BasicMessageChannel.Reply<Object> reply) {
    Map<Object, Object> arguments = (Map<Object, Object>)message;
    //方法名标识
    String lMethod = (String) arguments.get("method");
    String param = (String) arguments.get("param");
    Log.i(TAG, "message:"+lMethod);
    Log.i(TAG, "param:"+param);
    //测试 reply.reply()方法 发消息给Flutter
    if(lMethod.equals("open_ue_game"))
    {
        String msg = "";
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            UEManager.getInstance().OpenUE(MainActivity.this, param);
          }
        });
    }
    if (lMethod.equals("test")) {
      //Toast.makeText(mContext, "flutter 调用到了 android test", Toast.LENGTH_SHORT).show();
      //回调Flutter
      Map<String, Object> resultMap = new HashMap<>();
      resultMap.put("message", "reply.reply 返回给flutter的数据");
      resultMap.put("code", 200);
      //回调 此方法只能使用一次 向Flutter中反向回调消息
      reply.reply(resultMap);
    }
    /*
    if (message.containsKey("method")) {
      String method = (String) message.get("method");
      if ("showOpenByDefault".equals(method)) {
        //showOpenByDefault();
        reply.reply(true);
      } else if ("hasUnapprovedDomains".equals(method)) {
        reply.reply(hasUnapprovedDomains());
      } else {
        reply.reply(null);
      }
    } else {
      reply.reply(null);
    }*/
  }
  // Function to send a message to Flutter
  private void sendMessageToFlutter(HashMap<String, Object> message) {
    messageChannel.send(message);
  }
}
