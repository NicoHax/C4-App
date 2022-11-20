package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CompletarregistroActivity2 extends AppCompatActivity {
    TextInputEditText oTextInputUsernameRegister;
    Button oButtonRegister;
    FirebaseAuth oAuth;
    FirebaseFirestore oFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completarregistro2);

        oTextInputUsernameRegister=findViewById(R.id.textInputEditTextUserNameCompletar);
        oButtonRegister= findViewById(R.id.ButtonRegisterCompletar);
        oAuth=FirebaseAuth.getInstance();
        oFirestore=FirebaseFirestore.getInstance();

        oButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String username=oTextInputUsernameRegister.getText().toString();
        if(!username.isEmpty()){
            updateUser(username);
        }else{
            Toast.makeText(this, "Para continuar ingrese Usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(String username) {
        String id=oAuth.getCurrentUser().getUid();
        Map<String,Object>map=new HashMap<>();
        map.put("username",username);
        oFirestore.collection("Users").document(id).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(CompletarregistroActivity2.this,MainActivity.class);
                }else{
                    Toast.makeText(CompletarregistroActivity2.this, "No se guardo en base datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


