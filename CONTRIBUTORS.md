# ManaVault

## Overview
This project was designed to be a fullstack crud application storing a magic the gathering card collection using ReactJs for the frontend, Java SpringBoot for the backend, and MySQL for the storage.

## How to Contribute
If you are interested in contributing, first thank you. I appreciate all of the help I can get on this project. Second,
make sure when making changes you make a fork of this branch off of the mtg-epic branch.

## Technology Used
*   IntelliJ for Java Development
*   DBeaver for SQL development
*   VsCode for frontend development
*   Make for running makefiles
*   Ollama for the communicating to models
*   MySQL for hosting database server

## How to run project

### Pre-Project Setup
* Ensure you have environment variables setup for both test and prod database connections.
* Ensure MySQL Server is running in background

If you do not have make, don't worry. Refer to the makefiles for the necessary commands for running various commands.

Below I will be walking you through 

Start Frontend dev server
```sh
make dev
# Without make
npm run dev
```

To start backend server:
```
1.) Navigate to the server directory
2.) Go to the App.java file
3.) Run start
```

## Commit changes
When making a change ensure you have had made a fork off of the mtg-epic branch. Your flow should look like this where your branch name is dependent on mtg-epic and the epic branch is dependent on main:
```
main
    - mtg-epic
        -mtg-your-branch-name
```

## Q&A
If you have any questions please reach out to shaffemarcus@gmail.com or add a comment in an issue. Happy Coding!