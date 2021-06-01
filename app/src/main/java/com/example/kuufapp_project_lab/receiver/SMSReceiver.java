package com.example.kuufapp_project_lab.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null){
            Log.d("Info", "MyBroadCast");
        }
        else if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVER")){
            Bundle bundle = intent.getExtras();

            if (bundle != null){
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] smsMessages = new SmsMessage[pdus.length];

                for (int i =0;i<pdus.length;i++){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String format = bundle.getString("format");
                        smsMessages[i] = SmsMessage.createFromPdu((byte[])pdus[i], format);
                    }
                    else{
                        smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                }
//                MessageActivity.messageArrayList.add(new Message(smsMessages[0].getMessageBody().toString(), false));
//                MessageActivity.messageAdapter.notifyDataSetChanged();
            }
        }
    }
}