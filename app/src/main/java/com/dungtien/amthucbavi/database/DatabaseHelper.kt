package com.dungtien.amthucbavi.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dungtien.amthucbavi.models.Category
import com.dungtien.amthucbavi.models.CookingStep
import com.dungtien.amthucbavi.models.Dish
import com.dungtien.amthucbavi.models.MealPlan
import com.dungtien.amthucbavi.models.User
import org.json.JSONArray
import org.json.JSONObject

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dung_tien_food.db"
        private const val DATABASE_VERSION = 1

        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DatabaseHelper(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                phone TEXT,
                password TEXT NOT NULL,
                created_at TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE categories (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                description TEXT,
                image_url TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE dishes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                category_id TEXT,
                name TEXT NOT NULL,
                price INTEGER NOT NULL,
                cooking_time INTEGER,
                calories INTEGER,
                servings INTEGER,
                difficulty TEXT,
                image_url TEXT,
                description TEXT,
                ingredients TEXT,
                steps TEXT,
                FOREIGN KEY (category_id) REFERENCES categories(id)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE favorites (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                dish_id INTEGER,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE,
                UNIQUE(user_id, dish_id)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE view_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                dish_id INTEGER,
                viewed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE meal_plans (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                dish_id INTEGER,
                meal_type TEXT,
                quantity INTEGER DEFAULT 1,
                plan_date TEXT,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE,
                UNIQUE(user_id, dish_id, meal_type, plan_date)
            )
        """.trimIndent())

        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS meal_plans")
        db.execSQL("DROP TABLE IF EXISTS view_history")
        db.execSQL("DROP TABLE IF EXISTS favorites")
        db.execSQL("DROP TABLE IF EXISTS dishes")
        db.execSQL("DROP TABLE IF EXISTS categories")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        // Sample Users
        val user1 = ContentValues().apply {
            put("full_name", "Nguyễn Văn A")
            put("email", "khachhang@gmail.com")
            put("phone", "0835.272.818")
            put("password", "123456")
            put("created_at", System.currentTimeMillis().toString())
        }
        db.insert("users", null, user1)

        val user2 = ContentValues().apply {
            put("full_name", "Quản lý Dung Tiến")
            put("email", "admin@dungtien.com")
            put("phone", "0835.272.818")
            put("password", "123456")
            put("created_at", System.currentTimeMillis().toString())
        }
        db.insert("users", null, user2)

        // Categories
        val cat1 = ContentValues().apply {
            put("id", "da_dieu")
            put("name", "ĐÀ ĐIỂU")
            put("description", "Thịt đà điểu tươi ngon, đặc sản Ba Vì")
            put("image_url", "https://images.unsplash.com/photo-1544025162-d76694265947?w=900")
        }
        db.insert("categories", null, cat1)

        val cat2 = ContentValues().apply {
            put("id", "mon_bo")
            put("name", "MÓN BÒ")
            put("description", "Món bò mềm thơm, chế biến tại nhà hàng")
            put("image_url", "https://images.unsplash.com/photo-1546964124-0cce460f38ef?w=900")
        }
        db.insert("categories", null, cat2)

        // 10 Official Dishes
        val dishes = listOf(
            makeDish("da_dieu", "ĐĐ nướng tảng", 250000, 25, 420,
                listOf("Thịt đà điểu 500g", "Tỏi, tiêu xanh", "Dầu hào, mật ong", "Rau ăn kèm"),
                listOf("Thấm khô thịt, khứa nhẹ hai mặt.", "Ướp thịt với tỏi, tiêu, dầu hào và mật ong trong 20 phút.", "Nướng áp chảo hoặc than hoa mỗi mặt 6–8 phút, để thịt nghỉ rồi thái lát.")
            ),
            makeDish("da_dieu", "ĐĐ xào măng trúc", 200000, 15, 310,
                listOf("Thịt đà điểu 300g", "Măng trúc 250g", "Tỏi, hành lá", "Dầu ăn, gia vị"),
                listOf("Thái thịt mỏng ngang thớ, ướp gia vị 10 phút.", "Chần măng trúc, để ráo và phi thơm tỏi.", "Xào thịt nhanh tay trên lửa lớn, cho măng vào đảo đều rồi nêm vừa ăn.")
            ),
            makeDish("da_dieu", "ĐĐ xào lăn", 200000, 20, 380,
                listOf("Thịt đà điểu 300g", "Sả, hành tây", "Bột cà ri, nước cốt dừa", "Lạc rang, rau thơm"),
                listOf("Thái thịt mỏng, ướp cà ri, sả và gia vị.", "Phi thơm sả, cho thịt vào đảo nhanh trên lửa lớn.", "Thêm hành tây và chút nước cốt dừa, đảo chín tới rồi rắc lạc rang.")
            ),
            makeDish("da_dieu", "ĐĐ xào sả ớt", 200000, 15, 320,
                listOf("Thịt đà điểu 300g", "Sả băm", "Ớt chuông, ớt tươi", "Tỏi, nước mắm"),
                listOf("Thái thịt mỏng, ướp nước mắm và tiêu.", "Phi thơm sả, tỏi và ớt.", "Cho thịt vào xào lửa lớn 3–4 phút, nêm nếm và dùng nóng.")
            ),
            makeDish("da_dieu", "ĐĐ xào nấm", 200000, 15, 290,
                listOf("Thịt đà điểu 300g", "Nấm hương, nấm sò", "Tỏi, hành lá", "Dầu hào, tiêu"),
                listOf("Sơ chế nấm, thái thịt ngang thớ và ướp dầu hào.", "Phi thơm tỏi, xào thịt nhanh tay rồi trút ra đĩa.", "Xào nấm vừa chín, cho thịt vào đảo đều và thêm hành lá.")
            ),
            makeDish("mon_bo", "Bò nướng tảng", 250000, 25, 480,
                listOf("Thịt bò 500g", "Tỏi, tiêu đen", "Dầu ô liu, bơ lạt", "Khoai tây, rau củ"),
                listOf("Thấm khô thịt bò, ướp muối tiêu và dầu ô liu.", "Áp chảo hoặc nướng mỗi mặt 4–6 phút cùng tỏi và bơ.", "Để thịt nghỉ 5 phút, thái lát và dùng kèm rau củ.")
            ),
            makeDish("mon_bo", "Bò cuốn lá cải", 250000, 20, 360,
                listOf("Thịt bò thái lát 300g", "Lá cải xanh", "Gừng, tỏi", "Nước mắm, chanh"),
                listOf("Rửa sạch lá cải, thái mỏng gừng và chuẩn bị nước chấm.", "Xào nhanh thịt bò với tỏi hoặc dùng thịt bò chần vừa chín.", "Cuốn thịt với lá cải, thêm gừng và chấm nước mắm chanh.")
            ),
            makeDish("mon_bo", "Bò xào măng trúc", 200000, 15, 340,
                listOf("Thịt bò 300g", "Măng trúc 250g", "Tỏi, hành lá", "Dầu hào, tiêu"),
                listOf("Thái bò ngang thớ, ướp dầu hào và tiêu 10 phút.", "Chần măng, phi thơm tỏi rồi xào măng sơ qua.", "Cho bò vào xào lửa lớn đến vừa chín, thêm hành lá.")
            ),
            makeDish("mon_bo", "Bò xào rau muống", 200000, 12, 280,
                listOf("Thịt bò 250g", "Rau muống 1 bó", "Tỏi băm", "Nước mắm, dầu ăn"),
                listOf("Nhặt rau muống, rửa sạch; thái bò mỏng và ướp gia vị.", "Phi thơm tỏi, xào bò nhanh tay rồi trút riêng.", "Xào rau muống lửa lớn, cho bò trở lại đảo đều và nêm vừa ăn.")
            ),
            makeDish("mon_bo", "Bò xào nấm", 200000, 15, 310,
                listOf("Thịt bò 300g", "Nấm đùi gà, nấm kim châm", "Tỏi, hành tây", "Dầu hào, tiêu"),
                listOf("Sơ chế các loại nấm, thái bò ngang thớ và ướp dầu hào.", "Phi thơm tỏi, áp chảo bò nhanh tay rồi để riêng.", "Xào nấm và hành tây, cho bò vào đảo đều rồi thưởng thức.")
            )
        )

        for (dishVal in dishes) {
            db.insert("dishes", null, dishVal)
        }
    }

    private fun makeDish(
        catId: String, name: String, price: Int, time: Int, calories: Int,
        ingredients: List<String>, steps: List<String>
    ): ContentValues {
        val ingJson = JSONArray(ingredients).toString()
        val stepArray = JSONArray()
        val timerList = listOf(2, 10, 8)
        steps.forEachIndexed { index, instr ->
            val obj = JSONObject()
            obj.put("instruction", instr)
            obj.put("timer_minutes", timerList[index % timerList.size])
            stepArray.put(obj)
        }
        return ContentValues().apply {
            put("category_id", catId)
            put("name", name)
            put("price", price)
            put("cooking_time", time)
            put("calories", calories)
            put("servings", 2)
            put("difficulty", "Trung bình")
            put("image_url", "https://images.unsplash.com/photo-1544025162-d76694265947?w=900")
            put("description", "$name – món ngon đặc sản được chế biến tại Nhà hàng Dung Tiến, Ba Vì.")
            put("ingredients", ingJson)
            put("steps", stepArray.toString())
        }
    }

    // --- CRUD METHODS ---

    fun addUser(fullName: String, email: String, phone: String, pass: String): Long {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("full_name", fullName.trim())
            put("email", email.trim().lowercase())
            put("phone", phone.trim())
            put("password", pass)
            put("created_at", System.currentTimeMillis().toString())
        }
        return db.insert("users", null, cv)
    }

    fun findUser(email: String, pass: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email = ? AND password = ?",
            arrayOf(email.trim().lowercase(), pass)
        )
        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow("id"))
                val name = it.getString(it.getColumnIndexOrThrow("full_name"))
                val em = it.getString(it.getColumnIndexOrThrow("email"))
                val ph = it.getString(it.getColumnIndexOrThrow("phone"))
                val cr = it.getString(it.getColumnIndexOrThrow("created_at"))
                return User(id, name, em, ph, cr)
            }
        }
        return null
    }

    fun getUser(id: Long): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", arrayOf(id.toString()))
        cursor.use {
            if (it.moveToFirst()) {
                val name = it.getString(it.getColumnIndexOrThrow("full_name"))
                val em = it.getString(it.getColumnIndexOrThrow("email"))
                val ph = it.getString(it.getColumnIndexOrThrow("phone"))
                val cr = it.getString(it.getColumnIndexOrThrow("created_at"))
                return User(id, name, em, ph, cr)
            }
        }
        return null
    }

    fun updateProfile(id: Long, name: String, phone: String): Int {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("full_name", name.trim())
            put("phone", phone.trim())
        }
        return db.update("users", cv, "id = ?", arrayOf(id.toString()))
    }

    fun getCategories(): List<Category> {
        val list = mutableListOf<Category>()
        val db = readableDatabase
        val sql = """
            SELECT c.*, COUNT(d.id) as dish_count 
            FROM categories c LEFT JOIN dishes d ON c.id = d.category_id 
            GROUP BY c.id
        """.trimIndent()
        val cursor = db.rawQuery(sql, null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow("id"))
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                val desc = it.getString(it.getColumnIndexOrThrow("description"))
                val img = it.getString(it.getColumnIndexOrThrow("image_url"))
                val cnt = it.getInt(it.getColumnIndexOrThrow("dish_count"))
                list.add(Category(id, name, desc, img, cnt))
            }
        }
        return list
    }

    fun getDishes(categoryId: String? = null, sort: String = "name", descending: Boolean = false): List<Dish> {
        val list = mutableListOf<Dish>()
        val db = readableDatabase
        val sortCol = when (sort) {
            "price" -> "price"
            "cooking_time" -> "cooking_time"
            else -> "name"
        }
        val order = if (descending) "DESC" else "ASC"
        val sql = if (categoryId == null) {
            "SELECT * FROM dishes ORDER BY $sortCol $order"
        } else {
            "SELECT * FROM dishes WHERE category_id = ? ORDER BY $sortCol $order"
        }
        val args = if (categoryId == null) null else arrayOf(categoryId)
        val cursor = db.rawQuery(sql, args)
        cursor.use {
            while (it.moveToNext()) {
                list.add(parseDish(it))
            }
        }
        return list
    }

    fun getFeaturedDishes(): List<Dish> {
        val list = mutableListOf<Dish>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM dishes LIMIT 6", null)
        cursor.use {
            while (it.moveToNext()) {
                list.add(parseDish(it))
            }
        }
        return list
    }

    fun getDish(id: Long): Dish? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM dishes WHERE id = ?", arrayOf(id.toString()))
        cursor.use {
            if (it.moveToFirst()) {
                return parseDish(it)
            }
        }
        return null
    }

    fun searchDishes(keyword: String): List<Dish> {
        val list = mutableListOf<Dish>()
        val db = readableDatabase
        val query = "%${keyword.trim()}%"
        val cursor = db.rawQuery(
            "SELECT * FROM dishes WHERE name LIKE ? OR ingredients LIKE ? ORDER BY name",
            arrayOf(query, query)
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(parseDish(it))
            }
        }
        return list
    }

    fun filterDishes(price: Int? = null, categoryId: String? = null, maxMinutes: Int? = null, maxCalories: Int? = null): List<Dish> {
        val list = mutableListOf<Dish>()
        val db = readableDatabase
        val clauses = mutableListOf<String>()
        val args = mutableListOf<String>()

        if (price != null && price > 0) {
            clauses.add("price = ?")
            args.add(price.toString())
        }
        if (!categoryId.isNullOrEmpty()) {
            clauses.add("category_id = ?")
            args.add(categoryId)
        }
        if (maxMinutes != null && maxMinutes > 0) {
            clauses.add("cooking_time <= ?")
            args.add(maxMinutes.toString())
        }
        if (maxCalories != null && maxCalories > 0) {
            clauses.add("calories <= ?")
            args.add(maxCalories.toString())
        }

        val whereSql = if (clauses.isEmpty()) "" else " WHERE " + clauses.joinToString(" AND ")
        val cursor = db.rawQuery("SELECT * FROM dishes$whereSql ORDER BY name", if (args.isEmpty()) null else args.toTypedArray())
        cursor.use {
            while (it.moveToNext()) {
                list.add(parseDish(it))
            }
        }
        return list
    }

    fun addHistory(userId: Long, dishId: Long) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("user_id", userId)
            put("dish_id", dishId)
        }
        db.insert("view_history", null, cv)
    }

    fun getHistory(userId: Long): List<Dish> {
        val list = mutableListOf<Dish>()
        val db = readableDatabase
        val sql = """
            SELECT d.*, h.viewed_at FROM dishes d
            INNER JOIN view_history h ON d.id = h.dish_id
            WHERE h.user_id = ? ORDER BY h.viewed_at DESC
        """.trimIndent()
        val cursor = db.rawQuery(sql, arrayOf(userId.toString()))
        cursor.use {
            while (it.moveToNext()) {
                val dish = parseDish(it)
                val viewedAt = if (it.getColumnIndex("viewed_at") != -1) it.getString(it.getColumnIndexOrThrow("viewed_at")) else null
                list.add(dish.copy(viewedAt = viewedAt))
            }
        }
        return list
    }

    fun clearHistory(userId: Long) {
        val db = writableDatabase
        db.delete("view_history", "user_id = ?", arrayOf(userId.toString()))
    }

    fun isFavorite(userId: Long, dishId: Long): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM favorites WHERE user_id = ? AND dish_id = ?",
            arrayOf(userId.toString(), dishId.toString())
        )
        cursor.use {
            return it.moveToFirst()
        }
    }

    fun toggleFavorite(userId: Long, dishId: Long): Boolean {
        val favorite = isFavorite(userId, dishId)
        val db = writableDatabase
        if (favorite) {
            db.delete("favorites", "user_id = ? AND dish_id = ?", arrayOf(userId.toString(), dishId.toString()))
            return false
        } else {
            val cv = ContentValues().apply {
                put("user_id", userId)
                put("dish_id", dishId)
            }
            db.insert("favorites", null, cv)
            return true
        }
    }

    fun getFavorites(userId: Long): List<Dish> {
        val list = mutableListOf<Dish>()
        val db = readableDatabase
        val sql = """
            SELECT d.* FROM dishes d
            INNER JOIN favorites f ON d.id = f.dish_id
            WHERE f.user_id = ? ORDER BY f.id DESC
        """.trimIndent()
        val cursor = db.rawQuery(sql, arrayOf(userId.toString()))
        cursor.use {
            while (it.moveToNext()) {
                list.add(parseDish(it))
            }
        }
        return list
    }

    fun addMealPlan(userId: Long, dishId: Long, mealType: String, planDate: String) {
        val db = writableDatabase
        val sql = """
            INSERT INTO meal_plans(user_id, dish_id, meal_type, quantity, plan_date)
            VALUES (?, ?, ?, 1, ?)
            ON CONFLICT(user_id, dish_id, meal_type, plan_date)
            DO UPDATE SET quantity = quantity + 1
        """.trimIndent()
        db.execSQL(sql, arrayOf(userId, dishId, mealType, planDate))
    }

    fun getMealPlans(userId: Long): List<MealPlan> {
        val list = mutableListOf<MealPlan>()
        val db = readableDatabase
        val sql = """
            SELECT mp.id, mp.user_id, mp.dish_id, mp.meal_type, mp.quantity,
                   mp.plan_date, d.name as dish_name, d.price as dish_price, 
                   d.calories as dish_calories, d.image_url as dish_image_url
            FROM meal_plans mp INNER JOIN dishes d ON d.id = mp.dish_id
            WHERE mp.user_id = ? ORDER BY mp.plan_date, mp.meal_type
        """.trimIndent()
        val cursor = db.rawQuery(sql, arrayOf(userId.toString()))
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow("id"))
                val uId = it.getLong(it.getColumnIndexOrThrow("user_id"))
                val dId = it.getLong(it.getColumnIndexOrThrow("dish_id"))
                val mType = it.getString(it.getColumnIndexOrThrow("meal_type"))
                val qty = it.getInt(it.getColumnIndexOrThrow("quantity"))
                val pDate = it.getString(it.getColumnIndexOrThrow("plan_date"))
                val dName = it.getString(it.getColumnIndexOrThrow("dish_name"))
                val dPrice = it.getInt(it.getColumnIndexOrThrow("dish_price"))
                val dCal = it.getInt(it.getColumnIndexOrThrow("dish_calories"))
                val dImg = it.getString(it.getColumnIndexOrThrow("dish_image_url")) ?: ""
                list.add(MealPlan(id, uId, dId, mType, qty, pDate, dName, dPrice, dCal, dImg))
            }
        }
        return list
    }

    fun updateMealQuantity(id: Long, quantity: Int) {
        val db = writableDatabase
        if (quantity < 1) {
            db.delete("meal_plans", "id = ?", arrayOf(id.toString()))
        } else {
            val cv = ContentValues().apply { put("quantity", quantity) }
            db.update("meal_plans", cv, "id = ?", arrayOf(id.toString()))
        }
    }

    fun deleteMealPlan(id: Long) {
        val db = writableDatabase
        db.delete("meal_plans", "id = ?", arrayOf(id.toString()))
    }

    private fun parseDish(cursor: android.database.Cursor): Dish {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
        val catId = cursor.getString(cursor.getColumnIndexOrThrow("category_id"))
        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        val price = cursor.getInt(cursor.getColumnIndexOrThrow("price"))
        val time = cursor.getInt(cursor.getColumnIndexOrThrow("cooking_time"))
        val cal = cursor.getInt(cursor.getColumnIndexOrThrow("calories"))
        val serv = cursor.getInt(cursor.getColumnIndexOrThrow("servings"))
        val diff = cursor.getString(cursor.getColumnIndexOrThrow("difficulty"))
        val img = cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
        val desc = cursor.getString(cursor.getColumnIndexOrThrow("description"))
        val ingStr = cursor.getString(cursor.getColumnIndexOrThrow("ingredients")) ?: "[]"
        val stepStr = cursor.getString(cursor.getColumnIndexOrThrow("steps")) ?: "[]"

        val ingredients = mutableListOf<String>()
        val ingArray = JSONArray(ingStr)
        for (i in 0 until ingArray.length()) {
            ingredients.add(ingArray.getString(i))
        }

        val steps = mutableListOf<CookingStep>()
        val stepArray = JSONArray(stepStr)
        for (i in 0 until stepArray.length()) {
            val obj = stepArray.getJSONObject(i)
            steps.add(CookingStep(
                instruction = obj.optString("instruction", ""),
                timerMinutes = obj.optInt("timer_minutes", 0)
            ))
        }

        return Dish(id, catId, name, price, time, cal, serv, diff, img, desc, ingredients, steps)
    }
}
