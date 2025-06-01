package com.example.uniforte;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar; // Import ProgressBar
import android.widget.Toast;
import androidx.annotation.NonNull; // Import NonNull
import androidx.appcompat.app.AppCompatActivity;

import com.example.uniforte.data.network.ApiService; // Import ApiService
import com.example.uniforte.data.network.RetrofitInstance; // Import RetrofitInstance

import org.json.JSONObject; // Import JSONObject

import java.io.IOException; // Import IOException
import java.util.HashMap; // Import HashMap
import java.util.Map; // Import Map

import okhttp3.ResponseBody; // Import ResponseBody
import retrofit2.Call; // Import Call
import retrofit2.Callback; // Import Callback
import retrofit2.Response; // Import Response

public class CadastroActivity extends AppCompatActivity {

    private EditText etCpf, etEmailRegister, etSenhaRegister, etConfirmarSenha;
    private Button btnCadastrar;
    private ProgressBar progressBarCadastro; // Adicionar ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializar Views
        etCpf = findViewById(R.id.etCpf);
        etEmailRegister = findViewById(R.id.etEmailRegister);
        etSenhaRegister = findViewById(R.id.etSenhaRegister);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        // Assumindo que você adicionou uma ProgressBar com id progressBarCadastro no XML
        progressBarCadastro = findViewById(R.id.progressBarCadastro);

        // Botão voltar
        ImageButton btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(view -> {

            startActivity(new Intent(CadastroActivity.this, MainActivity.class));
        });

        // Botão Cadastrar
        btnCadastrar.setOnClickListener(view -> {
            tentarCadastrarUsuario();
        });
    }


    private void tentarCadastrarUsuario() {
        String cpf = etCpf.getText().toString().trim();
        String email = etEmailRegister.getText().toString().trim();
        String senha = etSenhaRegister.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        // --- Validações de entrada (mantidas como estavam) ---
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

        etCpf.setError(null);
        etEmailRegister.setError(null);
        etSenhaRegister.setError(null);
        etConfirmarSenha.setError(null);


        mostrarLoading(true);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("cpf", cpf);
        requestBody.put("email", email);
        requestBody.put("senha", senha);


        // Obter instância do serviço Retrofit
        ApiService apiService = RetrofitInstance.INSTANCE.getApi(); // Acessar via INSTANCE se RetrofitInstance for object Kotlin

        // Fazer a chamada assíncrona
        apiService.cadastrarUsuario(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                mostrarLoading(false);
                if (response.isSuccessful()) {
                    // Cadastro bem-sucedido (geralmente 201 Created)
                    Log.i("CadastroActivity", "Cadastro realizado com sucesso! Status: " + response.code());
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();

                    // Voltar para a tela de Login (MainActivity)
                    Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Limpar pilha de activities
                    startActivity(intent);
                    finish();

                } else {
                    // Erro retornado pela API (ex: email já existe, CPF inválido no backend, etc.)
                    String errorBodyString = "{\"error\": \"Erro ao cadastrar\"}"; // Padrão
                    try {
                        if (response.errorBody() != null) {
                            errorBodyString = response.errorBody().string();
                        }
                        JSONObject jsonObject = new JSONObject(errorBodyString);
                        String errorMessage = jsonObject.getString("error");
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
                // Erro de rede ou na configuração do Retrofit
                Log.e("CadastroActivity", "Falha na chamada de cadastro: " + t.getMessage(), t);
                if (t instanceof IOException) {
                    Toast.makeText(CadastroActivity.this, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CadastroActivity.this, "Erro inesperado ao tentar cadastrar.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Controla a visibilidade da ProgressBar e o estado do botão.
     * @param isLoading True para mostrar loading, False para esconder.
     */
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

