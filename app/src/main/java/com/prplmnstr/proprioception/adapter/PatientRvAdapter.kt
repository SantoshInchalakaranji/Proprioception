package com.prplmnstr.proprioception.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.prplmnstr.proprioception.R
import com.prplmnstr.proprioception.databinding.PatientRvItemBinding
import com.prplmnstr.proprioception.model.Patient

class PatientRvAdapter(
    private val selectListener: (Patient) -> Unit,
    private val infoClickListener: (Patient) -> Unit
) : RecyclerView.Adapter<PatientRvAdapter.ViewHolder>() {

    private val patients = ArrayList<Patient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: PatientRvItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.patient_rv_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return patients.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(patients[position], selectListener, infoClickListener)
    }

    fun setList(items: List<Patient>) {
        patients.clear()
        patients.addAll(items)
        notifyDataSetChanged()

    }

    inner class ViewHolder(val binding: PatientRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            patient: Patient,
            selectListener: (Patient) -> Unit,
            infoClickListener: (Patient) -> Unit
        ) {
            binding.nameTv.text = patient.name
            binding.ageTv.text = patient.age
            binding.dateTv.text = patient.lastVisit

            binding.editImage.setOnClickListener {
                infoClickListener(patient)
            }
            binding.cardView.setOnClickListener {
                selectListener(patient)
            }
        }


    }


}