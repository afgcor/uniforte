package com.example.uniforte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class NavInferiorAlunoFragment : Fragment() {
    var onNavItemSelected: ((itemId: Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nav_inferior_aluno, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHome = view.findViewById<TextView>(R.id.navHome)
        val navFicha = view.findViewById<TextView>(R.id.navFicha)
        val navPerfil = view.findViewById<TextView>(R.id.navPerfil)

        navHome.setOnClickListener {
            onNavItemSelected?.invoke(R.id.navHome)
        }
        navFicha.setOnClickListener {
            onNavItemSelected?.invoke(R.id.navFicha)
        }
        navPerfil.setOnClickListener {
            onNavItemSelected?.invoke(R.id.navPerfil)
        }
    }
}