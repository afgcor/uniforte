package com.example.uniforte

import android.content.Intent
import java.util.Locale
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import java.util.Calendar
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.example.uniforte.data.model.AulaProfessor
import com.example.uniforte.data.network.SupabaseRetrofitClient
import com.example.uniforte.data.network.AulaService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class AdicionarAulaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_aula)

        // Inserir o fragmento da navegação inferior no container
        val navInferiorProfessorFragment = NavInferiorProfessorFragment   ()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorProfessorFragment)
            .commit()

        // Configurar o callback para tratar cliques na navegação inferior
        navInferiorProfessorFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, HomeProfessorActivity::class.java))
                }
                R.id.navMeusAlunos -> {
                    startActivity(Intent(this, MeusAlunosActivity::class.java))
                }
                R.id.navPerfilAdmin -> {
                    startActivity(Intent(this, PerfilAdminActivity::class.java))
                }
            }
        }

        val inputDataAula = findViewById<TextInputEditText>(R.id.inputDataAula)
        inputDataAula.setOnClickListener {
            val c = Calendar.getInstance()
            val ano = c.get(Calendar.YEAR)
            val mes = c.get(Calendar.MONTH)
            val dia = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                val dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y)
                inputDataAula.setText(dataFormatada)
            }, ano, mes, dia)

            datePickerDialog.show()
        }

        val inputHorarioAula = findViewById<TextInputEditText>(R.id.inputHorarioAula)
        inputHorarioAula.setOnClickListener {
            val c = Calendar.getInstance()
            val hora = c.get(Calendar.HOUR_OF_DAY)
            val minuto = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, h, m ->
                val horarioFormatado = String.format(Locale.getDefault(), "%02d:%02d", h, m)
                inputHorarioAula.setText(horarioFormatado)
            }, hora, minuto, true)

            timePickerDialog.show()
        }

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val inputTitulo = findViewById<TextInputEditText>(R.id.inputTituloAula)
        val inputDescricao = findViewById<TextInputEditText>(R.id.inputDescricaoAula)


        val btnSalvar = findViewById<Button>(R.id.buttonSalvar)
        btnSalvar.setOnClickListener{
            val dataOriginal = inputDataAula.text.toString()
            val dataConvertida = try {
                val formatoEntrada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formatoSaida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val data = formatoEntrada.parse(dataOriginal)
                formatoSaida.format(data!!)
            } catch (e: Exception) {
                ""
            }

            val horario = inputHorarioAula.text.toString()

            val aula = AulaProfessor(
                titulo = inputTitulo.text.toString(),
                descricao = inputDescricao.text.toString(),
                data = dataConvertida,
                horario = horario
            )

            val service = SupabaseRetrofitClient.instance

            val call = service.adicionarAula(
                aula,
                apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZpdWtkYmR0bWtza3F5ZWhjcmtqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDczNTQ2NzIsImV4cCI6MjA2MjkzMDY3Mn0.XhgYs0igRP6utkLjhehxNVpJOovVMa79L3cCS1FqUCE",
                auth = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZpdWtkYmR0bWtza3F5ZWhjcmtqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDczNTQ2NzIsImV4cCI6MjA2MjkzMDY3Mn0.XhgYs0igRP6utkLjhehxNVpJOovVMa79L3cCS1FqUCE"
            )

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdicionarAulaActivity, "Aula adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                        // Limpar os campos após o sucesso
                        inputTitulo.text?.clear()
                        inputDescricao.text?.clear()
                        inputDataAula.text?.clear()
                        inputHorarioAula.text?.clear()
                    } else {
                        Toast.makeText(this@AdicionarAulaActivity, "Erro ao salvar: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AdicionarAulaActivity, "Falha: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        }

        val btnCancelar = findViewById<Button>(R.id.buttonCancelar)
        btnCancelar.setOnClickListener{
            finish()
        }
    }
}