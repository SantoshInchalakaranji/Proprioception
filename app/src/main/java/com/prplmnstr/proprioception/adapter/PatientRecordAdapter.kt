package com.prplmnstr.proprioception.adapter

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.prplmnstr.proprioception.R
import com.prplmnstr.proprioception.databinding.PatientRvItemBinding
import com.prplmnstr.proprioception.databinding.RecordRvItemBinding
import com.prplmnstr.proprioception.model.Patient
import com.prplmnstr.proprioception.model.Record
import kotlin.math.abs

class PatientRecordAdapter (
    private val deleteClickListener: (Record) -> Unit
):RecyclerView.Adapter<PatientRecordAdapter.ViewHolder>()
{

    private val records = ArrayList<Record>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:RecordRvItemBinding  =
            DataBindingUtil.inflate(layoutInflater, R.layout.record_rv_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.bind(records[position],deleteClickListener)
    }

    fun setList(items: List<Record>) {
        records.clear()
        records.addAll(items)
        notifyDataSetChanged()

    }
    inner class ViewHolder(val binding: RecordRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(record: Record, deleteClickListener: (Record) -> Unit) {
            binding.dateTv.text = record.date
            binding.initialReadingText.text = record.initialReading.toString()+" °"
            binding.finalReadingText.text = record.finalReading.toString()+" °"
            val difference = record.initialReading - record.finalReading
           binding.diffeneceValue.text = abs(difference).toString()+" °"
            binding.remarkDescTv.text = record.remark
            binding.jointTypeText.text = record.jointType


            //binding.expandingConstraintLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
           binding.expandImage.setOnClickListener {

               val hiddenItemsVisibility = if(binding.hiddenLayout.visibility == View.GONE){
                    binding.expandImage.setImageResource(R.drawable.expand_less)
                   View.VISIBLE

               } else{
                   binding.expandImage.setImageResource(R.drawable.expand_more)
                   View.GONE
               }
               binding.hiddenLayout.visibility = hiddenItemsVisibility
           }

            binding.deleteButton.setOnClickListener {
                deleteClickListener(record)
                notifyItemRemoved(records.indexOf(record))
            }
        }


    }
}