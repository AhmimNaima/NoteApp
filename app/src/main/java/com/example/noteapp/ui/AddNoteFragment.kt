package com.example.noteapp.ui


import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation

import com.example.noteapp.R
import com.example.noteapp.db.Note
import com.example.noteapp.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNoteFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note= AddNoteFragmentArgs.fromBundle(it).note
            edit_text_title.setText(note?.title)
            edit_text_note.setText(note?.note)
        }

        button_save.setOnClickListener{view ->
            val noteTitle = edit_text_title.text.toString().trim()
            val noteBody = edit_text_note.text.toString().trim()

            val c = Calendar.getInstance()
            val format= SimpleDateFormat("dd-MM-yyyy")
            val noteDate= format.format(c.time).toString().trim()

            println("Current Date is: $noteDate")


            if (noteTitle.isEmpty()) {
                edit_text_title.error = "title required"
                edit_text_title.requestFocus()
                return@setOnClickListener
            }

            if (noteBody.isEmpty()) {
                edit_text_note.error = "note required"
                edit_text_note.requestFocus()
                return@setOnClickListener
            }

            if (noteDate.isEmpty()) {
                return@setOnClickListener
            }

            launch {

                context?.let {
                    val mNote = Note(noteTitle, noteBody,noteDate)

                    if (note == null) {
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        it.toast("Note Saved")
                    } else {
                        mNote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        it.toast("Note Updated")
                    }

                    val action= AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)
                }
            }


            //val note = Note(noteTitle,noteBody)
            //saveNote(note)

        }
    }

    /*private fun saveNote(note:Note){
        class SaveNote : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                NoteDatabase(activity!!).getNoteDao().addNote(note)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)

                Toast.makeText(activity,"Note Saved",Toast.LENGTH_LONG).show()
            }

        }

        SaveNote().execute()
    }*/

    private fun deleteNote(){
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes") { _, _ ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view!!).navigate(action)
                }
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> if (note != null) deleteNote() else context?.toast("Cannot Delete")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

}
