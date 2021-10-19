package com.jsanzo97.recipemanager.ui.book

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleObserver
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import arrow.core.some
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.jsanzo97.common.Logger
import com.jsanzo97.common_android.*
import com.jsanzo97.common_android.logger.LifecycleLogger
import com.jsanzo97.common_android.ui.CustomFragment
import com.jsanzo97.common_android.ui.extensions.lazyBindView
import com.jsanzo97.common_android.ui.extensions.observe
import com.jsanzo97.common_android.ui.extensions.toHtmlList
import com.jsanzo97.common_android.viewmodel.UiConfigurationViewState
import com.jsanzo97.domain.entity.book.Recipe
import com.jsanzo97.recipemanager.R
import com.jsanzo97.recipemanager.ui.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.Instant

class BookFragment: CustomFragment(R.layout.fragment_book), LifecycleObserver, BookRecipeActionsListener {

    private val logger: Logger by inject()

    private val viewModel: BookViewModel by viewModel()

    private val recipeTextNoRecipes: MaterialTextView by lazyBindView(R.id.recipe_text_no_recipes)
    private val bookRecipeList: RecyclerView by lazyBindView(R.id.recipe_book_list)
    private val recipeDetailsViewLayout: ConstraintLayout by lazyBindView(R.id.recipe_details_layout)
    private val recipeDetailsTitle: MaterialTextView by lazyBindView(R.id.recipe_details_title)
    private val recipeDetailsCategory: MaterialTextView by lazyBindView(R.id.recipe_details_category_value)
    private val recipeDetailsDuration: MaterialTextView by lazyBindView(R.id.recipe_details_duration_value)
    private val recipeDetailsDifficult: MaterialTextView by lazyBindView(R.id.recipe_details_difficult_value)
    private val recipeDetailsSubcategories: MaterialTextView by lazyBindView(R.id.recipe_details_subcategories_value)
    private val recipeDetailsIngredients: MaterialTextView by lazyBindView(R.id.recipe_details_ingredients_value)
    private val recipeDetailsDescription: MaterialTextView by lazyBindView(R.id.recipe_details_description_value)
    private val recipeDetailsCalories: MaterialTextView by lazyBindView(R.id.recipe_details_calories_value)
    private val recipeDetailsBackButton: MaterialButton by lazyBindView(R.id.recipe_details_back)
    private val recipeDetailsCloseButton: FloatingActionButton by lazyBindView(R.id.recipe_details_close)
    private val recipeBookRefreshLayout: SwipeRefreshLayout by lazyBindView(R.id.recipe_book_content)

    private var recipesLoaded = mutableListOf<Recipe>()

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
        viewLifecycleOwner.lifecycle.addObserver(LifecycleLogger(logger.tag("BookFragment")))

        observe(viewModel.states, ::handleViewState)

        bookRecipeList.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        bookRecipeList.setHasFixedSize(true)

        recipeDetailsBackButton.setOnClickListener {
            hideRecipeDetails()
        }

        recipeDetailsCloseButton.setOnClickListener {
            hideRecipeDetails()
        }

        val sharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val username = sharedPrefs.getString(USERNAME_KEY, "") ?: ""
        val lastUsername = sharedPrefs.getString(LAST_USERNAME_LOGGED, "") ?: ""
        val lastUpdate = sharedPrefs.getLong(LAST_BOOK_UPDATE, 0L)

        if (lastUsername != username) {
            viewModel.processAction(BookViewActions.DeleteAllRecipesFromDb)
        } else {
            loadRecipes(username, lastUpdate)
        }

        recipeBookRefreshLayout.setOnRefreshListener {
            loadRecipes(username, lastUpdate, false)
        }

        (requireActivity() as MainActivity).unlockNavDrawer()
    }

    private fun handleViewState(viewState: BookViewStates) {
        when {
            viewState.loading -> showProgressDialog()
            viewState.showRecipes -> showRecipes(viewState.recipeList, viewState.updatedFromRemote)
            viewState.successDeleteRecipe -> updateRecipes(viewState.recipeDeleted)
            viewState.specificError -> showError(getString(viewState.specificMessageId))
            viewState.genericError -> showError(viewState.genericMessage)
            viewState.deletedAllDb -> deletedLastUserRecipes()
        }
    }

    private fun loadRecipes(username: String, lastUpdate: Long, shouldShowSpinner: Boolean = true) {

        val settings = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val shouldUpdateFromDb = settings.getBoolean(getString(R.string.settings_data_usage_key), false)

        when {
            (Instant.now().epochSecond - lastUpdate) > DAY_IN_SECONDS -> {
                viewModel.processAction(BookViewActions.LoadRecipesFromRemote(username, shouldShowSpinner))
            }
            shouldUpdateFromDb -> {
                viewModel.processAction(BookViewActions.LoadRecipesFromDb(shouldShowSpinner))
            }
            else -> {
                viewModel.processAction(BookViewActions.LoadRecipesFromRemote(username, shouldShowSpinner))
            }
        }
    }

    private fun showRecipes(recipes: List<Recipe>, updatedFromRemote: Boolean = false) {
        hideProgressDialog()
        recipeBookRefreshLayout.isRefreshing = false
        logger.d("Recipes retrieves: $recipes")

        if (updatedFromRemote) {
            val sharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putLong(LAST_BOOK_UPDATE, Instant.now().epochSecond)
            editor.apply()
        }

        if (recipes.isEmpty()) {
            bookRecipeList.visibility = View.GONE
            recipeTextNoRecipes.visibility = View.VISIBLE

        } else {
            bookRecipeList.visibility = View.VISIBLE
            recipeTextNoRecipes.visibility = View.GONE

            recipesLoaded = recipes.toMutableList()
            var adapter = bookRecipeList.adapter
            if (adapter != null) {
                adapter = adapter as TripAdapter
                adapter.onNewData(recipes.map { it.toRecipeRepresentable() })
                bookRecipeList.adapter = adapter
            } else {
                bookRecipeList.adapter = TripAdapter(recipes.map { it.toRecipeRepresentable() }.toMutableList(), this)
            }
        }
    }

    override fun showRecipeDetails(name: String) {
        val recipeSelected = recipesLoaded.first { it.name == name }

        recipeDetailsTitle.text = recipeSelected.name
        recipeDetailsCategory.text = recipeSelected.category
        recipeDetailsDuration.text = getString(R.string.recipe_duration_format, recipeSelected.duration).replace(".", ",")
        recipeDetailsDifficult.text = recipeSelected.difficult
        recipeDetailsSubcategories.text = HtmlCompat.fromHtml(recipeSelected.subcategories.map { it.value }.toHtmlList(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        recipeDetailsIngredients.text = HtmlCompat.fromHtml(recipeSelected.ingredients.map { it.name }.toHtmlList(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        recipeDetailsCalories.text = recipeSelected.ingredients.map { it.calories }.reduce { a, b -> a + b }.toString()
        recipeDetailsDescription.text = recipeSelected.description

        bookRecipeList.animate()
            .alpha(0.0f)
            .setDuration(125)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    bookRecipeList.visibility = View.GONE

                    recipeDetailsViewLayout.alpha = 0.0f
                    recipeDetailsViewLayout.visibility = View.VISIBLE

                    recipeDetailsViewLayout.animate()
                        .alpha(1.0f)
                        .setDuration(125)
                        .setListener(null)
                }
            })
    }

    private fun hideRecipeDetails() {
        recipeDetailsViewLayout.animate()
            .alpha(0.0f)
            .setDuration(125)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    recipeDetailsViewLayout.visibility = View.GONE

                    bookRecipeList.alpha = 0.0f
                    bookRecipeList.visibility = View.VISIBLE

                    bookRecipeList.animate()
                        .alpha(1.0f)
                        .setDuration(125)
                        .setListener(null)
                }
            })
    }

    override fun deleteRecipe(name: String) {
        val recipeToDelete = recipesLoaded.first{it.name == name}

        val sharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val username = sharedPrefs.getString(USERNAME_KEY, "") ?: ""

        showDialogWithFunction(getString(R.string.recipe_delete_confirmation)) {
            viewModel.processAction(BookViewActions.DeleteRecipe(username, recipeToDelete))
        }
    }

    private fun updateRecipes(recipeDeletedName: String) {
        val recipeDeleted = recipesLoaded.first{it.name == recipeDeletedName}
        recipesLoaded.remove(recipeDeleted)
        showRecipes(recipesLoaded)
    }

    private fun deletedLastUserRecipes() {
        val sharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val username = sharedPrefs.getString(USERNAME_KEY, "") ?: ""

        val editor = sharedPrefs.edit()
        editor.putString(LAST_USERNAME_LOGGED, username)
        editor.apply()

        viewModel.processAction(BookViewActions.LoadRecipesFromRemote(username))
    }
}
