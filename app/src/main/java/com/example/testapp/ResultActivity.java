package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.buttonReturnResult);

        button.setOnClickListener(v -> {
            String resultData = editText.getText().toString();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result_key", resultData);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}