# Todoist :clipboard:
A Client-Server To-Do List project for the Modern Java Technologies course, FMI, 2023

## Overview

A console Client-Server application which servers the purpose of task scheduling & management on a daily basis (inspired by [Todoist](https://www.todoist.com/)).

## Command interface & Implementation notes

Each task consists of the following attributes:
  - **name** - the name of the task
  - **date** (optional) - the date we would like to complete the task on
  - **due date** (optional) - the date by which we should complete the task for sure
  - **description** (optional) - a description of the task

### Authorization & Authentication

To use the services of the application, the User needs to create an accout or log into an existing one.

***Note:** The password is not being stored into the memory as plain text, it is first being encoded through the use of the PBKDF2-HMAC-SHA256 Password Storage Scheme which provides a mechanism for encoding user passwords using the PBKDF2-HMAC-SHA256 message digest algorithm.*

- **Create a new account**
```bash
$ register --username=<username> --password=<password>
```
- **log into an account**
```bash
$ login --username=<username> --password=<password>
```
***Note:** All the data that is created within an account is being stored in a data storage (using JSON files and the [Gson](https://github.com/google/gson) library), so all the data is being saved and available throughout different user sessions. The database files are being created for the current file system in case they do not exist after the server has started running.*

- **quit the current session**
```bash
$ quit
```

### Tasks

The User can create tasks and perform a variety of operations with them, described bellow:

- **create a task**

  If a task has no date attribute, it is put into a separate task storage called Inbox
```bash
$ add-task --name=<name> --date=<date> --due-date=<due-date> --description=<description>
```

- **update a task**
  
  Every attribute can be changed except for the name (We use a name to reference tasks from the Inbox and a name and a date to reference all other tasks)
```bash
$ update-task --name=<name> --date=<date> --due-date=<due-date> --description=<description>
```

- **delete a task**

```bash
$ delete-task --name=<task name> 
$ delete-task --name=<task name> --date=<date>
```

- **print task information in human-readable format**

```bash
$ get-task --name=<task name> 
$ get-task --name=<task name> --date=<date> 
```

- **list tasks**

  An additional optional parameter can be used, in order to filter out only the completed tasks
```bash
$ list-tasks 
$ list-tasks --completed=true
$ list-tasks --date=<date>
```

- **list all tasks scheduled for the current date**

```bash
$ list-dashboard 
```

- **mark a task as completed**

```bash
$ finish-task --name=<task name> 
$ finish-task --name=<task name> --date=<date> 
```

### Collaborations

A collaboration is a shared project which can contain tasks that are visible for all members of the project. This type of tasks have one more optional attribute - an assignee (the User that is assigned to complete the task).

- **add a collaboration**

```bash
$ add-collaboration --name=<name> 
```

- **delete a collaboration**

  Can only be done by the owner of the collaboration, deletes all tasks within it
  
```bash
$ delete-collaboration --name=<name> 
```

- **list all collaborations that the current User is a member of**

```bash
$ list-collaborations --name=<name> 
```

- **add a new member to a collaboration**

  Can only be done by the owner of the collaboration

```bash
$ add-user --collaboration=<name> --user=<username> 
```

- **assign a task to a member of the collaboration**

```bash
$ assign-task --collaboration=<name> --user=<username> --task=<name> 
```

- **list all tasks within a collaboration**

```bash
$ list-tasks --collaboration=<name>
```

- **list all members of a collaboration**

```bash
$ list-users --collaboration=<name>
```

## Server notes
This project is a Client-Server application, which allows the Server to work with multiple clients concurrently. The implementation is based on the non-blocking Client-Server architecture, using Java NIO.

## Logging
When an error (such as an I/O exception) occurs during the usage of the Server, it is logged into a text file, which is being created for the current file system in case it does not exist. The logging information contains the type of the error, the error message, and the stack trace. The logging operations are performed through the use of *java.util.logging*.

## Testing framework
All of the application logic is being tested (except for the Server and Client implementations) through the use of [JUnit 5.8.1](https://junit.org/junit5/docs/5.8.1/api/index.html).
