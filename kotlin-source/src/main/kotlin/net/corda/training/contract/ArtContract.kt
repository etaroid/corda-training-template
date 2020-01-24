package net.corda.training.contract

import net.corda.core.contracts.*
import net.corda.core.contracts.Requirements.using
import net.corda.core.transactions.LedgerTransaction
import net.corda.finance.contracts.asset.Cash
import net.corda.finance.contracts.utils.sumCash
import net.corda.training.state.ArtState

@LegalProseReference(uri = "<prose_contract_uri>")
class ArtContract : Contract {
    companion object {
        @JvmStatic
        val ART_CONTRACT_ID = "net.corda.training.contract.ArtContract"
    }

    interface Commands : CommandData {
        class Issue : TypeOnlyCommandData(), Commands
        class Transfer : TypeOnlyCommandData(), Commands
        class Exit : TypeOnlyCommandData(), Commands
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<ArtContract.Commands>()
        when (command.value) {
            is Commands.Issue -> requireThat {
                "No inputs should be consumed when issuing an Art." using (tx.inputs.isEmpty())
                "Only one output state should be created when issuing an Art." using (tx.outputs.size == 1)
                val art = tx.outputsOfType<ArtState>().single()
                "The appraiser and owner cannot have the same identity." using (art.appraiser != art.owner)
                "Both Appraiser and Owner together only may sign Art issue transaction." using
                        (command.signers.toSet() == art.participants.map { it.owningKey }.toSet())
            }
            is Commands.Transfer -> requireThat {
                // ...
            }
            is Commands.Exit -> {
                // ...
            }
        }
    }
}
