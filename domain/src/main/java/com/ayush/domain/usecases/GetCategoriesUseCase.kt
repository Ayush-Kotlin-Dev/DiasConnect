package com.ayush.domain.usecases


import com.ayush.domain.model.Category
import com.ayush.domain.model.SubCategory
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor() {
    suspend operator fun invoke(): Result<List<Category>> {
        delay(1000) // Simulate network delay

        return Result.success(
            listOf(
                Category(1, "For You", "https://example.com/for_you.jpg"),
                Category(2, "Grocery", "https://example.com/grocery.jpg",
                    listOf(
                        SubCategory(27, "Packaged Foods", "https://images.pexels.com/photos/2252584/pexels-photo-2252584.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
                        SubCategory(28, "Personal Care", "https://images.pexels.com/photos/3735149/pexels-photo-3735149.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
                        SubCategory(29, "Household Care", "https://images.pexels.com/photos/4239013/pexels-photo-4239013.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
                        SubCategory(21, "Staples", "https://images.pexels.com/photos/17525263/pexels-photo-17525263/free-photo-of-peach-juice-in-glasses.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
                        SubCategory(22, "Masalas & Spices", "https://images.pexels.com/photos/17525263/pexels-photo-17525263/free-photo-of-peach-juice-in-glasses.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
                        SubCategory(23, "Dry Fruits, Nuts & Seeds", "https://images.pexels.com/photos/17525263/pexels-photo-17525263/free-photo-of-peach-juice-in-glasses.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
                        SubCategory(24, "Rice & Rice Products", " https://images.pexels.com/photos/1251198/pexels-photo-1251198.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
                        SubCategory(25, "Oils & Ghee", "https://images.pexels.com/photos/33783/olive-oil-salad-dressing-cooking-olive.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
                        SubCategory(26, "Snacks & Beverages", "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),

                        
                    )
                ),
                Category(3, "Fashion", "https://example.com/fashion.jpg"),
                Category(4, "Appliances", "https://example.com/appliances.jpg"),
                Category(5, "Mobiles", "https://example.com/mobiles.jpg"),
                Category(6, "Electronics", "https://example.com/electronics.jpg"),
                Category(7, "Smart Gadgets", "https://example.com/smart_gadgets.jpg"),
                Category(8, "Home", "https://example.com/home.jpg")
            )
        )
    }
}