---

# 🌟 SAP BusinessObjects Report Query Extraction Script 🌟

Welcome to the **SAP BusinessObjects Report Query Extraction Script**! This Java-based Script is designed to help you log in to a SAP BusinessObjects (BO) server, retrieve queries from reports, and save them to text files. The tool supports both Freehand SQL (FHSQL) and Universe-based reports.

## 📋 Table of Contents
- [Prerequisites](#-prerequisites)
- [Setup](#-setup)
- [How It Works](#-how-it-works)
- [Usage](#-usage)
- [Code Overview](#-code-overview)
- [License](#-license)

## 🛠 Prerequisites

Before you start using this Script, ensure you have the following:

- **Java JDK**: Installed on your machine.
- **Maven/Gradle**: For managing dependencies, if not included manually.
- **OkHttp**: For making HTTP requests.
- **JSON & XML**: Libraries for JSON/XML processing.

## 🚀 Setup

### 1. Clone the Repository
Start by cloning the repository to your local machine:

```bash
git clone https://github.com/swapnilyavalkar/SAP-BO-Getting-SQL-Queries-Webi-Reports.git
cd SAP-BO-Getting-SQL-Queries-Webi-Reports
```

### 2. Add Dependencies
Ensure that your project includes the following dependencies:

```xml
<dependencies>
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>4.9.3</version>
    </dependency>
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20211205</version>
    </dependency>
</dependencies>
```

### 3. Configuration
Update the `MainProgram.java` class with your **CMS IP**, **username**, **password**, and **report ID**:

```java
public static String SOURCE_CMS_IP = "Your_CMS_IP";
public static String SOURCE_CMS_USN = "Your_Username";
public static String SOURCE_CMS_PWD = "Your_Password";
public static String SOURCE_REPORT_ID = "Your_Report_ID"; //You can also mention multiple SI_IDs of the reports as {"760528", "478278"}
```

## 💡 How It Works

This Script follows these main steps:

1. **Log in to the BO Server**: Authenticates the user with the BO server and retrieves a session token.
2. **Retrieve Data Providers**: Extracts the list of data providers (queries) from a given report.
3. **Save Queries to Files**: Depending on the data source type (FHSQL or Universe), the corresponding query is saved to a `.txt` file.
4. **Log off from the Server**: Logs out from the BO server, invalidating the session token.

## 🎮 Usage

### 1. Log in to the Server
The `logonServer()` method logs you into the BO server and retrieves a session token:

```java
String token = Util.logonServer(SOURCE_CMS_IP, SOURCE_CMS_PWD);
```

### 2. Retrieve and Save Queries
Call the `getQueries()` method with the report ID and session token:

```java
Util.getQueries(SOURCE_REPORT_ID, token, reportName);
```
This will save the queries into text files in the current directory.

### 3. Log off from the Server
Finally, log off from the server:

```java
Util.logoffServer(token);
```

### 4. Execute the Program
Compile and run the program using your preferred IDE or via the command line:

```bash
javac MainProgram.java
java MainProgram
```

## 📝 Code Overview

### **Key Methods:**

- **`logonServer(String, String)`**: Authenticates the user and returns a session token.
- **`getQueries(String, String, String)`**: Retrieves queries from the report and saves them to text files.
- **`getFHSQLQuery(String, String, String, String)`**: Handles FHSQL-based queries.
- **`getUniverseQuery(String, String, String, String)`**: Handles Universe-based queries.
- **`logoffServer(String)`**: Logs out from the BO server.

### **Configuration Variables:**

- **`baseURI`**: The base URI for the BO server.
- **`client`**: The OkHttp client used for making HTTP requests.
- **`mediaType`**: The media type set to "application/xml".
- **`filepath`**: The directory where queries are saved.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

By following these steps and guidelines, you'll be able to extract queries from your SAP BusinessObjects reports quickly and efficiently. Feel free to contribute to the project or raise any issues on the repository!

Happy coding! 😊
