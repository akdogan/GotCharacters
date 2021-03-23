package com.akdogan.swcharacters.datasource

import com.akdogan.swcharacters.datasource.remote.RemoteSourceFakeInstrumented
import com.akdogan.swcharacters.datasource.remote.getRemoteGotTestCharacter

    val instrumentedTestChar = getRemoteGotTestCharacter(
        url = "MyRepositoryTestUrl/123456",
        name = "12345TEST"

    )


    fun setupRemoteSourceFakeWithTestDataInstrumented() = RemoteSourceFakeInstrumented().apply {
        addCharacter(instrumentedTestChar)
    }
