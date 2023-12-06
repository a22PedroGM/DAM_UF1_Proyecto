package com.example.pmdm_dam_proyecto

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment

class ScoresFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val puntuacionMasAlta = arguments?.getInt("puntuacionMasAlta", 0) ?: 0
        añadirFila(puntuacionMasAlta)
        return inflater.inflate(R.layout.fragment_scores, container, false)
    }

    private fun añadirFila(puntuacion: Int) {
        val tabla = view?.findViewById<TableLayout>(R.id.tableLayout)
        val fila = TableRow(requireContext())
        val nombre = EditText(requireContext())
        val puntuacionTextView = TextView(requireContext())

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Nombre del ganador")
        builder.setView(nombre)
        builder.setPositiveButton("OK") { _, _ ->
            puntuacionTextView.text = puntuacion.toString()
            fila.addView(nombre)
            fila.addView(puntuacionTextView)
            tabla?.addView(fila)
        }
        builder.show()
    }

}
