package com.example.objecboxusingassignable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
    private lateinit var editText: EditText
    private lateinit var box: Box<School>
    private lateinit var query: Query<School>
    private lateinit var subscription: DataSubscription
    private lateinit var textView: TextView
    private var school = School()
    private var student = Student()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeUI()
        initializeObjectBox()
        initializeQuery()
        initializeObserver()
        buttonClickListener()

    }

    private fun buttonClickListener() {
        buttonAdd.setOnClickListener {
            val strings = editText.text.toString().split(" ")
            school.id = 0
            school.name = strings[0]
            box.attach(school)
            for(i in 1 until strings.size){
                student.id = 0
                student.name = strings[i]
                school.students.add(student)
            }
            box.put(school)
        }
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
        editText = findViewById(R.id.text_input_edit_text)
        textView = findViewById(R.id.text_view)
    }
}