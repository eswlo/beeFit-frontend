# beeFit
beeFit is a full-stack fitness-tracking plus loyalty program Android app that aims to enhance omnichannel integration and encourage users to stay healthy.

## Table of Contents
* [About](#about)
* [Getting Started](#getting-started)
  * [Backend Setup](#backend-setup)
  * [Frontend Setup](#frontend-setup)
* [Usage](#usage)

## About
Users can earn points by achieving their fitness goals and redeem rewards using the earned points to—hypothetically—shop at the retail stores that the app partners with.

Please be advised that the brands and stores used in this app are for demonstration purposes only.

## Getting Started
The following introduces how to set up the backend and frontend to run the app locally. 

### Backend Setup
* Clone [beeFit-backend](https://github.com/eswlo/beeFit-backend).
* Download and install [Node.js](https://nodejs.org/en) if needed.
* Set up [MongoDB](https://www.mongodb.com/) and have the connection string ready.
* Open the cloned folder in your chosen IDE and execute `npm install` in the terminal to install the necessary dependencies:
* Create a `.env file` in the folder, and add the following code into the file after replacing `CS` with your MongoDB connection string:
```
MONGODB_URI="CS"
```
* Execute `npm run dev` in the terminal to connect to MongoDB.

### Frontend Setup
* Clone this repo and open it in Android Studio.
* Obtain the GEMINI API key at https://ai.google.dev/gemini-api/docs/api-key
* Locate `local.properties`, and add the following code into the file after replacing `AK` with your own api key
```
API_KEY=AK
```
* To get a better experience, it is recommended to run the app on your mobile device. To do so:
   * Retrieve the IP address where the backend is running.
   * Locate the `NetworkService` file in the `api` folder, and replace the `IP` seen in the following code with the retrieved one:
```
private const val BASE_URL = "http://IP"
```
   * Locate the `network_security_config.xml` file and replace the `IP` seen in the following code with the retrieved one:
```
<domain includeSubdomains="true">IP</domain>
```   
   * Pair your device in Android Studio and run the app.

## Usage
### Login & Signup
Press buttons to log in or create a new account:
</br>
</br>
<img src="./docs/assets/demo_login_signup.jpg" width="200">
<img src="./docs/assets/demo_login.jpg" width="200">
<img src="./docs/assets/demo_signup.jpg" width="200">

### Home
Press the button to start tracking and monitoring workout activities, which allows users to earn points.
</br>
<img src="./docs/assets/demo_login_signup.jpg" width="200">
</br>
* The app relies on the accelerometer on the mobile device to determine whether the user is moving/exercising and the duration.  





   
