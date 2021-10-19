package com.jsanzo97.recipemanager.ui.creation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import arrow.core.some
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.jsanzo97.common.Logger
import com.jsanzo97.common_android.SHARED_PREFERENCES_KEY
import com.jsanzo97.common_android.USERNAME_KEY
import com.jsanzo97.common_android.logger.LifecycleLogger
import com.jsanzo97.common_android.ui.CustomFragment
import com.jsanzo97.common_android.ui.extensions.hideKeyboard
import com.jsanzo97.common_android.ui.extensions.lazyBindView
import com.jsanzo97.common_android.ui.extensions.observe
import com.jsanzo97.common_android.ui.extensions.toHtmlList
import com.jsanzo97.common_android.viewmodel.UiConfigurationViewState
import com.jsanzo97.domain.entity.book.Ingredient
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.domain.entity.book.Subcategory
import com.jsanzo97.domain.entity.creation.CategoryType
import com.jsanzo97.domain.entity.creation.DifficultType
import com.jsanzo97.domain.entity.creation.IngredientType
import com.jsanzo97.domain.entity.creation.SubcategoryType
import com.jsanzo97.recipemanager.R
import com.jsanzo97.recipemanager.ui.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class
CreationFragment: CustomFragment(R.layout.fragment_creation), LifecycleObserver {

    private val logger: Logger by inject()

    private val creationContent: ConstraintLayout by lazyBindView(R.id.creation_content)
    private val creationIngredientContent: MaterialCardView  by lazyBindView(R.id.creation_ingredient_layout)
    private val categorySelector: AppCompatSpinner by lazyBindView(R.id.creation_category_selector)
    private val subcategorySelector: AppCompatSpinner by lazyBindView(R.id.creation_subcategory_selector)
    private val subcategoryValues: MaterialTextView by lazyBindView(R.id.creation_subcategories_selected)
    private val difficultSelector: AppCompatSpinner by lazyBindView(R.id.creation_difficult_selector)
    private val ingredientTypeSelector: AppCompatSpinner by lazyBindView(R.id.creation_ingredient_type_selector)
    private val ingredientsCreated: MaterialTextView by lazyBindView(R.id.creation_ingredients_selected)

    private val creationTitleField: TextInputEditText by lazyBindView(R.id.creation_recipe_title_value)
    private val creationDescriptionField: TextInputEditText by lazyBindView(R.id.creation_description_value)
    private val creationDurationField: TextInputEditText by lazyBindView(R.id.creation_duration_value)
    private val creationIngredientTitleField: TextInputEditText by lazyBindView(R.id.creation_recipe_ingredient_title_value)
    private val creationIngredientCaloriesField: TextInputEditText by lazyBindView(R.id.creation_recipe_ingredient_calories_value)

    private val creationIngredient: MaterialButton by lazyBindView(R.id.creation_ingredient_add)
    private val creationIngredientBack: MaterialButton by lazyBindView(R.id.creation_ingredient_back)
    private val creationIngredientDone: MaterialButton by lazyBindView(R.id.creation_ingredient_done)
    private val clearFields: MaterialButton by lazyBindView(R.id.creation_recipe_clear)
    private val createRecipe: MaterialButton by lazyBindView(R.id.creation_recipe_create)

    private val viewModel: CreationViewModel by viewModel()

    private val subcategoriesSelected: MutableList<String> = mutableListOf()
    private val ingredientsSelected: MutableList<Ingredient> = mutableListOf()
    private lateinit var difficultSelected: String
    private lateinit var categorySelected: String
    private lateinit var ingredientTypeSelected: String

    override var uiConfigurationViewState = UiConfigurationViewState(
        showToolbar = true,
        statusBarColor = R.color.colorPrimaryDark.some(),
        toolbarColor = R.color.colorPrimary.some(),
        toolbarNavigationIconColor = R.color.white.some(),
        showLogoutButton = true
    )

    override fun handleUiConfigurationViewState(uiConfigurationViewState: UiConfigurationViewState) = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleLogger(logger.tag("CreationFragment")))

        observe(viewModel.states, ::handleViewState)

        creationContent.setOnClickListener { hideKeyboard() }

        creationIngredient.setOnClickListener { showCreationIngredientView() }

        creationIngredientBack.setOnClickListener { hideCreationIngredientView() }

        creationIngredientDone.setOnClickListener { createIngredient() }

        clearFields.setOnClickListener { clearFields() }

        createRecipe.setOnClickListener { createRecipe() }

        viewModel.processAction(CreationViewAction.LoadInformationData)

        (requireActivity() as MainActivity).unlockNavDrawer()
    }

    private fun handleViewState(viewState: CreationViewState) {
        when {
            viewState.loading -> showProgressDialog()
            viewState.specificError -> showError(getString(viewState.specificMessageId))
            viewState.genericError -> showError(viewState.genericMessage)
            viewState.loadedTypesInfo -> loadTypes(viewState.ingredientTypes, viewState.difficultTypes, viewState.categoryTypes, viewState.subcategoryTypes)
            viewState.successCreationRecipe -> successCreationRecipe()
        }
    }

    private fun loadTypes(ingredientTypes: List<IngredientType>, difficultTypes: List<DifficultType>, categoryTypes: List<CategoryType>, subcategoryTypes: List<SubcategoryType>) {

        val categoryArray = categoryTypes.map { it.category }.toMutableList()
        categoryArray.add(0, getString(R.string.recipe_creation_select_category))
        val categoryAdapter = ArrayAdapter(
                requireContext(),
                R.layout.custom_spinner_textview,
                categoryArray
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySelector.adapter = categoryAdapter
        categorySelector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i > 0) {
                    categorySelected = categoryArray[i]
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        val subcategoryArray = subcategoryTypes.map { it.value }.toMutableList()
        subcategoryArray.add(0, getString(R.string.recipe_creation_select_subcategory))
        val subcategoryAdapter = ArrayAdapter(
                requireContext(),
                R.layout.custom_spinner_textview,
                subcategoryArray
        )
        subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subcategorySelector.adapter = subcategoryAdapter
        subcategorySelector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i > 0) {
                    subcategorySelected(subcategoryArray[i])
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        val difficultArray = difficultTypes.map { it.difficult }.toMutableList()
        difficultArray.add(0, getString(R.string.recipe_creation_select_subcategory))
        val difficultAdapter = ArrayAdapter(
                requireContext(),
                R.layout.custom_spinner_textview,
                difficultArray
        )
        difficultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        difficultSelector.adapter = difficultAdapter
        difficultSelector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i > 0) {
                    difficultSelected = difficultArray[i]
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        val ingredientTypesArray = ingredientTypes.map { it.type }.toMutableList()
        ingredientTypesArray.add(0, getString(R.string.recipe_creation_select_ingredient_type))
        val ingredientTypesAdapter = ArrayAdapter(
                requireContext(),
                R.layout.custom_spinner_textview,
                ingredientTypesArray
        )
        ingredientTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ingredientTypeSelector.adapter = ingredientTypesAdapter
        ingredientTypeSelector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i > 0) {
                    ingredientTypeSelected = ingredientTypesArray[i]
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        hideProgressDialog()
        creationContent.animate()
            .alpha(1.0f)
            .setDuration(125)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    creationContent.alpha = 0.0f
                    creationContent.visibility = View.VISIBLE
                }
            }
        )
    }

    private fun subcategorySelected(subcategory: String) {
        if (subcategoriesSelected.contains(subcategory)) {
            subcategoriesSelected.remove(subcategory)
        } else {
            subcategoriesSelected.add(subcategory)
        }
        subcategoryValues.text = ""
        subcategoryValues.text = HtmlCompat.fromHtml(subcategoriesSelected.toHtmlList(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun showCreationIngredientView() {
        creationContent.animate()
            .alpha(0.0f)
            .setDuration(125)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    creationContent.visibility = View.GONE

                    creationIngredientContent.alpha = 0.0f
                    creationIngredientContent.visibility = View.VISIBLE

                    creationIngredientContent.animate()
                            .alpha(1.0f)
                            .setDuration(125)
                            .setListener(null)
                }
            }
        )
    }

    private fun hideCreationIngredientView() {
        hideKeyboard()
        creationIngredientContent.animate()
            .alpha(0.0f)
            .setDuration(125)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    creationIngredientContent.visibility = View.GONE

                    creationContent.alpha = 0.0f
                    creationContent.visibility = View.VISIBLE

                    creationContent.animate()
                            .alpha(1.0f)
                            .setDuration(125)
                            .setListener(null)
                }
            }
        )
        ingredientsCreated.text = ""
        ingredientsCreated.text = HtmlCompat.fromHtml(ingredientsSelected.map { it.name }.toHtmlList(), HtmlCompat.FROM_HTML_MODE_LEGACY)

        creationIngredientTitleField.text?.clear()
        ingredientTypeSelected = ""
        creationIngredientCaloriesField.text?.clear()
        creationIngredientCaloriesField.text?.clear()
    }

    private fun clearFields() {
        creationTitleField.text?.clear()
        creationDescriptionField.text?.clear()
        creationDurationField.text?.clear()
        difficultSelector.setSelection(0)
        difficultSelected = ""
        ingredientTypeSelector.setSelection(0)
        ingredientsSelected.clear()
        ingredientsCreated.text = ""
        categorySelector.setSelection(0)
        categorySelected = ""
        subcategorySelector.setSelection(0)
        subcategoriesSelected.clear()
        subcategoryValues.text = ""

    }

    private fun createIngredient() {
        if (creationIngredientTitleField.text.isNullOrEmpty() ||
            (!::ingredientTypeSelected.isInitialized || ingredientTypeSelected.isEmpty()) ||
            creationIngredientCaloriesField.text.isNullOrEmpty() ||
            creationIngredientCaloriesField.text.toString().contains(" ") ||
            creationIngredientCaloriesField.text.toString().toDouble() <= 0.0) {
            showError(getString(R.string.recipe_creation_empty_fields_wrong_values))
        } else {
            showDialogWithFunction(getString(R.string.recipe_creation_ingredient_confirmation)) {
                ingredientsSelected.add(
                    Ingredient(
                            creationIngredientTitleField.text.toString(),
                            ingredientTypeSelected,
                            creationIngredientCaloriesField.text.toString().toDouble()
                    )
                )
                hideCreationIngredientView()
            }
        }
    }

    private fun createRecipe() {
        if (creationTitleField.text.isNullOrEmpty() ||
            creationDescriptionField.text.isNullOrEmpty()||
            creationDurationField.text.isNullOrEmpty() ||
            (!::difficultSelected.isInitialized || difficultSelected.isEmpty()) ||
            ingredientsSelected.isEmpty() ||
            (!::categorySelected.isInitialized || categorySelected.isEmpty()) ||
            subcategoriesSelected.isEmpty() ||
            creationDurationField.text.toString().contains(" ") ||
            creationDurationField.text.toString().toDouble() <= 0.0) {
            showError(getString(R.string.recipe_creation_empty_fields_wrong_values))
        } else {
            val recipe = Recipe(
                    creationTitleField.text.toString(),
                    creationDescriptionField.text.toString(),
                    creationDurationField.text.toString().toDouble(),
                    difficultSelected,
                    ingredientsSelected,
                    categorySelected,
                    subcategoriesSelected.map { Subcategory(it) }
            )

            val sharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
            val username = sharedPrefs.getString(USERNAME_KEY, "") ?: ""
            showDialogWithFunction(getString(R.string.recipe_creation_confirmation)) {
                viewModel.processAction(CreationViewAction.CreateNewRecipe(username, recipe))
            }
        }
    }

    private fun successCreationRecipe() {
        clearFields()
        hideProgressDialog()
        showSuccess(getString(R.string.recipe_creation_success_message))
        findNavController().navigate(R.id.action_fragment_creation_to_fragment_book)
    }
}