package me.bnnq.jetpacksandbox.extensions

fun CharSequence.isWhitespace(): Boolean = this.all { it.isWhitespace() }

fun CharSequence.isEmptyOrWhitespace(): Boolean = this.isWhitespace() || this.isEmpty()
