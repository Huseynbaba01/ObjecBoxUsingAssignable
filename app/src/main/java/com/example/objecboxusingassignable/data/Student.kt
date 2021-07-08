package com.example.objecboxusingassignable.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class Student(
	@Id (assignable = true) var id: Long? = 0,
	var name: String? = null
){
	lateinit var school: ToOne<School>
}
