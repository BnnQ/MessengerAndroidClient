package me.bnnq.jetpacksandbox.models

class Result {
    var success : Boolean = false
        private set

    var errorMessage : String? = null
        private set

    constructor() {
        this.success = true
    }
    constructor(errorMessage : String) {
        this.success = false
        this.errorMessage = errorMessage
    }
}