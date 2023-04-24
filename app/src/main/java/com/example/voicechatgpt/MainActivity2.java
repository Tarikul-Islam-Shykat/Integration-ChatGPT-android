package com.example.voicechatgpt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voicechatgpt.Adapter.MessageAdapter;
import com.example.voicechatgpt.Modal.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    List<Message> messageList;

    RecyclerView recyclerView;
    EditText inputText;
    ImageButton btnVoice, btnSend;
    MessageAdapter messageAdapter;

    private String apiUrl = "https://api.openai.com/v1/completions";
    String accessToken = "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";

    final int REQUEST_CODE_SPEECH_INPUT = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));


        init_();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        btnSend.setOnClickListener(v -> {
            processAi();
        });

        btnVoice.setOnClickListener(v -> {
            speak();
        });

    }

    private void init_() {
        recyclerView = findViewById(R.id.am_recView);
        inputText = findViewById(R.id.edt_enter);
        btnSend = findViewById(R.id.btn_send);
        btnVoice = findViewById(R.id.btn_voice);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
    }

    private void processAi() {
        String text = inputText.getText().toString();
        messageList.add(new Message(text,true)); // here true means that message is sent by the user.
        messageAdapter.notifyItemInserted(messageList.size()-1); // notify that item inserted at the last position of the list.
        recyclerView.scrollToPosition(messageList.size()-1); // recycler view will auto move to that position of the array.
        inputText.setText("");

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", text);
            requestBody.put("max_tokens", 100);
            requestBody.put("temperature", 1);
            requestBody.put("top_p", 1);
            requestBody.put("n", 1);
            requestBody.put("stream", false);
            requestBody.put("logprobs", null);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray js = response.getJSONArray("choices");
                    JSONObject jsonObject = js.getJSONObject(0);
                    String text = jsonObject.getString("text");
                    messageList.add(new Message(text.replaceFirst("\n", "").replaceFirst("\n",""),false));
                    messageAdapter.notifyItemInserted(messageList.size()-1); // notifying the addapter about added last item
                    recyclerView.scrollToPosition(messageList.size()-1); //   last position of the array, for smooth scrolling
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Occured", "Error Section");
                messageList.add(new Message(error.toString().replaceFirst("\n", "").replaceFirst("\n",""),false));
                messageAdapter.notifyItemInserted(messageList.size()-1);
                recyclerView.scrollToPosition(messageList.size()-1);
            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private  void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e ){
            Log.d("error", "error occured in speech section");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    inputText.setText(result.get(0));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
        System.exit(0);
    }
}