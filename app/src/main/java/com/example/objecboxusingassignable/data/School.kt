package com.example.objecboxusingassignable.data

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class School(
	@Id (assignable = true) var id: Long? = 0,
	var name: String? = null
)
{
	@Backlink(to = "school")
	lateinit var students: ToMany<Student>
}
