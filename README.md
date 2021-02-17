# Todo

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description

A gamified To-Do list app in which you take care of a virtual pet named Todo!

### App Evaluation
- **Category:** Productivity, Self-care, Lifestyle
- **Mobile:** Push notifications for task deadlines, camera capture for task verification
- **Story:** The app would have a clear value
- **Market:** Everyone can use this app to complete their tasks! It appeals to people of all ages, because everyone loves pets. It provides a support system for people who are seeking to improve their productivity.
- **Habit:** Users would login daily. They don't simply consume content but they interact with the app.
- **Scope:** This app would be moderately challenging to complete. A version with less integral features would still be interesting to build. It's pretty defined.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Create an account
* Login and logout
* Create and get reminders for a scheduled task in your to-do list
* Choose a pet + features
* See pet, pet status, pet features
* Add pre-defined tasks (cleaning, eating, exercise, waking, sleeping)?
* private task completion results in pet happiness increase
* public task completion results in pet happiness increase + points
* Buddy system approval of tasks + sending pictures
* Update pet status and task bars to match completed tasks

**Optional Nice-to-have Stories**

* Points system
    * Points are given for public posts
* Buying food with points
* Miscellaneous: ability to pet/interact with your pet
* Friends List

### 2. Screen Archetypes

* Login Screen
   * Create an account
   * Login and logout
* Pet Screen
   * Choose a pet + features
   * See pet, pet status, pet features
* Task Screen
    * See tasks to do
    * See past tasks?
    * Buddy system approval of tasks + sending pictures
* Buddy Approval Screen
    * Scroll through people's uploaded photos and approve/deny tasks

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* To-Do list tab
* Buddy system tab
* Pet! tab

**Flow Navigation** (Screen to Screen)

* Login
   * To-Do list preferences
* Pet 
   * Shop
* To-do List
    * Task Details
    * Pet

## Wireframes
<img src="https://github.com/codepath-FKS/codepath-project/blob/main/wireframe_sketch.png" width=600>


### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
### Models
**User**
| Property | Type | Description |
| -------- | -------- | -------- |
|  username  | string| The user's name |
| password |string| The user's password|
|points | int | The user's points|
| createdAt| DateTime | When the user was created |
| pet | pointer to [Pet] | The user's pet |
| todoList | pointer to [To-do List] | List of the user's tasks |

**To-do List**
| Property | Type | Description |
| -------- | -------- | -------- |
| list     | list of pointer to [Task Item] | list of pointers to task items 

**Task Item**
| Property | Type | Description |
| -------- | -------- | -------- |
| author     | pointer to [User]     | pointer to the user who created the task     |
| public    | bool     | whether the item is public or not and set for approval    |
| due date   | DateTime     | when a task should be completed by     |
| photo    | file    | photo uploaded to prove a task has been done     |
| description    | string   | a description of the task   |
| complete    | boolean    | if a task has been completed or not     |
|approved|bool|whether a task has been approved by another user or not

**Pet**
| Property | Type | Description |
| -------- | -------- | -------- |
|happiness  | float    | measures the "happiness" level of a pet |
|models  |List| a list of the different animations/models a pet will use to display the happiness meter|

**Store**

| Property | Type | Description |
| -------- | -------- | -------- |
| fancy food    | list of pointer to [Fancy Food]     | fancy food to buy with user points for the pet    |

**Fancy Food**


| Property | Type | Description |
| -------- | -------- | -------- |
| name    | String    | the name of the food     |
| image | file | image of the fancy food| 
| cost | int | the cost for the fancy food |
| effectiveness| int | how much happiness your pet will gain feeding this food|

### Networking
* Signup/Login Screen
    * (GET) [User] object
    * (Create/POST) create new [User] 
    
* Pet Screen
    * (Update/PUT) points when public tasks are approved by other users
    * (Read/GET) points
    * (Update/PUT) when points are used at the store
    
* Task Screen
    * (Create/POST) Create new [Task Item] object
    * (Read/GET) all tasks from the user
    * (Update/PUT) Specific tasks when edited
    * (Delete) Delete/remove a task
    
* Buddy Approval Screen
    * (Read/GET) Query all public tasks from other users that have not yet been approved
    * (Read/GET) Query pending tasks created by the user that have not yet been approved
    * (Update/PUT) tasks when approved/denied
