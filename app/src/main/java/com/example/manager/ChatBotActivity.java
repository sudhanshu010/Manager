package com.example.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.applozic.mobicommons.commons.core.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.kommunicate.KmConversationBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;

public class ChatBotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        Kommunicate.init(getApplicationContext(), "2ba99739078677c45246d951d8810f0f2");

        List<String> botList = new ArrayList();
        botList.add("aai-bot-iimcn"); //enter your integrated bot Ids

        new KmConversationBuilder(this)
                .launchConversation(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        Utils.printLog(getApplicationContext(), "ChatTest", "Success : " + message);
                    }



                    @Override
                    public void onFailure(Object error) {
                        Utils.printLog(getApplicationContext(), "ChatTest", "Failure : " + error);
                    }
                });




    }
}
