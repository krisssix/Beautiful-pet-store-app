package cz.mendelu.pef.petstore.ui.screens.add

data class AddPetErrors (
    val communicationError: Int,
    val nameError: String? = null,
    val categoryError: String? = null
)

