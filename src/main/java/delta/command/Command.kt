package delta.command

open class Command(name: String) {
    var msg: String = name

    fun getName(): String {
        return msg
    }

    open fun command(msg: String) {
        // funny
    }
}