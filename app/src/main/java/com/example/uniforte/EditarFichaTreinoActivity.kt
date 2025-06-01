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
import com.google.android.material.button.MaterialButton

class EditarFichaTreinoActivity : AppCompatActivity() {

    private lateinit var btnVoltar: ImageView
    private lateinit var btnNomeAluno: TextView
    private lateinit var btnSalvar: MaterialButton
    private lateinit var navInferiorProfessorFragment: NavInferiorProfessorFragment
    private lateinit var etExercicios: TextInputEditText
    private lateinit var flexboxLayout: FlexboxLayout
    private val listaExercicios = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_ficha_treino)

        inicializarViews()
        configurarListeners()

        // Inserir o fragmento da navegação inferior no container, e aguardar finalização imediata
        val navInferiorProfessorFragment = NavInferiorProfessorFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorProfessorFragment)
            .commit() // <- Aqui está o segredo!

        // Configurar o callback após o fragmento estar ativo
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
    }

    private fun inicializarViews() {
        btnVoltar = findViewById(R.id.btnVoltar)
        btnSalvar = findViewById(R.id.btnSalvar)
        etExercicios = findViewById(R.id.etExercicios)
        flexboxLayout = findViewById(R.id.flexboxChips)
        btnNomeAluno = findViewById(R.id.btnNomeAluno)

    }

    private fun configurarListeners() {
        btnVoltar.setOnClickListener { finish() }

        btnNomeAluno.setOnClickListener {
            startActivity(Intent(this, PerfilAlunoActivity::class.java))
        }

        btnSalvar.setOnClickListener {
            salvar()
        }

        etExercicios.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.endsWith(",")) {
                        val texto = it.trim().trimEnd(',').trim()
                        if (texto.isNotEmpty() && !listaExercicios.contains(texto.toString())) {
                            adicionarChip(texto.toString())
                            etExercicios.text?.clear()
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun adicionarChip(texto: String) {
        val chip = Chip(this).apply {
            this.text = texto
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

    private fun salvar() {
        finish()
        Toast.makeText(this, "Ficha editada com sucesso!", Toast.LENGTH_SHORT).show()
    }

    // Se quiser carregar uma lista inicial de exercícios e criar chips:
    private fun carregarListaExercicios(exercicios: List<String>) {
        exercicios.forEach {
            if (!listaExercicios.contains(it)) {
                adicionarChip(it)
            }
        }
    }
}
