import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
object BlockChain {
    val blocks = mutableListOf<Block>()
    private const val DIFFICULTY = 3
    val lastHash: String
        get() = blocks.last().hash

    init {
        val genesisBlock = Block("0".repeat(64), "Genesis Block")
        genesisBlock.add()
    }

    fun Block.add() {
        if (isValid()) {
            blocks.add(this)
        }
    }

    fun getBlock(hash: String): Block? {
        return blocks.find { it.hash == hash }
    }

    fun printBlocks() {
        var i = 0
        for (block in blocks) {

            print("Block $i\n")
            print(
                "\tPrevious Hash: ${block.previousHash}\n" +
                        "\tData: ${block.data}\n" +
                        "\tTime mined: ${block.timestamp}\n" +
                        "\tBlock Hash: ${block.hash}\n" +
                        "\tNonce: ${block.nonce}\n"

            )
            i += 1
        }
    }

    fun Block.mine(): Block {
        // Go through the block until it has x amount of zeros in the front of it's hash
        hash = calculateHash()

        while (!hash.startsWith("0".repeat(DIFFICULTY))) {
            nonce++
            hash = calculateHash()
        }

        print("Block has been mined with a hash of: ${hash}\n")
        return this
    }

    fun load() {
        val json = File("src/main/resources/BlockChain.json").readText()
        blocks.addAll(Json.decodeFromString(json))
    }
}



fun main() {

    BlockChain.printBlocks()


    File("src/main/resources/BlockChain.json").writeText(Json.encodeToString(BlockChain))
}