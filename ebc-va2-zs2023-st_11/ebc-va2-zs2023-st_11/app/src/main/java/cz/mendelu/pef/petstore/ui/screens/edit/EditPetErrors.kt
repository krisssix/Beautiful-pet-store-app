package cz.mendelu.pef.petstore.ui.screens.add

data class EditPetErrors (
    val communicationError: Int,
    val nameError: String? = null,
    val categoryError: String? = null
)

