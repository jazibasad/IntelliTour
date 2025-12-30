# IntelliTour - Your Smart Travel Companion

IntelliTour is a modern, feature-rich Android application designed to be a smart travel companion for tourists in Pakistan. It provides users with everything they need to plan their next adventure, from AI-powered trip suggestions to real-time weather forecasts and tour package bookings.

---

## Features

*   **User Authentication**: Secure user registration and login functionality using **Firebase Authentication**.
*   **Cloud Data Storage**: User profile information and tour bookings are saved securely online using **Cloud Firestore**.
*   **AI-Powered Trip Planner**: A sophisticated, on-device Machine Learning feature that suggests the perfect tour package based on a user's budget and desired trip duration. 
*   **Interactive Map**: A full-screen map feature that shows the user's current location with a live "blue dot" and allows for searching other locations. Powered by **MapLibre** and **Geoapify**.
*   **Real-time Weather Forecast**: A professional weather screen that displays the current weather conditions and a 5-day future forecast for any city. Powered by the **OpenWeatherMap API**.
*   **Tour Packages**: A beautifully designed, multi-page list of 10 real-world tour packages in Pakistan, complete with images and descriptions.
*   **Wikipedia Integration**: Each tour package and AI suggestion includes a direct link to the location's Wikipedia page for more information.
*   **Booking & Payment Simulation**: A complete booking flow where users can select a package and simulate a payment, with the booking details saved to their user account in Firestore.
*   **Professional UI/UX**: The app features a modern, clean, and professional design with a consistent color scheme, custom fonts, and smooth animations.

---

## Technologies & Libraries Used

*   **UI & Design**: Material Design 3, `MaterialCardView`, `ConstraintLayout`, `NestedScrollView`, `RecyclerView`
*   **Authentication**: Firebase Authentication
*   **Database**: Cloud Firestore
*   **Machine Learning**: TensorFlow Lite (for on-device inference)
*   **Networking**: Retrofit & Gson (for weather API calls)
*   **Mapping**: MapLibre GL Native for Android & Geoapify (for map tiles)
*   **Image Loading**: (Implicitly used for package images, can be added if needed, e.g., Glide/Picasso)
*   **Location Services**: Google Play Services `FusedLocationProviderClient`

---

## Setup and Configuration

To build and run this project, you will need to configure three API keys.

### 1. Firebase

1.  Create a new project in the [Firebase Console](https://console.firebase.google.com/).
2.  Add an Android app with the package name `com.example.intellitour`.
3.  Download the `google-services.json` file and place it in the `app/` directory.
4.  Enable **Authentication** (Email/Password method) and **Cloud Firestore** in the console.

### 2. Weather API (OpenWeatherMap)

1.  Create a free account at [OpenWeatherMap](https://openweathermap.org/)
2.  Get your API key from your account dashboard.
3.  Open `app/src/main/java/com/example/intellitour/WeatherActivity.java` and paste your key into the `API_KEY` variable.

### 3. Map API (Geoapify)

1.  Create a free account at [Geoapify](https://www.geoapify.com/)
2.  Get your API key from your account dashboard.
3.  Open `app/src/main/AndroidManifest.xml` and paste your key into the `com.geoapify.API_KEY` meta-data value.

### 4. AI Model

This project uses a custom-trained TensorFlow Lite model for its AI Planner.

1.  The model was trained in a Python environment (e.g., Google Colab) using a dataset of the 10 tour packages.
2.  The trained model, named `recommendation_model.tflite`, must be placed in the `app/src/main/assets/` directory.

### 5. Package Images

The tour package screens require 10 local image files.

1.  Find 10 images, one for each package.
2.  Name them according to the convention (`pkg_hunza.jpg`, `pkg_skardu.jpg`, etc.).
3.  Place all 10 images in the `app/src/main/res/drawable/` directory.
