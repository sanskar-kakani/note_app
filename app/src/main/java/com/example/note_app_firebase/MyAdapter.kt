package com.example.note_app_firebase


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app_firebase.models.NoteDataClass
import com.google.firebase.database.FirebaseDatabase


class MyAdapter(private val list: ArrayList<NoteDataClass>, private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private val pref = context.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
    val username = pref.getString("username" , null).toString()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitle: TextView = view.findViewById(R.id.note_title)
        val noteText: TextView = view.findViewById(R.id.note_text)

        val cardView: CardView = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item, parent,false))
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        holder.noteTitle.text = list[position].title
        holder.noteText.text = list[position].text

        holder.cardView.setOnClickListener {
            val intent = Intent(context, EditNoteActivity::class.java)
            intent.putExtra("title", list[position].title)
            intent.putExtra("text", list[position].text)
            intent.putExtra("key", list[position].key)

            context.startActivity(intent)
        }

        holder.cardView.setOnLongClickListener {

            deleteNote(position)

            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private  fun deleteNote(position: Int) {
        val builder = AlertDialog.Builder(context)
            .setTitle("delete??")
            .setMessage("Do you want to delete the note?")
            .setIcon(R.drawable.baseline_delete_24)
            .setPositiveButton("yes") { _, _ ->

                FirebaseDatabase.getInstance().getReference("users")
                    .child(username).child("noteList").child(list[position].key).removeValue()

                list.removeAt(position)
                notifyItemRemoved(position)

            }
            .setNegativeButton("no") { _, _ ->}

        val alertDialog = builder.create()
        alertDialog.show()
    }


}