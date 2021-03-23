package com.akdogan.swcharacters.datasource

import com.akdogan.swcharacters.datasource.remote.RemoteSourceFake
import com.akdogan.swcharacters.datasource.remote.getRemoteGotTestCharacter

/*object TestDataSetup {*/
    var testChar = getRemoteGotTestCharacter(
        url = "MyRepositoryTestUrl/12345",
        father = "/11",
        mother = "/22",
        spouse = "/33"
    )
    const val fatherId = "/11"
    const val motherId = "/22"
    const val spouseId = "/33"
    const val fatherName = "TestFather11"
    const val motherName = "TestMother22"
    const val spouseName = "TestSpouse33"
    val fatherChar = getRemoteGotTestCharacter(
        url = fatherId,
        name = fatherName
    )
    val motherChar = getRemoteGotTestCharacter(
        url = motherId,
        name = motherName
    )
    val spouseChar = getRemoteGotTestCharacter(
        url = spouseId,
        name = spouseName
    )

    fun setupRemoteSourceFakeWithTestData() = RemoteSourceFake().apply {
        addCharacter(testChar)
        //addCharacter(fatherChar)
        //addCharacter(motherChar)
        //addCharacter(spouseChar)
        addInternalIdReference(fatherId, fatherName)
        addInternalIdReference(motherId, motherName)
        addInternalIdReference(spouseId, spouseName)
    }
