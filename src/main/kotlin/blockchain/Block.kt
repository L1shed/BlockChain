import kotlinx.serialization.Serializable
import java.security.MessageDigest
import java.time.Instant

@Serializable
data class Block(
    val previousHash: String,
    val data: String,
    val timestamp: Long = Instant.now().toEpochMilli(),
    var hash: String = "",
    var nonce: Int = 0,
) {
    init {
        hash = calculateHash()
    }

    fun isValid(): Boolean {
        return hash == calculateHash()
    }

    // uses SHA256 to digest the data and return a number
    fun calculateHash(): String {
        val data = "$previousHash$timestamp$nonce"
        val hexChars = "0123456789ABCDEF"

        val bytes = MessageDigest
            .getInstance("sha256")
            .digest(data.toByteArray())

        val hash = StringBuilder(bytes.size * 2)
        bytes.forEach {
            val i = it.toInt()
            hash.append(hexChars[i shr 4 and 0x0f])
            hash.append(hexChars[i and 0x0f])
        }

        return hash.toString()
    }
}