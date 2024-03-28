
# Proprioception Assessment and Treatment Device With Android App

<!---
![Device Image](device_image.jpg)
-->
## Overview

The Proprioception Assessment and Treatment Device is a real-time solution designed to aid physiotherapists in diagnosing and treating proprioception deficiencies. This portable and affordable device, along with its accompanying Android application, enables efficient assessment, treatment, and record-keeping for patients.

## Screenshots

<img src="https://github.com/SantoshInchalakaranji/Proprioception/blob/master/screenshots/proprioception.png" />

## Features

- **Real-time Assessment**: Utilizes MPU6050 sensor to provide real-time assessment of proprioception deficiencies.
- **Treatment Control**: Controls linear actuators for targeted proprioception treatment.
- **Bluetooth Connectivity**: Allows seamless communication between the device and the Android application.
- **Patient Record Management**: Stores patient records securely within the Android application for easy access and tracking.
- **Dark and Light Theme Support**: Provides theme customization options for user preference.

## Components Used

- MPU6050
- Arduino Nano
- Bluetooth Module
- Linear Actuators

## Technologies Used

- Kotlin: The Android application is developed using Kotlin, a modern programming language for Android development.
- MVVM Architecture: Adopts the Model-View-ViewModel architectural pattern for a clear separation of concerns and improved testability.
- Hilt for Dependency Injection: Utilizes Hilt, an official dependency injection library for Android, to facilitate dependency injection throughout the application.
- Room for Local Storage: Implements Room, an SQLite object mapping library, to provide local storage capabilities for patient records.
- Bluetooth Communication: Utilizes Bluetooth communication for seamless connectivity between the device and the Android application.

## Installation

1. Clone this repository.
   ```bash
   git clone https://github.com/SantoshInchalakaranji/Proprioception.git
   ```

2. Open the Android application project in Android Studio.

3. Connect the Arduino Nano to the device hardware and ensure proper configuration.

4. Build and deploy the Android application to your device.

5. Connect the Android application to the device via Bluetooth.

## Usage

1. Power on the device and ensure Bluetooth connectivity with the Android application.
2. Use the Android application to perform real-time assessment or control treatment sessions.
3. Record patient data and track progress within the application.

## Contributing

Contributions are welcome! Please fork this repository and create a pull request with your proposed changes.


## Acknowledgements

- [Arduino](https://www.arduino.cc/)
- [MPU6050 Library](https://github.com/jrowberg/i2cdevlib/tree/master/Arduino/MPU6050)
- [Bluetooth Communication in Android](https://developer.android.com/guide/topics/connectivity/bluetooth)

---

## Demo Video

[![Demo Video](https://png.pngtree.com/png-vector/20190724/ourmid/pngtree-vector-play-icon-png-image_1572584.jpg)](https://drive.google.com/file/d/1TMpkJ9pATuEPy4eoLDZZhBF1qCw0s0bw/view?usp=sharing)



Absolutely! Here's an updated version of the README with the additional technologies used:

---

