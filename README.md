# Bus Route Monitoring Project

## Getting Started

This repository contains the Bus Route Monitoring project.  
**Note:** The main project branch is `master`. If you are coming from IntelliJ or a different IDE, the project structure may differ.  
Please Push into another branch. Create another one with ChatGPT.
---

## Folder Structure

By default, the workspace contains the following folders:  

- `src` – Contains all source code files
   - `MainApp.java` – The main entry point of the project  
- `lib` – Contains dependencies and external libraries
   - `JavaFX SDK` – JavaFX Folder is also located here  
- `assets` - Contains all img and other assets that is related

## File Paths

When accessing project files, always use relative paths:

- Use `./lib/...` for dependencies or libraries  
- Use `./assets/...` for images or other resources  

> Using relative paths ensures that the project works without manually editing file locations.  
> The exact format may vary depending on the code context (e.g., `FileInputStream`, `FXMLLoader`, etc.).

---

## JavaFX Setup

This project uses **JavaFX 25** for GUI development.

### A. Prerequisites

Ensure you have **Java SDK 25** installed:  
- [Download Java SE 25](https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html)  


> If you are cloning this repo, you can skip this step. The SDK is already included in `./lib/...`

Download the **JavaFX 25 SDK**:  
- [Download JavaFX 25](https://gluonhq.com/products/javafx/)

---

### B. VSCode Setup

1. Install the **Java Extension Pack** from Microsoft.  
2. Add JavaFX to your project’s module path:  
   - Copy the JavaFX SDK to the `lib` folder.  
   - Create `launch.json` in `.vscode`:

To run the project with JavaFX in VS Code, use the following `launch.json` configuration:

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "BusRouteMonitoringSystem",
            "request": "launch",
            "mainClass": "MainApp",
            "vmArgs": "--module-path lib/javafx-sdk-25.0.1/lib --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics"
        }
    ]
}
```
4. **Run the project**:
   - Go to MainApp.java
   - Click the Run button.
   - The Bus Route Monitoring application GUI should start.
  
### C. IntelliJ IDEA Setup

Follow these steps to run the project in IntelliJ IDEA:

1. **Open the project** in IntelliJ IDEA.  
2. **Add JavaFX SDK to the project libraries**:  
   - Go to **File > Project Structure > Libraries**.  
   - Click **+** and select the folder where you downloaded the JavaFX 35 SDK.  
3. **Set the main class**:  
   - Open **Run > Edit Configurations**.  
   - Create a new **Application** configuration (or edit an existing one).  
   - Set the **Main class** to `MainApp`.  
4. **Add VM options for JavaFX**:  
   ```text
   --module-path lib/javafx-sdk-25/lib --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics

5. **Set the Project SDK**:
   - Go to File > Project Structure > Project.
   - Select Java 25 as the Project SDK.

6. **Run the project**:
   - Go to MainApp.java
   - Click the Run button or press Shift + F10.
   - The Bus Route Monitoring application GUI should start.



