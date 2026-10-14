# Ẩm Thực Dung Tiến

Ứng dụng Native Android (Kotlin) giới thiệu thực đơn và công thức món ngon Ba Vì, sử dụng SQLite trên thiết bị, xác thực tài khoản cục bộ và lưu phiên đăng nhập.

## Chạy ứng dụng
Dự án đã được chuyển đổi từ Flutter sang **Native Android (Kotlin + XML)**.

1. Nhấn nút **Sync Project with Gradle Files** trên thanh công cụ của Android Studio.
2. Chọn thiết bị (Emulator hoặc thiết bị thật) và nhấn nút **Run ('app')**.

Tài khoản dùng thử:
- Khách hàng: `khachhang@gmail.com` / `123456`
- Quản lý: `admin@dungtien.com` / `123456`

## Cấu trúc
- `app/src/main/java/.../database/DatabaseHelper.kt`: tạo schema, dữ liệu mẫu và truy vấn CRUD.
- `app/src/main/java/.../models/`: các Data Class.
- `app/src/main/java/.../services/AuthService.kt`: đăng nhập, đăng ký, khôi phục và xóa phiên.
- `app/src/main/java/.../member1_auth_home/`: Splash, Auth, Home & Bottom Navigation.
- `app/src/main/java/.../member2_catalog/`: Categories, CategoryDishes, MenuBoard.
- `app/src/main/java/.../member3_search_detail/`: Search, Filter, DishDetail.
- `app/src/main/java/.../member4_cooking_saved/`: CookingGuide, Favorites, ViewingHistory.
- `app/src/main/java/.../member5_planner_profile/`: MealPlanner, Articles, Profile.
