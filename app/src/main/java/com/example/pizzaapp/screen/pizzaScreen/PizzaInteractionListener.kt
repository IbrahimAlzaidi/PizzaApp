package com.example.pizzaapp.screen.pizzaScreen

interface PizzaInteractionListener {

    fun onChangePizzaSize(position: Int, size: Float)

    fun onIngredientsPizzaClick(ingredientPosition: Int, pizzaPosition: Int)
}