# GlucoGuide

### Smart Healthcare Monitoring Mobile Application for Diabetic Patients

<div style="display: flex; align-items: center;">

<div style="display: grid; grid-template-columns: 2fr auto; align-items: center; gap: 20px;">
    <div>
        This Android application, designed to support diabetic patients in managing their health, integrates several essential features to monitor, visualize, and predict health metrics. This includes an AI-driven early diagnosis tool, nutrition insights, and various tools for medication tracking, lab tests, and doctor management.
    </div>
    <div>
        <img src="https://github.com/user-attachments/assets/ec80e176-fad5-4c84-a502-b30810ace9a5" alt="logo" width="200">
    </div>
</div>

---

## Overview

<div style="display: flex; align-items: center; gap: 20px;">
    <div style="flex: 1;">
        <strong>Smart Healthcare Monitoring Mobile Application</strong> provides diabetic patients with a comprehensive platform to monitor, analyze, and manage their health data. The app integrates tracking functionalities for blood glucose levels, blood pressure, insulin intake, and other critical health metrics, enabling users to manage their condition effectively. This application also features an AI-based diabetes risk predictor using the Pima Indian Diabetes dataset for preliminary assessment and offers nutrition tracking through QR/barcode scanning and image recognition.
    </div>
    <div>
        <img src="https://github.com/user-attachments/assets/74433493-a645-49d2-85ae-e2957dd5cc80" alt="Smart Healthcare Monitoring Mobile Application" width="200">
    </div>
</div>



The app leverages two types of databases:
1. **Firebase** - Used for user authentication, storing user data, and handling external service integration.
2. **Internal CSV Database** - A local storage format where patient data is secured and managed offline in CSV format, ensuring patient data confidentiality.

---

## Features

### 1. Diabetes Prediction
- **AI-driven Diagnosis**  
  Users can input various health metrics, such as BMI, glucose, blood pressure, and more, to receive a preliminary diabetes risk assessment based on a pre-trained model.  
  

- **Detailed Feedback**  
  Provides detailed explanations of each metric’s influence on diabetes risk, guiding users to understand the contributing factors.
  ![Prediction Feedback](./screenshots/prediction_feedback.png)

### 2. Nutrition Analysis
- **Barcode Scanning**  
  Users can scan packaged food items to retrieve nutritional data instantly.  
  ![Barcode Scanning](./screenshots/barcode_scanning.png)

- **Image-based Food Analysis**  
  By capturing food images, the app retrieves nutritional values, including glycemic index, carbohydrates, proteins, and fats, for better dietary control.  
  ![Food Image Analysis](./screenshots/food_image_analysis.png)

### 3. Lab Tests Management
- **Comprehensive Test Catalogue**  
  Users can browse lab tests, understand diagnostic parameters, and compare prices to select the most suitable lab.  
  ![Lab Test Catalogue](./screenshots/lab_test_catalogue.png)

- **Appointment Scheduling**  
  In-app lab test booking, where users can select the date and time, with detailed diagnostic information for easy preparation.  
  ![Lab Test Appointment](./screenshots/lab_test_appointment.png)

### 4. Medicine Ordering and Tracking
- **Medicine Search and Purchase**  
  Users can search for medicines, add them to a cart, and schedule delivery with a few clicks.  
  ![Medicine Search](./screenshots/medicine_search.png)

- **Order Tracking**  
  The app provides real-time updates on order status, with details like expected delivery date and order history.  
  ![Order Tracking](./screenshots/order_tracking.png)

### 5. Doctor Management
- **Profile Management**  
  Store multiple doctors' contact details, specialization, and clinic information, including a "Primary Doctor" flag for emergencies.  
  ![Doctor Profile](./screenshots/doctor_profile.png)

### 6. Health Tracker
- **Daily Data Entry**  
  Track daily metrics, such as blood pressure, glucose levels, and medication intake.  
  ![Daily Data Entry](./screenshots/daily_data_entry.png)

- **Graphical Visualization**  
  View trends over time with interactive charts and graphs for easy health status tracking.  
  ![Data Visualization](./screenshots/data_visualization.png)

- **Exporting Data**  
  Users can export health records in CSV or PDF formats, ensuring privacy by storing sensitive data in the internal database.  
  ![Data Export](./screenshots/data_export.png)

### 7. Health Dashboard
- **Key Statistics**  
  Displays averages, blood sugar counts, estimated HbA1c, and trends over time, with customizable views.  
  ![Health Dashboard](./screenshots/health_dashboard.png)

### 8. Alarms and Reminders
- **Medication Reminders**  
  Schedule reminders for data input and medication timing, including persistent notifications even after device reboots.  
  ![Reminders](./screenshots/reminders.png)

### 9. Articles & Resources
- **RSS Feed Integration**  
  Get the latest health articles on diabetes, nutrition, and healthy living, with search functionality by topic or date.  
  ![Health Articles](./screenshots/health_articles.png)

---

## Technical Details

### Architecture & Components

This application is structured for modularity and security, with Firebase and internal databases as the foundation for data management. 

#### Firebase Database
Used for **user authentication** and securely managing user-related data that needs to be accessible across devices, Firebase ensures robust external data management for:
- Authentication
- User preferences
- Synchronizing lab test data and doctor details

#### CSV-based Internal Database
For **sensitive and confidential patient data**, a local CSV format database stores health metrics such as blood glucose and blood pressure readings. This ensures data remains accessible offline and maintains high privacy standards for patient confidentiality.

### Major Files and Classes

| Class                   | Purpose                                              |
|-------------------------|------------------------------------------------------|
| `PredictorActivity.java`| Handles data input and invokes the AI predictor      |
| `NutritionActivity.java`| Manages barcode scanning and food analysis functions |
| `LabTestActivity.java`  | Enables browsing and booking lab tests               |
| `StoreActivity.java`    | Manages medicine search, cart, and ordering          |
| `DoctorAdapter.java`    | Binds doctor data to RecyclerView                    |
| `DashboardFragment.java`| Displays dashboard statistics and visualizations     |
| `AlarmUtils.java`       | Schedules and manages reminders                      |
| `PreferenceStore.java`  | Manages user preferences and settings                |
| `Export.java`           | Executes health data export in PDF or CSV            |

---

## Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Shib-Sankar-Das/GlucoGuide.git
