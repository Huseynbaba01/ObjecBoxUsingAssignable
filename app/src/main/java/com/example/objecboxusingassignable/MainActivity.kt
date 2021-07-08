package com.example.objecboxusingassignable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.objecboxusingassignable.data.ObjectBox.boxStore
import com.example.objecboxusingassignable.data.ObjectBox.init
import com.example.objecboxusingassignable.data.School
import com.example.objecboxusingassignable.data.Student
import io.objectbox.Box
import io.objectbox.query.Query
import io.objectbox.reactive.DataSubscription
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {
    private lateinit var buttonAdd: Button
    private lateinit var buttonUpdate: Button
    private lateinit var buttonUpdateStudent: Button
    private lateinit var editText: EditText
    private lateinit var box: Box<School>
    private lateinit var query: Query<School>
    private lateinit var subscription: DataSubscription
    private lateinit var textView: TextView
    private var school = School()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeUI()
        initializeObjectBox()
        initializeQuery()
        initializeObserver()
        buttonClickListeners()

    }

    private fun buttonClickListeners() {
        buttonAdd.setOnClickListener {
            val strings = editText.text.toString().split(" ")
            school.id = 0
            school.name = strings[0]
            box.attach(school)
            for(i in 1 until strings.size){
                school.students.add(Student(0,strings[i]))
            }
            box.put(school)
            Toast.makeText(this,"New school added",Toast.LENGTH_SHORT).show()
        }

        buttonUpdate.setOnClickListener {
            val strings = editText.text.toString().split(" ")
            school.id = strings[0].toLong()
            school.name = strings[1]
            box.attach(school)
            for(i in 2 until strings.size){
                school.students.add(Student(0,strings[i]))
            }
            box.put(school)
            Toast.makeText(this,"${school.id}. school updated",Toast.LENGTH_SHORT).show()
        }

        /*buttonUpdateStudent.setOnClickListener {
            val strings = editText.text.toString().split(" ")
            Toast.makeText(this,"${box[strings[0].toLong()].students[strings[1].toInt()-1].name}",Toast.LENGTH_SHORT).show()
        }*/

    }

    private fun initializeObserver() {
        subscription = query.subscribe().observer {  schoolsFromBox ->
            val stringBuilder = StringBuilder()
            schoolsFromBox.forEach { schoolFromSchools ->
                stringBuilder.append("${schoolFromSchools.id}. ${schoolFromSchools.name}\n")
                schoolFromSchools.students.forEach {  student ->
                    stringBuilder.append("\t${student.id}. ${student.name}\n")
                }
            }
            textView.text = stringBuilder
        }
    }

    private fun initializeQuery() {
        query = box
            .query()
            .build()
    }

    private fun initializeObjectBox() {
        init(this)
        box = boxStore!!.boxFor(School::class.java)
    }

    private fun initializeUI() {
        buttonAdd = findViewById(R.id.button_add)
        buttonUpdate = findViewById(R.id.button_update)
        buttonUpdateStudent = findViewById(R.id.button_update_students)
        editText = findViewById(R.id.text_input_edit_text)
        textView = findViewById(R.id.text_view)
    }
}