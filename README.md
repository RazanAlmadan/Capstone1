# 🛒 MerchVerse — Pop-Culture E-Commerce 

**MerchVerse** is a robust Spring Boot RESTful API designed for a specialized e-commerce platform featuring Anime, Marvel, and Gaming merchandise. The system handles full e-commerce operations including multi-merchant stock management, account administrative controls, promotional discount management, and budget-aware product recommendations.

---

## 🛠️ Tech Stack & Architecture

* **Framework:** Java 17 / Spring Boot
* **Build Tool:** Maven
* **Architecture:** Layered Architecture (Controller $\rightarrow$ Service $\rightarrow$ Data Repository / In-Memory State)
* **Data Persistence:** In-Memory List & Object State Management

---

## ✨ Key Features & Business Logic

### 💼 Admin & User Management
* **Role-Based Access:** Distinction between `Customer` and `Admin` users across endpoints.
* **Account Freeze / Unfreeze:** Admins can freeze suspended accounts, holding user balances safely, and unfreeze accounts to restore user purchasing power.
* **Gift Card Transfer:** Customers can purchase and send $50 gift card balance discounts to other users.

### 📦 Multi-Merchant Stock & Inventory
* **Stock Tracking:** Track stock levels across multiple merchants for identical or distinct products.

### 🎟️ Global Promotions & Price Snapshots
* **Global System Discounts:** Admins can apply store-wide promotional percentage discounts (e.g., 20% off) across all products.
* **Price Snapshot & Restoration:** Deep-copy price backup mechanism ensures original product prices can be restored without losing baseline figures.

### 💡 Budget-Aware Recommendation Engine
* **Wallet-Based Recommendations:** Recommends products across the entire store that fit strictly within a customer's available balance.
* **Category & Budget Recommendations:** Filters products within a specific fandom category (e.g., Anime, Marvel, Gaming) that a customer can afford.

---

## 🚀 API Endpoints Overview

### 1. Categories (`/api/v1/category`)
* `GET /get` — Retrieve all product categories
* `POST /add` — Create a new category

### 2. Merchants (`/api/v1/merchant`)
* `GET /get` — Retrieve all merchants
* `POST /add` — Register a new merchant

### 3. Products (`/api/v1/product`)
* `GET /get` — Retrieve all products
* `POST /add` — Add a new product

### 4. Merchant Stock (`/api/v1/stock`)
* `GET /get` — Get all merchant stock items
* `POST /add` — Add stock to a merchant
* `GET /get/low/stock` — Get products with low stock levels


### 5. User & Admin Operations (`/api/v1/user`)
* `GET /get` — List all registered users
* `POST /add` — Register a new user (`Customer` or `Admin`)
* `PUT /buy/{userID}/{productID}/{merchantID}/{quantity}` — Purchase a product
* `PUT /gift-card/{senderID}/{receiverID}` — Send a gift card to another user
* `PUT /add/discount/{adminID}/{discount}` — *(Admin)* Apply store-wide percentage discount
* `DELETE /delete/discount/{adminID}` — *(Admin)* Remove global discount and restore original prices
* `PUT /freeze/{adminID}/{customerID}` — *(Admin)* Suspend user account and hold balance
* `PUT /unfreeze/{adminID}/{customerID}` — *(Admin)* Reactivate user account and restore balance
* `GET /get/recommendations/{userID}` — Get product recommendations fitting user balance
* `GET /get/recommendations/{userID}/{categoryName}` — Get category-specific budget recommendations

