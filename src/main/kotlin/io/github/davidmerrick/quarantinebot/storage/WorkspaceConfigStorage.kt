package io.github.davidmerrick.quarantinebot.storage

import io.github.davidmerrick.quarantinebot.config.QuarantineBotConfig
import javax.inject.Singleton



/**
 * Stores per-workspace app config
 */

@Singleton
class WorkspaceConfigStorage(
        botConfig: QuarantineBotConfig
) {

    /**
     * Store config for this workspace
     */
    fun storeConfig(payload: WorkspaceConfig){

    }

}