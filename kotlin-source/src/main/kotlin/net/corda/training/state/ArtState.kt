package net.corda.training.state

import net.corda.core.contracts.Amount
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import net.corda.training.contract.ArtContract
import java.util.*

@BelongsToContract(ArtContract::class)
data class ArtState(val artist: String,
                    val title: String,
                    val appraiser: Party,
                    val owner: Party,
                    override val linearId: UniqueIdentifier = UniqueIdentifier()): LinearState {

    override val participants: List<Party> get() = listOf(appraiser, owner)

    fun withNewOwner(newOwner: Party) = copy(owner = newOwner)
}