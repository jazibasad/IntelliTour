# IntelliTour - Smart Tourism Companion App

IntelliTour is a feature-rich Android application designed to revolutionize tourism in Pakistan. It serves as a smart travel companion, integrating AI-based recommendations, real-time weather forecasting, interactive mapping, and a seamless booking system to provide a complete travel experience.

---

## 📱 Features

### 1. **AI-Powered Trip Planner**
*   **On-Device Machine Learning**: Utilizes a custom **TensorFlow Lite** model to analyze user preferences.
*   **Smart Recommendations**: Users input their **Budget** and **Duration**, and the app intelligently suggests the best matching tour package (e.g., Hunza, Skardu, Naran).
*   **Detailed Itineraries**: Instantly generates a day-by-day travel plan based on the suggested package.

### 2. **Interactive Maps & Navigation**
*   **Vector Maps**: Powered by **MapLibre GL** and **Geoapify** for fast, beautiful map rendering.
*   **Live Tracking**: Shows the user's current location with a directional "puck" indicator.
*   **Search**: Includes a floating search bar to find and zoom into any location globally.
*   **Smart Zoom**: Automatically centers and zooms on the user's location for better orientation.

### 3. **Real-Time Weather Forecast**
*   **Current Conditions**: Displays live temperature, humidity, and weather conditions for any searched city.
*   **5-Day Forecast**: Provides a scrollable list of future weather predictions to help users plan ahead.
*   **Powered by**: Integration with the **OpenWeatherMap API** using **Retrofit** for efficient networking.

### 4. **Tour Packages & Booking**
*   **Curated Destinations**: Features 10 premium tour packages including Hunza, Skardu, Swat, Kashmir, and more.
*   **Visual Cards**: Beautifully designed cards with local imagery and key details (Price, Duration, Hotel).
*   **Wikipedia Integration**: Direct links to Wikipedia articles for each destination.
*   **Booking System**: A streamlined booking flow where users can select a package.
*   **Payment Simulation**: Supports simulated payments via Easypaisa and Bank Transfer.

### 5. **User Accounts & Cloud Sync**
*   **Authentication**: Secure Sign Up and Login system powered by **Firebase Authentication**.
*   **Cloud Database**: Stores user profiles and booking history securely in **Cloud Firestore**.

---

## 🛠️ Tech Stack

*   **Language**: Java
*   **Minimum SDK**: API 26 (Android 8.0)
*   **UI Components**: Material Design 3, ConstraintLayout, NestedScrollView, RecyclerView, CardView.
*   **Backend**: Firebase (Auth, Firestore).
*   **Machine Learning**: TensorFlow Lite.
*   **Networking**: Retrofit, Gson.
*   **Mapping**: MapLibre SDK, Geoapify Tiles.
*   **Location**: Google Play Services (FusedLocationProvider).

---

## 🚀 Setup & Installation

To run this project locally, you must configure the following API keys and assets.

### 1. Firebase Setup
1.  Go to the [Firebase Console](https://console.firebase.google.com/).
2.  Create a project and add an Android app with package: `com.example.intellitour`.
3.  Download `google-services.json` and place it in the `app/` folder.
4.  Enable **Authentication** (Email/Password) and **Firestore** in the console.

### 2. Map Configuration (Geoapify)
1.  Sign up at [Geoapify](https://www.geoapify.com/).
2.  Get your API Key.
3.  Open `app/src/main/AndroidManifest.xml` and replace the placeholder:
    ```xml
    <meta-data
        android:name="com.geoapify.API_KEY"
        android:value="YOUR_GEOAPIFY_KEY_HERE" />
    ```

### 3. Weather Configuration (OpenWeatherMap)
1.  Sign up at [OpenWeatherMap](https://openweathermap.org/).
2.  Get your API Key.
3.  Open `WeatherActivity.java` and replace the placeholder:
    ```java
    private static final String API_KEY = "YOUR_OPENWEATHER_KEY_HERE";
    ```

### 4. AI Model Setup
1.  Ensure the trained model file `recommendation_model.tflite` is present in:
    `app/src/main/assets/`

### 5. Image Assets
1.  Ensure the 10 package images (`pkg_hunza.jpg`, `pkg_skardu.jpg`, etc.) are placed in:
    `app/src/main/res/drawable/`

---

## 📂 Project Structure

*   **`AiPlannerActivity.java`**: Handles TFLite model inference and itinerary generation.
*   **`MapActivity.java`**: Manages MapLibre map, location permissions, and search.
*   **`WeatherActivity.java`**: Handles Retrofit API calls for weather data.
*   **`PackagesActivity.java`**: Displays the list of tour packages.
*   **`PaymentActivity.java`**: Manages the booking confirmation and Firestore write operations.

---

## 📝 License

This project is created for educational and development purposes.
