package com.jsanzo97.domain.usecase.creation

import arrow.core.Either
import com.jsanzo97.domain.entity.creation.CreationTypesInformation
import com.jsanzo97.domain.error.RecipeManagerError
import com.jsanzo97.domain.repository.creation.CreationRepository

class GetInformationToCreateRecipeUseCase(private val creationRepository: CreationRepository) {

    suspend operator fun invoke(): Either<RecipeManagerError, CreationTypesInformation> = creationRepository.getCreationTypesInformation()

}