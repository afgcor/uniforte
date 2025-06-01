package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText

class AdicionarFichaTreinoActivity : AppCompatActivity() {

    private lateinit var flexboxLayout: FlexboxLayout
    private val listaExercicios = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_ficha_treino)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

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

        val btnNomeAluno = findViewById<TextView>(R.id.btnNomeAluno)
        btnNomeAluno.setOnClickListener{
            startActivity(Intent(this, PerfilAlunoActivity::class.java))
        }

        val btnSalvarFicha = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSalvarFicha)
        btnSalvarFicha.setOnClickListener {
            finish()
            Toast.makeText(this, "Ficha adicionada com sucesso!", Toast.LENGTH_SHORT).show()
        }

        val etExercicios = findViewById<TextInputEditText>(R.id.etExercicios)
        flexboxLayout = findViewById(R.id.flexboxChips)

        // Função para criar e adicionar chip no FlexboxLayout
        fun adicionarChip(texto: String) {
            val chip = Chip(this).apply {
                text = texto
                isCloseIconVisible = true
                setChipBackgroundColorResource(R.color.unifor_anil)
                setTextColor(ContextCompat.getColor(context, R.color.unifor_gelo))
                closeIconTint = ContextCompat.getColorStateList(context, R.color.unifor_gelo)
                setOnCloseIconClickListener {
                    flexboxLayout.removeView(this)
                    listaExercicios.remove(texto)
                }
                setOnClickListener {
                    etExercicios.setText(text)
                    etExercicios.setSelection(text.length)
                    flexboxLayout.removeView(this)
                    listaExercicios.remove(text)
                }
                layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 16
                    bottomMargin = 2
                }
            }
            flexboxLayout.addView(chip)
            listaExercicios.add(texto)
        }

        etExercicios.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.endsWith(",")) {
                        val texto = it.trim().trimEnd(',').trim().toString()
                        if (texto.isNotEmpty() && !listaExercicios.contains(texto)) {
                            adicionarChip(texto)
                            etExercicios.text?.clear()
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
