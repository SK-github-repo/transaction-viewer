# Transaction Viewer - Backend

## Table of contents
* [General info](#general-info)
* [CSV Validation](#csv-validation)
* [Technologies](#technologies)
* [Prerequirements](#prerequirements)
* [Setup](#setup)
* [Related project](#related-project)

## General info

This project enables uploading and viewing transactions which were uploaded in CSV files.  
The transactions can be uploaded with different currencies.   
The amount of transaction can be converted to other currency according to exchange rate for the date when the transaction was made.  
For this moment the only option on the website is to show the transaction amount in the currency relation PLN -> EUR.  
In case the date of some transaction doesn't fit in given currency exchange rate date range then the shown converted amount will be 0.  

## CSV Validation

Samples of CSV files are described in frontend part. The validation of files is run on the backed.  
Except the validation of providing required fields there are few other requirements which needs to be achieved.  

The exchange rate file validation:
- The date ranges cannot interfere each other. In case they interfere a validation error will be thrown.
- Currency needs to be added to database before exchange rate for given currency can be uploaded.  

The transaction file validation:
- *Transaction id* cannot be duplicated. In case of duplication a validation error will be thrown.
- Transaction currency needs to be added to database before transactions in given currency can be uploaded.

## Technologies
Project is created with:
* SpringBoot: 2.2.4
* Java: 13
* JUnit: 5.6
* AssertJ 3.11.1
* Lombok
* H2 in-memory database

## Prerequirements
To download and run this project install below software:
* Java: 13
* Git: newer than 2.0
* Maven: newer than 3.3.9 (Optional)

To test the application:
* Run the frontend project  
OR use:  
* Postman

## Setup
To run this project, install it locally following below steps: 

#### 1. Download project from github
To copy project from github run below command in your terminal in location you want to save the project:

```
$ git clone <project link.git>
```

#### 2. Run Maven command to create \*.war file
Running below command will generate \.war file which can be locally deployed:
```
$ ./mvnw package
```
(Without local Maven): 
```
$ mvn package
```

#### 3. Deploy \*.war file
*\*While running below commands you need to be in the same directory as the \*.war file* <br/>
To quickly deploy this project you can run below command. 
```
$ java -jar viewer-0.0.1-SNAPSHOT.war
```

## Related project
Frontend part of this project - https://github.com/SK-github-repo/transaction-viewer-react
