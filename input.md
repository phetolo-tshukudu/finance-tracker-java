Finance Tracker -- Java Desktop Application

**Project overview**

This Finance Tracker is an easy-to-use desktop application meant to
handle user financial transactions, giving a clean classification of
transactions into either income or expense.

It provides the use with the ability to categorize and track and
re-evaluate their spending.

This project, with some minor improvements, is meant to help/ assist
ordinal people to understand their spending by giving them the complete
breakdown of the expenses versus their income allowing to make
calculated adjustments to unnecessary spending.

**Features**

-   Add income and expenses with validation

-   Edit transactions

-   Delete transactions

-   View transactions

-   Database persistence

-   Runtime summary calculation of total income, total expenditure and
    balance.

**System Architecture**

Layered design that provides a very clear separation of different logic.

-   UI Layer

> JavaFX-based user interface designed for clarity and usability

-   Service Layer

> Manages all user transactions , making basic but main financial
> calculations.

-   Persistence Layer

> Saves and manages a database containing a very detailed representation
> of the data.

-   Model Layer

> Definition of data components and properties.

**Technologies used**

-   Java-jdk-21

-   JavaFX

-   JDBC

-   SQLite

-   Eclipse IDE

-   Git

**Database Design**

-   Table Design

The SQL table design offers a layer of validation by enforcing strict
rules and constraints (like NOT NULL and CHECK, improving data
reliability and accuracy.

**How to run the Project**

-   Git clone command

-   Open in eclipse

-   Set up database including database file location

-   Where SQLite is embedded needs setup

-   JDK version required

-   Run main class

**Future Improvements**

-   Add user authentication

-   High level abstraction and information hiding

-   Export reports (PDF/CSV)

-   REST API version

-   Unit testing

-   Maven integration

**Screenshots**

-   **UI**

![](./image1.png){width="6.268055555555556in"
height="3.5243055555555554in"}

-   **Files**

![](./image2.png){width="3.0420909886264216in"
height="2.9483278652668417in"}

**AUTHOR**

Phetolo Tshukudu

BSc Computer science & mathematical Statistics

LinkedIn : <https://www.linkedin.com/in/phetolo-tshukudu-96a8a8249/>

Email : phetolotshukudu@gmail.com
