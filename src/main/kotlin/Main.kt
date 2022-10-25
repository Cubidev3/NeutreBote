import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.user.PresenceUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PresenceUpdate
import dev.kord.gateway.PrivilegedIntent

const val token = "Bote o token do seu bot aqui"

@OptIn(PrivilegedIntent::class)
suspend fun main()
{
    val kord = Kord(token)

    kord.on<PresenceUpdateEvent> {
        print(this.presence.status.value)
    }

    kord.on<MessageCreateEvent> {
        if (!this.member?.isBot!!)
        {
            val channel = message.channel
            val emNeutre = toNeutralPhrase(message.content)
            if (!message.content.contentEquals(emNeutre)) channel.createMessage("E Correte é: ${toNeutralPhrase(message.content)}")
        }
    }

    kord.login {
        intents += Intent.MessageContent
    }
}

fun toNeutralPhrase(message: String) : String
{
    val words = message.split(' ')
    print(words)
    return reunite(words.map { word -> toNeutralWord(word) })
}

fun toNeutralWord(word: String) : String
{
    var (neutre,excess) = splitExcess(word.lowercase())

    when (neutre) {
        "ele" -> return "elu$excess"
        "ela" -> return "elu$excess"
        "dele" -> return "delu$excess"
        "dela" -> return "delu$excess"
        "nele" -> return "nelu$excess"
        "nela" -> return "nelu$excess"
        "este" -> return "estu$excess"
        "esta" -> return "estu$excess"
        "esse" -> return "essu$excess"
        "essa" -> return "essu$excess"
        "aquele" -> return "aquelu$excess"
        "aquela" -> return "aquelu$excess"
        "naquele" -> return "naquelu$excess"
        "naquela" -> return "naquelu$excess"
        "daquele" -> return "daquilu$excess"
        "daquela" -> return "daquilu$excess"
    }

    if (neutre.endsWith('a') || neutre.endsWith('o'))
    {
        neutre = neutre.dropLast(1)
        return matchCase (neutre + 'e', word) + excess
    }

    return word
}

fun reunite(list: List<String>) : String
{
    return list.joinToString(" ")
}

fun matchCase(a: String, match: String) : String
{
    if (a == "") return ""
    if (match == "") return a
    val matched = when  {
        match.first().isLowerCase() -> a.first().lowercaseChar()
        match.first().isUpperCase() -> a.first().uppercaseChar()
        else -> a.first()
    }
    return matched + matchCase(a.drop(1), match.drop(1))
}

fun splitExcess(word: String) : Pair<String,String> {
    if (word.last().isLetter() && word.last() != 's') return Pair(word, "")
    val excess = word.takeLast(1)
    val (w,e) = splitExcess(word.dropLast(1))
    return Pair(w, e + excess)
}