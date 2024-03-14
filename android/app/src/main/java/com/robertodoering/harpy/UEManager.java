package com.robertodoering.harpy;

import android.content.Context;
import android.content.Intent;

public class UEManager
{
    private static UEManager instance = new UEManager();
    private static boolean bHaveStartUESplash = false;
    public static UEManager getInstance()
    {
        return instance;
    }

    private UEManager()
    {
    }
    // 方法
    public void sayHi()
    {
        System.out.println("Hi,Java.");
    }

    public void OpenUE(Context context,String msg)
    {
        Intent intent = new Intent();
        String MessageType = "com.leduo.chat.UEMamager.message";
        intent.putExtra(MessageType, msg);
        if(bHaveStartUESplash==false) //不然每次都要进去显示splash
        {
            intent.setClass(context, com.epicgames.unreal.SplashActivity.class);
            bHaveStartUESplash=true;
        }
        else
        {
            intent.setClass(context, com.epicgames.unreal.GameActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        context.startActivity(intent);
    }
}
