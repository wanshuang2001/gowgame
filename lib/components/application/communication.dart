import 'package:flutter/services.dart';

// 创建 BasicMessageChannel
// flutter_and_native_100 为通信标识
// StandardMessageCodec() 为参数传递的 编码方式
const messageChannel = const BasicMessageChannel(
    'com.wanshuang2001.harpy/messages',
    StandardMessageCodec()
);

// 发送消息
Future<Map<dynamic, dynamic>> sendMessage(Map<dynamic, dynamic> arguments) async {
  Map<dynamic, dynamic>? reply = await messageChannel.send(arguments) as Map<dynamic, dynamic>?;
  return reply ?? {};
}
