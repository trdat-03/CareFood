package com.example.carefood.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carefood.Domain.Category
import com.example.carefood.R
import com.example.projectfood.Adapter.CategoryAdapter
import com.example.projectfood.Adapter.PopularAdapter
import com.example.projectfood.Domain.CategoryDomain
import com.example.projectfood.Domain.FoodDomain
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var adapter: RecyclerView.Adapter<*>? = null
    private var adapter2: RecyclerView.Adapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // set View cho Category
        recyclerViewCategory()
        // set View cho Popular
        recyclerViewPopular()
        // set envent cho button trong bottom
        bottomNavigation()
    }

    private fun bottomNavigation() {
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.card_btn1)
        val homeBtn = findViewById<LinearLayout>(R.id.home_btn1)
        floatingActionButton.setOnClickListener {
//            startActivity(
//                Intent(
//                    this@MainActivity,
//                    CardListActivity::class.java
//                )
//            )
        }
        homeBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    MainActivity::class.java
                )
            )
        }
    }

    private fun recyclerViewPopular() {
        var recyclerViewPopularList = findViewById<RecyclerView>(R.id.recyclerView2)
        recyclerViewPopularList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val foodlist = ArrayList<FoodDomain>()
        foodlist.add(
            FoodDomain(
                "Pepperoni pizza",
                "pizza1",
                "slices pepperoni ,mozzarella cheese, fresh oregano,  ground black pepper, pizza sauce",
                9.76
            )
        )
        foodlist.add(
            FoodDomain(
                "Cheese Burger",
                "burger",
                "beef, Gouda Cheese, Special sauce, Lettuce, tomato ",
                8.79
            )
        )
        foodlist.add(
            FoodDomain(
                "Vegetable pizza",
                "pizza2",
                " olive oil, Vegetable oil, pitted Kalamata, cherry tomatoes, fresh oregano, basil",
                8.5
            )
        )
        adapter2 = PopularAdapter(foodlist)
        recyclerViewPopularList.setAdapter(adapter2)
    }

    private fun recyclerViewCategory() {
        var recyclerViewCategoryList = findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategoryList.layoutManager = linearLayoutManager
        val categoryList = getCategoty();
        adapter = CategoryAdapter(categoryList)

        recyclerViewCategoryList.setAdapter(adapter)
    }
    private fun getCategoty():ArrayList<Category> {

        var categoryList = ArrayList<Category>()

        val client = OkHttpClient()

        val moshi = Moshi.Builder().build()
        val usersType = Types.newParameterizedType(List::class.java, Category::class.java)
        val jsonAdapter: JsonAdapter<ArrayList<Category>> = moshi.adapter(usersType)

        val request = Request.Builder()
            .url("http://192.168.1.3/CareFood/getCategory.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Error", "Network Error")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.e("Error", "Network succes")
                val json = response.body?.string()
                val users: ArrayList<Category>? = jsonAdapter.fromJson(json)
                runOnUiThread {
                    if (users != null) {
                        categoryList = users
                    };
                }
            }
        })
        return  categoryList;
    }
}