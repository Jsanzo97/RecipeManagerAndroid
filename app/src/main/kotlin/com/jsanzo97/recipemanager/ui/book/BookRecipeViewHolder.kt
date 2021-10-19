package com.jsanzo97.recipemanager.ui.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.jsanzo97.recipemanager.R

class TripAdapter(
    private val recipeList: MutableList<RecipeRepresentable>,
    private val listener: BookRecipeActionsListener
) : RecyclerView.Adapter<TripAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.custom_recipe, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(cell: ViewHolder, position: Int) {
        cell.name.text = recipeList[position].name
        cell.category.text = recipeList[position].category
        cell.difficult.text = recipeList[position].difficult
        cell.duration.text = recipeList[position].duration

        cell.detailsButton.setOnClickListener {
            listener.showRecipeDetails(recipeList[position].name)
        }

        cell.deleteButton.setOnClickListener {
            listener.deleteRecipe(recipeList[position].name)
        }
    }

    fun onNewData(newTripList: List<RecipeRepresentable>) {
        val diffResult = DiffUtil.calculateDiff(BookDiffUtilCallback(newTripList, recipeList))
        diffResult.dispatchUpdatesTo(this)
        recipeList.clear()
        recipeList.addAll(newTripList)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: MaterialTextView = itemView.findViewById(R.id.recipe_title)
        val category: MaterialTextView = itemView.findViewById(R.id.recipe_category_value)
        val difficult: MaterialTextView = itemView.findViewById(R.id.recipe_difficult_value)
        val duration: MaterialTextView = itemView.findViewById(R.id.recipe_duration_value)
        val detailsButton: MaterialButton = itemView.findViewById(R.id.recipe_details)
        val deleteButton: MaterialButton = itemView.findViewById(R.id.recipe_delete)
    }
}
