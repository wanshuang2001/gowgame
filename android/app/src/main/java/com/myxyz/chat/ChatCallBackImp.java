package com.myxyz.chat;

import android.content.Intent;
import com.epicgames.unreal.IChatCallBack;

import android.util.Log;
import android.content.Context;

//import cn.wildfire.chat.app.main.MainActivity;
import com.robertodoering.harpy.MainActivity;
public class ChatCallBackImp implements IChatCallBack
{
	private static final String TAG = "ChatCallBackImp";

	@Override
	public void StartMainActivity(Context packageContext,String Message)
	{
		    Log.e(TAG, "==>ChatCallBackImp,StartMainActivity="+Message );
        Intent intent = new Intent();
        intent.setClass(packageContext, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG设置
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP );
        packageContext.startActivity(intent);

	}
}
