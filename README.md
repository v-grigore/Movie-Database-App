# IMDb System Readme

## Overview

The IMDb (Internet Movie Database) system is a console-based application designed to manage information related to productions, actors, users, and requests. The system allows users to perform various tasks such as viewing productions and actors, managing favorites, submitting and solving requests, updating the database, and more. The system supports different user roles, including Admin, Contributor, and Regular User.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Structure](#structure)
3. [User Roles](#user-roles)
4. [Functionality](#functionality)
5. [Running the System](#running-the-system)

## Getting Started

To use the IMDb system, follow these steps:

1. **Clone the Repository:** Clone the IMDb repository to your local machine.
2. **Compile and Run:** Compile the Java code and run the system.

## Structure

The IMDb system is structured into several components:

- **model:** Contains the main IMDb class, enumerations, and data model classes (User, Actor, Production, Request, Rating).
- **gui:** Includes the GUI components for handling the graphical user interface.
- **utils:** Houses utility classes for command-line interaction (CLI) and data parsing.

## User Roles

The IMDb system supports different user roles with specific commands available for each:

- **Admin:**
    - Manage users (add, remove).
    - View and solve requests.
    - Perform various database operations.

- **Contributor:**
    - Contribute to productions and actors.
    - Manage own contributions.
    - Submit and solve requests.

- **Regular User:**
    - View productions and actors.
    - Manage favorites.
    - Submit and view requests.
    - Add ratings to productions.

## Functionality

The IMDb system provides the following key functionalities:

1. **Viewing Information:**
    - View productions and actors.
    - Display notifications and search results.

2. **Favorites:**
    - Add and remove productions or actors from favorites.

3. **Requests:**
    - Submit and manage requests.
    - Solve requests (Admin and Contributors).

4. **Database Management:**
    - Add, update, and remove productions or actors from the database.

5. **Ratings:**
    - Add and remove ratings for productions.

6. **User Management:**
    - Add and remove users (Admin only).

7. **User Experience:**
    - Experience points (XP) are awarded based on user actions.

## Running the System

To run the IMDb system:

- Execute the `run` method in the `IMDB` class.
- Choose the mode (CLI or GUI) when prompted.
- Follow the command-line instructions to interact with the system.

**Note:** Ensure that the required data files (`production.json`, `actors.json`, `accounts.json`, `requests.json`) are present in the `resources` folder.

## Conclusion

The IMDb system is a comprehensive Java-based application designed to manage and interact with movie and actor data. It offers a command-line interface for console interaction and includes features for user management, request handling, database operations, and more. The system is extensible and can be further developed to incorporate additional features and improvements.
