package com.example.uniforte;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uniforte.data.network.ApiService;
import com.example.uniforte.data.network.RetrofitInstance;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etCpf, etEmailRegister, etSenhaRegister, etConfirmarSenha;
    private Button btnCadastrar;
    private ProgressBar progressBarCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNome = findViewById(R.id.etNome);
        etCpf = findViewById(R.id.etCpf);
        etEmailRegister = findViewById(R.id.etEmailRegister);
        etSenhaRegister = findViewById(R.id.etSenhaRegister);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        progressBarCadastro = findViewById(R.id.progressBarCadastro);

        // Botão voltar (mantido)
        ImageButton btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(view -> {
            startActivity(new Intent(CadastroActivity.this, MainActivity.class));
            // finish(); // Descomente se quiser fechar esta tela ao voltar
        });

        // Botão Cadastrar (mantido)
        btnCadastrar.setOnClickListener(view -> {
            tentarCadastrarUsuario();
        });
    }

    private void tentarCadastrarUsuario() {
        // Ler todos os campos, incluindo nome
        String nome = etNome.getText().toString().trim();
        String cpf = etCpf.getText().toString().trim();
        String email = etEmailRegister.getText().toString().trim();
        String senha = etSenhaRegister.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        if (nome.isEmpty()) { // Validar nome
            etNome.setError("Informe o nome completo");
            etNome.requestFocus();
            return;
        }

        if (cpf.isEmpty()) {
            etCpf.setError("Informe o CPF");
            etCpf.requestFocus();
            return;
        }
        // Adicionar validação de formato de CPF se necessário

        if (email.isEmpty()) {
            etEmailRegister.setError("Informe o email");
            etEmailRegister.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailRegister.setError("Email inválido");
            etEmailRegister.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            etSenhaRegister.setError("Informe a senha");
            etSenhaRegister.requestFocus();
            return;
        }
        if (senha.length() < 5) {
            etSenhaRegister.setError("A senha deve ter pelo menos 5 caracteres");
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
            etConfirmarSenha.setText("");
            return;
        }

        // Limpar erros se passou nas validações
        etNome.setError(null);
        etCpf.setError(null);
        etEmailRegister.setError(null);
        etSenhaRegister.setError(null);
        etConfirmarSenha.setError(null);

        // --- Iniciar chamada de API ---
        mostrarLoading(true);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("nome", nome);
        requestBody.put("cpf", cpf);
        requestBody.put("email", email);
        requestBody.put("senha", senha);

        // Obter instância do serviço Retrofit
        ApiService apiService = RetrofitInstance.INSTANCE.getApi();

        // Fazer a chamada assíncrona
        apiService.cadastrarUsuario(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                mostrarLoading(false);
                if (response.isSuccessful()) {
                    Log.i("CadastroActivity", "Cadastro realizado com sucesso! Status: " + response.code());
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    String errorBodyString = "{\"error\": \"Erro ao cadastrar\"}";
                    try {
                        if (response.errorBody() != null) {
                            errorBodyString = response.errorBody().string();
                        }
                        JSONObject jsonObject = new JSONObject(errorBodyString);
                        String errorMessage = jsonObject.optString("error", "Erro desconhecido no cadastro."); // Usar optString com fallback
                        Log.e("CadastroActivity", "Erro no cadastro (API): " + response.code() + " - " + errorMessage);
                        Toast.makeText(CadastroActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("CadastroActivity", "Erro ao parsear JSON de erro ou ler errorBody: " + e.getMessage());
                        Toast.makeText(CadastroActivity.this, "Erro ao processar resposta do servidor.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mostrarLoading(false);
                Log.e("CadastroActivity", "Falha na chamada de cadastro: " + t.getMessage(), t);
                if (t instanceof IOException) {
                    Toast.makeText(CadastroActivity.this, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CadastroActivity.this, "Erro inesperado ao tentar cadastrar.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void mostrarLoading(boolean isLoading) {
        if (isLoading) {
            progressBarCadastro.setVisibility(View.VISIBLE);
            btnCadastrar.setEnabled(false);
        } else {
            progressBarCadastro.setVisibility(View.GONE);
            btnCadastrar.setEnabled(true);
        }
    }
}

