package com.jsanzo97.recipemanager.ui.book

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

class BookDiffUtilCallback(
        private val newList: List<RecipeRepresentable>,
        private val oldList: List<RecipeRepresentable>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition] == oldList[oldItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

        val newRecipe = newList[newItemPosition]
        val oldRecipe = oldList[oldItemPosition]

        val diff = Bundle()
        if (newRecipe.name != oldRecipe.name) {
            diff.putString("name", newRecipe.name)
        }

        if (diff.size() == 0) {
            return null
        }
        return diff
    }

}
