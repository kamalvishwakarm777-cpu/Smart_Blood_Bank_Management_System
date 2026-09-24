# 🩸 Smart Blood Bank Management System

> **A Java-based Object-Oriented Blood Bank Management System for managing donors, blood inventory, hospital requests, staff, and emergency blood requirements.**

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge\&logo=openjdk)
![OOP](https://img.shields.io/badge/OOP-Concepts-blue?style=for-the-badge)
![PBL](https://img.shields.io/badge/Project-PBL-green?style=for-the-badge)
![SDG 3](https://img.shields.io/badge/SDG%203-Good%20Health-red?style=for-the-badge)

---

## 📌 Project Overview

The **Smart Blood Bank Management System** is a console-based Java application developed using **Object-Oriented Programming (OOP)** principles.

The system provides a structured way to manage:

* 🧑‍🤝‍🧑 Blood donors
* 🩸 Blood donations and blood units
* 🏥 Hospitals and blood requests
* 👨‍💼 Staff and administrators
* 📦 Blood inventory
* 🚨 Emergency and routine requests
* ⏳ Blood-unit expiry tracking
* ✅ Donor eligibility

The main goal of the project is to demonstrate how real-world blood-bank operations can be represented using **classes, objects, inheritance, abstraction, encapsulation, polymorphism, collections, and exception handling**.

The project is developed as a **PBL Capstone Project** for:

**Object Oriented Techniques using Java (CCSE0355)**

It is also aligned with:

> **SDG 3 — Good Health and Well-Being**

---

# 🎯 Problem Statement

Traditional or manually managed blood-bank systems can make it difficult to maintain accurate records of donors, available blood units, hospital requests, and expiry dates.

The Smart Blood Bank Management System addresses these challenges by providing a centralized application that can:

1. Register and manage blood donors.
2. Track previous donations.
3. Check whether a donor is eligible to donate again.
4. Create and track individual blood units.
5. Organize inventory according to blood groups.
6. Track blood-unit expiry.
7. Handle emergency and routine hospital requests.
8. Allow administrators to approve or reject requests.
9. Prevent requests from being approved when sufficient valid stock is unavailable.

---

# 💡 Why "Smart"?

The system is called **Smart Blood Bank Management System** because it does more than simply store blood-bank records.

It applies **rule-based decision making** to automate important operations.

For example:

* A donor's last donation date is checked before accepting a new donation.
* Blood units are automatically assigned expiry dates.
* Expired blood is excluded from available inventory.
* Blood requests are checked against current stock.
* Emergency and routine requests follow different processing logic.
* Custom exceptions prevent invalid business operations.

Therefore, the system combines **data management + business rules + OOP-based decision logic**.

---

# 🚀 Key Features

### 👤 Donor Management

* Register new donors.
* Store donor name, contact information, and blood group.
* Maintain donation history.
* Check donor eligibility.
* Enforce the minimum **90-day donation gap**.

### 🩸 Blood Donation

* Record a successful donation.
* Automatically create a new `BloodUnit`.
* Assign blood group and donation date.
* Calculate blood-unit expiry date.
* Add the unit to the appropriate inventory group.

### 📦 Inventory Management

* Store blood units according to blood group.
* View available inventory.
* Check available stock before issuing blood.
* Automatically ignore expired units.
* Issue valid blood units when a request is approved.

### 🏥 Hospital Requests

Hospitals can raise:

* 🚨 Emergency requests
* 📋 Routine requests

Each request contains information such as:

* Request ID
* Hospital
* Blood group
* Number of units required
* Request status
* Request priority

### 👨‍💼 Admin Approval

Administrators can:

* View pending requests.
* Process requests.
* Approve valid requests.
* Reject requests when business conditions are not satisfied.

### ⚠️ Exception Handling

Custom exceptions are used to handle invalid operations such as:

* Insufficient blood stock
* Ineligible donors
* Invalid donation attempts

---

# 🧠 OOP Concepts Demonstrated

The project was specifically designed to demonstrate core **Object-Oriented Programming concepts in Java**.

| OOP Concept            | Implementation                                                        |
| ---------------------- | --------------------------------------------------------------------- |
| **Abstraction**        | `Person` and `BloodRequest` are abstract classes                      |
| **Encapsulation**      | Class fields are private and accessed through methods                 |
| **Inheritance**        | `Person → Donor`, `Person → Staff → Admin`                            |
| **Inheritance**        | `BloodRequest → EmergencyRequest / RoutineRequest`                    |
| **Polymorphism**       | `processRequest()` behaves differently for request types              |
| **Exception Handling** | Custom checked exceptions enforce business rules                      |
| **Collections**        | `HashMap` and `ArrayList` manage system data                          |
| **Composition**        | Objects such as requests and inventory contain/manage related objects |
| **Aggregation**        | The main system manages donors, staff, and requests                   |

---

# 🏗️ System Architecture

The project follows a simple layered object-oriented structure:

```text
                    Smart Blood Bank System
                              │
                              ▼
                 ┌────────────────────────┐
                 │   Console / Main Menu  │
                 └────────────┬───────────┘
                              │
                              ▼
                 ┌────────────────────────┐
                 │   BloodBankSystem       │
                 │   Central Controller    │
                 └────────────┬───────────┘
                              │
          ┌───────────────────┼───────────────────┐
          ▼                   ▼                   ▼
      Donor Management   Request Management   Staff/Admin
          │                   │
          ▼                   ▼
   Donation History     Emergency/Routine
                              │
                              ▼
                    BloodBankInventory
                              │
                              ▼
                       BloodUnit Storage
```

---

# 📊 Class Diagram

The UML class diagram represents the relationships between the major classes in the system.

![Class Diagram](Blood_Bank_Class_Diagram.png)

### UML Relationship Legend

| Symbol            | Meaning                  |
| ----------------- | ------------------------ |
| ▲ Hollow Triangle | Inheritance              |
| ◇ Hollow Diamond  | Aggregation              |
| ◆ Filled Diamond  | Composition              |
| - - →             | Dependency / Association |

---

# 🧩 Main Classes

### `Person` — Abstract Class

Represents common information shared by different people in the system.

**Main attributes:**

* ID
* Name
* Contact

---

### `Donor`

Extends `Person`.

Responsible for:

* Blood group
* Donation history
* Eligibility checking
* Recording donations

---

### `Staff`

Extends `Person`.

Stores information related to blood-bank staff members, including their department.

---

### `Admin`

Extends `Staff`.

Responsible for administrative operations such as:

* Processing requests
* Approving requests
* Rejecting requests

---

### `BloodUnit`

Represents one individual unit of donated blood.

Stores:

* Blood group
* Donation date
* Expiry date
* Availability status

---

### `Hospital`

Represents the hospital requesting blood.

Stores information such as:

* Hospital name
* Location

---

### `BloodRequest` — Abstract Class

Represents a general blood request.

Contains:

* Request ID
* Hospital
* Blood group
* Required units
* Request status

---

### `EmergencyRequest`

Extends `BloodRequest`.

Represents high-priority blood requirements.

It provides emergency-specific request processing.

---

### `RoutineRequest`

Extends `BloodRequest`.

Represents normal/non-emergency blood requirements.

---

### `BloodBankInventory`

Responsible for:

* Storing blood units
* Grouping blood according to blood group
* Checking stock
* Removing expired units
* Issuing blood units

The inventory uses:

```java
HashMap<String, List<BloodUnit>>
```

---

### `BloodBankSystem`

Acts as the central manager of the application.

It connects:

* Donors
* Staff
* Hospitals
* Requests
* Inventory

---

### `SmartBloodBankManagementSystem`

Contains the `main()` method and provides the console-based user interface.

---

# 🔄 System Workflow

The basic workflow of the application is:

```text
Start
  │
  ▼
Main Menu
  │
  ├── Register Donor
  │       │
  │       ▼
  │   Store Donor
  │
  ├── Donate Blood
  │       │
  │       ▼
  │   Check Eligibility
  │       │
  │       ▼
  │   Create BloodUnit
  │       │
  │       ▼
  │   Add to Inventory
  │
  ├── Raise Request
  │       │
  │       ▼
  │   Emergency / Routine
  │       │
  │       ▼
  │   Pending Request
  │
  ├── Admin Approval
  │       │
  │       ▼
  │   Check Inventory
  │       │
  │       ├── Available → Approve
  │       │
  │       └── Not Available → Reject
  │
  └── View Inventory
          │
          ▼
      Display Valid Stock
```

---

# 📋 Main Menu

When the application starts, the user is presented with:

```text
--------------- MAIN MENU ---------------

1. Register New Donor
2. Donate Blood (adds a Blood Unit)
3. Raise Blood Request (Hospital)
4. Approve a Pending Request (Admin)
5. View Inventory
6. View Donors
7. View Requests
8. View Staff/Admin
0. Exit
```

---

# ⚙️ Business Rules

The application implements several real-world business rules.

### Rule 1 — Donor Eligibility

A donor must wait at least **90 days** between donations.

```text
Last Donation
      │
      ▼
Calculate Days Passed
      │
      ▼
Days >= 90 ?
   │       │
  YES      NO
   │        │
Eligible   Not Eligible
```

---

### Rule 2 — Blood Availability

A request cannot be approved if the required number of valid blood units is unavailable.

For example:

```text
Required: 3 units
Available: 2 units

Result → Request cannot be approved
```

---

### Rule 3 — Expired Blood

Expired blood units are not considered available.

```text
Blood Unit
    │
    ▼
Check Expiry Date
    │
 ┌──┴───┐
 ▼      ▼
Valid  Expired
 │       │
Use    Exclude
```

---

### Rule 4 — Request Processing

Different request types use different processing logic.

```text
BloodRequest
     │
     ├───────────────┐
     ▼               ▼
EmergencyRequest  RoutineRequest
     │               │
     ▼               ▼
Emergency Logic   Routine Logic
```

This demonstrates **runtime polymorphism**.

---

# 🛡️ Exception Handling

The system uses custom checked exceptions to protect business rules.

### `BloodNotAvailableException`

Used when the requested blood units are not available.

Example:

```text
Requested: 5 units
Available: 2 units

→ BloodNotAvailableException
```

### `InvalidDonorException`

Used when a donor does not satisfy the donation eligibility requirements.

Example:

```text
Last donation: 30 days ago

→ InvalidDonorException
```

This prevents invalid operations from silently continuing.

---

# 🗂️ Data Structures Used

The project uses Java Collections to manage application data.

### `ArrayList`

Used for collections such as:

```java
ArrayList<Donor>
ArrayList<Staff>
ArrayList<BloodRequest>
```

### `HashMap`

Blood inventory is organized using:

```java
HashMap<String, List<BloodUnit>>
```

Example:

```text
O+  → [BloodUnit, BloodUnit]
A+  → [BloodUnit]
B+  → [BloodUnit, BloodUnit]
AB+ → [BloodUnit]
```

This allows blood units to be grouped according to blood group.

---

# 🖥️ Example Output

### Emergency Request

```text
Processing EMERGENCY request REQ-1 with top priority...

Request REQ-1 -> Status: APPROVED
```

### Inventory

```text
----- Blood Inventory (available units) -----

O+  : 1 unit(s)
B+  : 1 unit(s)
A+  : 2 unit(s)
```

---

# 📁 Project Structure

```text
smart-blood-bank-management-system/
│
├── SmartBloodBankManagementSystem.java
│
├── Blood_Bank_Class_Diagram.pdf
│
├── Blood_Bank_Class_Diagram.png
│
└── README.md
```

Currently, all Java classes are contained in a single source file for simplicity.

```text
SmartBloodBankManagementSystem.java
        │
        ├── Person
        ├── Donor
        ├── Staff
        ├── Admin
        ├── BloodUnit
        ├── Hospital
        ├── BloodRequest
        ├── EmergencyRequest
        ├── RoutineRequest
        ├── BloodBankInventory
        ├── BloodBankSystem
        └── Main Class
```

For a production-style implementation, the classes can later be separated into individual `.java` files and organized into packages.

---

# 🛠️ Technologies Used

| Technology                | Purpose                             |
| ------------------------- | ----------------------------------- |
| **Java**                  | Core application development        |
| **OOP**                   | System architecture                 |
| **Collections Framework** | Data management                     |
| **Exception Handling**    | Business-rule validation            |
| **UML**                   | System/class design                 |
| **Console Interface**     | User interaction                    |
| **Git/GitHub**            | Version control and project sharing |

---

# ▶️ Getting Started

## Prerequisites

Make sure Java is installed:

```bash
java -version
```

and:

```bash
javac -version
```

The project requires **JDK 8 or later**.

---

## Compile

Open the project directory in your terminal and run:

```bash
javac SmartBloodBankManagementSystem.java
```

---

## Run

```bash
java SmartBloodBankManagementSystem
```

---

# 🧪 Sample Test Scenarios

The system can be tested using scenarios such as:

| Test Case                            | Expected Result              |
| ------------------------------------ | ---------------------------- |
| Register a new donor                 | Donor added successfully     |
| Donor donates after 90+ days         | Donation accepted            |
| Donor tries to donate before 90 days | `InvalidDonorException`      |
| Request available blood              | Request can be approved      |
| Request unavailable blood            | `BloodNotAvailableException` |
| Expired blood requested              | Expired units excluded       |
| Emergency request                    | Emergency processing logic   |
| Routine request                      | Routine processing logic     |

---

# 🌱 Future Enhancements

The current project is a console-based academic implementation. It can be extended into a complete real-world system.

### 🌐 1. Web Application

Develop a web interface using:

* HTML
* CSS
* JavaScript
* React

### 🗄️ 2. Database Integration

Replace in-memory collections with a database such as:

* MySQL
* PostgreSQL
* MongoDB

This would allow donor and inventory data to persist after the application closes.

### 🔐 3. Authentication & Authorization

Add secure login for:

* Admin
* Staff
* Hospital users

Different roles could receive different permissions.

### 📱 4. Mobile Application

Develop a mobile application where donors can:

* Register
* View donation history
* Check eligibility
* Receive donation reminders

### 🔔 5. Notification System

Send notifications for:

* Blood shortages
* Donation eligibility
* Upcoming donation dates
* Emergency requirements
* Expiring blood units

### 📊 6. Analytics Dashboard

Add dashboards showing:

* Total donors
* Available blood units
* Blood-group distribution
* Monthly donations
* Emergency requests
* Expired units

### 🤖 7. Smart Demand Prediction

A future version could analyze historical requests to estimate potential blood demand and help staff plan inventory.

### 📍 8. Hospital & Donor Matching

The system could match eligible donors with nearby hospitals or blood banks based on:

* Blood group
* Eligibility
* Location
* Urgency

---

# 🎓 Academic Learning Outcomes

Through this project, the following concepts are demonstrated:

* Understanding classes and objects
* Designing relationships between classes
* Applying abstraction
* Implementing encapsulation
* Using inheritance
* Demonstrating runtime polymorphism
* Handling exceptions
* Using Java Collections
* Implementing business rules
* Designing UML class diagrams
* Converting a real-world problem into an OOP-based solution

---

# 🌍 Sustainable Development Goal

This project supports:

## **SDG 3 — Good Health and Well-Being**

The project is conceptually aligned with SDG 3 because efficient blood inventory management and donor coordination can support timely access to blood resources.

> **Project Focus:** Using software and Object-Oriented Programming concepts to model and improve the organization of blood-bank operations.

---

# 👥 Project Information

**Project:** Smart Blood Bank Management System

**Project Group:** Group 13

**Course:** B.Tech — Computer Science & Engineering

**Subject:** Object Oriented Techniques using Java

**Course Code:** CCSE0355

**Faculty:** Ms. Swati

**Project Type:** PBL Capstone Project

---

# 📜 License

This project was created for **academic and educational purposes** as part of a college PBL/Capstone assignment.

You are free to study, modify, and extend the project for learning and educational purposes.

---

# ⭐ If You Find This Project Useful

If this project helped you understand Java OOP concepts, feel free to:

⭐ Star the repository
🍴 Fork the project
💡 Extend the system with new features
📚 Use it for learning Java and OOP

---

## 🩸 Smart Blood Bank Management System

> **From donor registration to blood inventory and hospital requests — bringing core Java OOP concepts into a real-world healthcare management scenario.**
