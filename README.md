# GlucoGuide <img src="https://github.com/user-attachments/assets/ec80e176-fad5-4c84-a502-b30810ace9a5" alt="logo" width="40">

### Smart Healthcare Monitoring Mobile Application for Diabetic Patients

<div style="display: flex; align-items: center;">

<div style="display: grid; grid-template-columns: 2fr auto; align-items: center; gap: 20px;">
    <div>
        This Android application, designed to support diabetic patients in managing their health, integrates several essential features to monitor, visualize, and predict health metrics. This includes an AI-driven early diagnosis tool, nutrition insights, and various tools for medication tracking, lab tests, and doctor management.
    </div>
    <div>
        <img src="https://github.com/user-attachments/assets/a32cd698-ef3b-4f9c-9e9e-4efb04feb62e" alt="chart" width="500">
    </div>
</div>

## Overview

<div style="display: flex; align-items: center; gap: 20px;">
    <div style="flex: 1;">
        <strong>Smart Healthcare Monitoring Mobile Application</strong> provides diabetic patients with a comprehensive platform to monitor, analyze, and manage their health data. The app integrates tracking functionalities for blood glucose levels, blood pressure, insulin intake, and other critical health metrics, enabling users to manage their condition effectively. This application also features an AI-based diabetes risk predictor using the Pima Indian Diabetes dataset for preliminary assessment and offers nutrition tracking through QR/barcode scanning and image recognition.
    </div>
    <div>
        <img src="https://github.com/user-attachments/assets/74433493-a645-49d2-85ae-e2957dd5cc80" alt="Smart Healthcare Monitoring Mobile Application" width="200">
        <img src="https://github.com/user-attachments/assets/27e41a98-3d73-4e76-a7d2-6c071c3a7d52" alt="Screenshot 1" width="200">
        <img src="https://github.com/user-attachments/assets/9a378f56-6607-4cdb-880a-3847b64c06ea" alt="Screenshot 2" width="200">
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

<div style="display: flex; justify-content: space-around; gap: 20px;">
    <img src="https://github.com/user-attachments/assets/422bc91e-a83b-40e3-8e99-0fb64de3860f" alt="Screenshot 1" width="200">
</div>

- **Detailed Feedback**  
  Provides detailed explanations of each metric’s influence on diabetes risk, guiding users to understand the contributing factors.
  <div style="display: flex; justify-content: space-around; gap: 20px;">
    <img src="https://github.com/user-attachments/assets/89ef55ed-f406-4112-94cf-0daa36fc36d0" alt="Screenshot 2" width="200">
    <img src="https://github.com/user-attachments/assets/5cd5c8a2-b5e3-4c2e-b765-b7d363428b9d" alt="Screenshot 3" width="200">
</div>

### 2. Nutrition Analysis
- **Barcode Scanning**  
  Users can scan packaged food items to retrieve nutritional data instantly.  
  <img src="https://github.com/user-attachments/assets/b45671fa-200b-4ae1-a185-a8f67a2f9831" alt="Screenshot" width="200">


- **Image-based Food Analysis**  
  By capturing food images, the app retrieves nutritional values, including glycemic index, carbohydrates, proteins, and fats, for better dietary control.
  <div style="display: flex; justify-content: space-around; gap: 20px;">
      <img src="https://github.com/user-attachments/assets/6e3b6fec-e796-4453-b04d-fcd3f9480238" alt="Screenshot" width="200">
    <img src="https://github.com/user-attachments/assets/a671f8d8-0c84-4eba-a63b-6ea2efd657d5" alt="Screenshot 1" width="200">
    <img src="https://github.com/user-attachments/assets/a722c1b5-f019-4fd0-a491-58a34a38c4ec" alt="Screenshot 2" width="200">
</div>

### 3. Lab Tests Management
- **Comprehensive Test Catalogue**  
  Users can browse lab tests, understand diagnostic parameters, and compare prices to select the most suitable lab.
  <div style="display: flex; justify-content: space-around; gap: 20px;">
    <img src="https://github.com/user-attachments/assets/acbaf066-fb33-4cef-bdc8-1698013bfdde" alt="Screenshot 1" width="200">
    <img src="https://github.com/user-attachments/assets/3eac159d-f7c5-4598-ade3-1431671de4c0" alt="Screenshot 2" width="200">
    <img src="https://github.com/user-attachments/assets/17ff1794-3d00-48ae-89d8-9e55473c23ca" alt="Screenshot 3" width="200">
</div>

- **Appointment Scheduling**  
  In-app lab test booking, where users can select the date and time, with detailed diagnostic information for easy preparation.  
  <div style="display: flex; justify-content: space-around; gap: 20px;">
    <img src="https://github.com/user-attachments/assets/05269f8b-b1f1-4406-b5cd-ddcf0e5903ef" alt="Screenshot 1" width="200">
    <img src="https://github.com/user-attachments/assets/8080bf73-1976-4205-bd3f-540860e3f941" alt="Screenshot 2" width="200">
</div>




### 4. Medicine Ordering and Tracking
- **Medicine Search and Purchase**  
  Users can search for medicines, add them to a cart, and schedule delivery with a few clicks.  
  <div style="display: flex; justify-content: space-around; gap: 20px;">
    <img src="https://github.com/user-attachments/assets/0bcaece2-2cee-4524-80e5-2ec5f02bb0ed" alt="Screenshot 1" width="200">
    <img src="https://github.com/user-attachments/assets/429bfd08-517e-4779-a278-c1db10b5eb1d" alt="Screenshot 2" width="200">
    <img src="https://github.com/user-attachments/assets/2455d5a8-4c1a-4ae3-adce-eb00bfd417d8" alt="Screenshot 3" width="200">
</div>

- **Order Tracking**  
  The app provides real-time updates on order status, with details like expected delivery date and order history.  
  <div style="display: flex; justify-content: space-around; gap: 20px;">
    <img src="https://github.com/user-attachments/assets/c7d08d5a-9e43-4bbc-a5d4-fb5088e1227e" alt="Screenshot 1" width="200">
    <img src="https://github.com/user-attachments/assets/6f30ead5-38a7-4ae3-b085-8e63de8e1d6b" alt="Screenshot 2" width="200">
</div>



### 5. Doctor Management
- **Profile Management**  
  Store multiple doctors' contact details, specialization, and clinic information, including a "Primary Doctor" flag for emergencies.  
  <div style="display: flex; justify-content: space-around; gap: 20px;">
    <img src="https://github.com/user-attachments/assets/f6bf6934-7a8d-47c5-abde-3761b201a3e6" alt="Screenshot 1" width="200">
    <img src="https://github.com/user-attachments/assets/cfeb3c29-b5f6-4183-86b5-f64f0cd3743b" alt="Screenshot 2" width="200">
</div>


### 6. Health Tracker
- **Daily Data Entry**  
  Track daily metrics, such as blood pressure, glucose levels, and medication intake.  
<div style="display: flex; justify-content: space-around; gap: 20px;">
    <img src="https://github.com/user-attachments/assets/f25e812e-9217-4d6d-89d9-6e23e6f8ffa3" alt="Screenshot 1" width="200">
    <img src="https://github.com/user-attachments/assets/18ccf196-7c11-4ca0-afbd-9493c8389557" alt="Screenshot 2" width="200">
</div>

- **Graphical Visualization**  
  View trends over time with interactive charts and graphs for easy health status tracking.  

- **Exporting Data**  
  Users can export health records in CSV or PDF formats, ensuring privacy by storing sensitive data in the internal database.  

- **Key Statistics**  
  Displays averages, blood sugar counts, estimated HbA1c, and trends over time, with customizable views.  

- **Alarms and Reminders**  
  Schedule reminders for data input and medication timing, including persistent notifications even after device reboots.  
  

### 7. Articles & Resources
- **RSS Feed Integration**  
  Get the latest health articles on diabetes, nutrition, and healthy living, with search functionality by topic or date.
  <div style="display: flex; justify-content: space-around; gap: 20px;">
    <img src="https://github.com/user-attachments/assets/d13bc57e-eeb4-4dd4-8fe2-ca661a340bcd" alt="Screenshot 1" width="200">
    <img src="https://github.com/user-attachments/assets/5bc21154-f256-4764-8b6f-a49603252299" alt="Screenshot 2" width="200">
</div>




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
