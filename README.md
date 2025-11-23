# 💻 Spring MVC Project – Support Platform for Reservist Families

## 👥 Team Members
- Roey Yonayov – roeyyo@edu.jmc.ac.il  
- Lara Duek – laradu@edu.jmc.ac.il

## 🧠 General Overview

This web application was developed as part of Exercise 4 in the Internet course – תשפ״ה.  
The goal of the system is to connect families of soldiers in active reserve duty with volunteers from the community who are willing to help with various tasks (e.g., cooking, transportation, shopping, childcare, etc.).

The system allows:
- Registration and management of **families** and **volunteers**.
- Posting and viewing of **help requests** and **volunteering offers**.
- **Automatic matching** between needs and offers.
- **Mutual approval** of matches before opening a **private chat**.
- **Admin functionalities** including approval of families and basic system management.
- **Volunteer profile pictures**, visible to families if the volunteer chooses to upload one.

---

## 📄 Main Features

- ✅ Registration and login with role separation (`FAMILY`, `VOLUNTEER`, `ADMIN`)
- ✅ Server-side validations
- ✅ Data persistence with JPA and MySQL
- ✅ Security with Spring Security
- ✅ Session handling (`HttpSession`) to store temporary user data
- ✅ Thymeleaf view engine (server-side rendering)
- ✅ Full matching system and private chat after mutual match approval
- ✅ Role-based access control
- ✅ Families **must be approved by an admin** to log in
- ✅ Volunteers can log in **without needing approval**
- ✅ Volunteers can **upload a profile picture** visible to matched families

---

## Project Structure

- Java:
  - `controller/` – MVC controllers for each user type
  - `service/` – Business logic layer
  - `repository/` – JPA repository interfaces
  - `model/` – JPA entities 
  - `config/` – Security and general configuration
- Resources:
  - `templates/` – Thymeleaf HTML pages
  - `static/` – CSS and JS files
  - `application.properties` – App and database configuration

> ⚠️ MySQL database connection settings can be found in the `application.properties` file.  
> Make sure to review and configure it correctly before running the app.

---

## 🧰 Requirements

To run this project, you need to have:

- ✅ Java 17 or later
- ✅ Maven
- ✅ MySQL (running locally or remotely)
- ✅ IDE such as IntelliJ IDEA or Eclipse
- ✅ [Lombok](https://projectlombok.org/) plugin enabled in your IDE

---

## 🗃️ Database Setup

Required database name: `ex4`  
Before running the application, create the database using:

```sql
CREATE DATABASE ex4;
````

Then configure the connection in `application.properties`.

---

## 🔐 Initial Admin User

When the application starts, if no admin user exists, one is created automatically with the following credentials:

* **Name:** Admin Admin
* **Email:** `admin@admin.com`
* **ID:** `000000001`
* **Password:** `admin123`
* **Role:** `ROLE_ADMIN`
* **Approval status:** `approved = true`

This logic is handled inside `SupportappApplication.java` using a `CommandLineRunner` bean that checks whether the admin user already exists by email, and if not, creates it automatically.

---

## 💬 Chat Functionality

Once a **family approves a match** with a volunteer, a **private chat** is automatically opened.
Both users can access this chat from the **"Chats"** section and communicate freely.
Contact information (e.g., phone number) may be shared only after mutual agreement.

---

## ⚠️ Final Notes

* The system includes user-friendly error pages and clear feedback messages.
* **The demo video can be found here:** \[https://drive.google.com/drive/folders/1BV_reDsnVPljw0mdKKQn_8OPtd-xH-sD?usp=sharing]

---

## 🐞 Known Bugs

* We have a design inconsistency issue. Due to time constraints, most pages are styled in a clean and pleasant way, but some pages still have a more basic layout. We decided to keep the new design even if it's not fully uniform and slightly different across some pages. The logic and functionality work well, even if the styling varies.

* There is one admin page that does not open properly.

* Of course, there are additional aspects that still need improvement. We would have liked to enhance them, but unfortunately, we ran out of time.
```
```
