package com.example.uniforte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    private EditText etCpf, etEmailRegister, etSenhaRegister, etConfirmarSenha;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etCpf = findViewById(R.id.etCpf);
        etEmailRegister = findViewById(R.id.etEmailRegister);
        etSenhaRegister = findViewById(R.id.etSenhaRegister);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // Configuração do botão de voltar
        ImageButton btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                finish();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
            }
        });
    }

    /**
     * Método que realiza a validação dos dados e efetua o cadastro do usuário.
     */
    private void cadastrarUsuario() {

        String cpf = etCpf.getText().toString().trim();
        String email = etEmailRegister.getText().toString().trim();
        String senha = etSenhaRegister.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        if (cpf.isEmpty()) {
            etCpf.setError("Informe o CPF");
            etCpf.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmailRegister.setError("Informe o email");
            etEmailRegister.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            etSenhaRegister.setError("Informe a senha");
            etSenhaRegister.requestFocus();
            return;
        }

        if (confirmarSenha.isEmpty()) {
            etConfirmarSenha.setError("Confirme a senha");
            etConfirmarSenha.requestFocus();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            etConfirmarSenha.setError("As senhas não coincidem");
            etConfirmarSenha.requestFocus();
            return;
        }

        Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
    }
}
