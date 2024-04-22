package com.example.proekt9;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.fileText);
                String fileText = editText.getText().toString();
                EditText editName = findViewById(R.id.fileName);
                String fileName = editName.getText().toString();

                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(storageDir, fileName + ".txt");

                try {
                    if (!file.exists()){
                        boolean created = file.createNewFile();
                        if (created){
                            FileWriter writer = new FileWriter(file);
                            writer.append(fileText);
                            writer.flush();
                            writer.close();
                        }
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        Button buttonRead = findViewById(R.id.buttonRead);
        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName = findViewById(R.id.fileName);
                String fileName = editName.getText().toString();

                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(storageDir, fileName + ".txt");
                // чтение файла
                if (file.exists()){
                    StringBuilder text = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;

                        while ((line = br.readLine()) != null){
                            text.append(line);
                            text.append('\n');
                        }
                        TextView textView = findViewById(R.id.textFile);
                        textView.setText(text.toString());
                        br.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    String fileContent = text.toString();
                }
            }
        });

        Button buttonSafe = findViewById(R.id.buttonSafe);
        buttonSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName = findViewById(R.id.fileName);
                String fileName = editName.getText().toString();

                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(storageDir, fileName + ".txt");

                EditText editText = findViewById(R.id.fileText);
                String fileText = editText.getText().toString();
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    outputStream.write(fileText.getBytes());
                    outputStream.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "Fale is safed", Toast.LENGTH_LONG).show();
            }
        });

        Button buttonDel = findViewById(R.id.buttonDel);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName = findViewById(R.id.fileName);
                String fileName = editName.getText().toString();

                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(storageDir, fileName + ".txt");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Подтверждение");
                builder.setMessage("Вы уверены, что хотите удалить файл " + fileName);
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // удаление файла
                        if (file.exists()){
                            boolean deleted = file.delete();
                            if (deleted){
                                Toast.makeText(MainActivity.this, "File deleted", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "File not deleted", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("KEY_STATE", "some state");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String state = savedInstanceState.getString("KEY_STATE");
    }
}