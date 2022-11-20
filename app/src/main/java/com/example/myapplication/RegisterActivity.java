package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView oCircleImageViewBack;
    TextInputEditText oTextInputEditTextUserName;
    TextInputEditText oTextInputEditTextEmailRegister;
    TextInputEditText oTextInputEditTextPasswordRegister;
    TextInputEditText oTextInputEditTextConfirmPassword;
    Button oButtonRegister;
    FirebaseAuth mAut;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        oTextInputEditTextUserName=findViewById(R.id.textInputEditTextUserName);
        oTextInputEditTextEmailRegister=findViewById(R.id.textInputEditTextEmailRegister);
        oTextInputEditTextPasswordRegister=findViewById(R.id.textInputEditTextPasswordRegister);
        oTextInputEditTextConfirmPassword=findViewById(R.id.textInputEditTextConfirmPassword);

        mAut=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();

        oButtonRegister=findViewById(R.id.ButtonRegister);
        oButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        oCircleImageViewBack= findViewById(R.id.circleimageback);
        oCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register() {
        String username=oTextInputEditTextUserName.getText().toString();
        String email=oTextInputEditTextEmailRegister.getText().toString();
        String password=oTextInputEditTextPasswordRegister.getText().toString();
        String confirmpassword=oTextInputEditTextConfirmPassword.getText().toString();
        if (!username.isEmpty() && !email.isEmpty() &&!password.isEmpty() &&!confirmpassword.isEmpty()){
            if (isEmailValid(email)){
                if (password.equals(confirmpassword)){
                    if (password.length()>=6){
                        CreateUser(username,email,password);
                    }else{
                        Toast.makeText(this, "La contrasena debe tener minimo 6 cracters", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "El correo no es válido", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Has insertado todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void CreateUser(final String username, final String email, String password) {
        mAut.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id=mAut.getCurrentUser().getUid();
                    Map<String, String> map=new HashMap<>();
                    map.put("email",email);
                    map.put("username",username);
                    map.put("password",password);
                    mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Se almaceno correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Su registro no se realizo", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                    Toast.makeText(RegisterActivity.this, "Su registro fue exitoso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "El registro no se pudo completar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}