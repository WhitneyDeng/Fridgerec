Original App Design Project - README
===

# Fridgerec

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Allows users to log and track food in their pantry. Notifies user of upcoming expiration dates. Suggests recepies based on available ingredients (ranked based on how many soon to be expired ingredients is used). Compiles shopping list for user.

Nice to have: Convenient grocery/food logging with UPC (optional: barcode scanner with camera). Calendar view of when food is expired. Automatically decrements amount of grocery used in a recipe. Shows stores where certain food products are in stock. Allows users to plan meals ahead of time. Track nutritional intake.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Lifestyle (Food)
- **Mobile:** Mobile is essential for on-the-go logging of food & consulting food stores at home when user goes grocery shopping in person
- **Story:** Reduce food waste & redundant shopping by tracking stocked food
- **Market:** Anyone who cooks or stores food at home
- **Habit:** Users use this during and after grocery-shopping, before and during meal preparation
- **Scope:** <tbd> Not sure what scope means, the [app eval guide](https://courses.codepath.com/courses/metau_android/unit/1#heading-evaluating-app-ideas-protocol) is blank atm

## Product Spec

### 1. User Stories (Required and Optional)

#### **Required Must-have Stories**

* Uses [Back4App API](https://dashboard.back4app.com/apps) for data persistence
> [Back4App or should I look into SQL?]
* Uses [Spoonacular API](https://spoonacular.com/food-api)
* User can login
* User can create a new account
* User can record grocery inventory
  * User can log grocery
    * Toast when grocery successfully logged
    * user chooses expiration or bought on date as priority metric
  * User can delete grocery
  * User can edit grocery
  * User can view a list of stocked grocery
    * User can view name, expiration date, amount left
  * [technical problem] Filtering: Sort grocery stock list by:
    * expire date: expiring within # days, expired
    * source date: bought ≥# days ago
    * based on main freshness metric (set per item)
    * none
    * food groups
      * RSC: [sectioning RecyclerView](https://www.youtube.com/watch?v=q8WumgNQqrU)
>     if no sourcedate or expire date, put them last
* User can compile shopping list
  * User can add shopping item
  * User can delete (tick) shopping item
  * User can edit shopping item
  * User can check off shopping item [checked box in recycler view](https://blog.oziomaogbe.com/2017/10/18/android-handling-checkbox-state-in-recycler-views.html)
  * checked shopping list item transfer to inventory
* [technical problem] push notification for when grocery expire
* ≥1 gesture: pull to refresh; swipe to delete [ref1](https://www.journaldev.com/23164/android-recyclerview-swipe-to-delete-undo#:~:text=Android%20Swipe%20To%20Delete,use%20the%20ItemTouchHelper%20utility%20class.) [ref2](https://www.geeksforgeeks.org/swipe-to-delete-and-undo-in-android-recyclerview/)
* ≥1 external library to add visual polish
  * Material Design: floating button
* ≥1 animation: animate transition bn Create & Stream

#### **Optional Nice-to-have Stories**

##### Priority Stretches
* autocomplete search when adding item (Creation views) [src](http://ramannanda.blogspot.com/2014/10/android-searchview-integration-with.html)
* Convenient grocery logging with UPC
  * optional: barcode scanning [Google ML Barcode](https://developers.google.com/ml-kit/vision/barcode-scanning/android#java)
* Settings page
  * notify user of food expiring in X days
  * when to deliver push notification for when food is expiring
* same grocery item, diff batches/products
* add button allow user to choose between adding custom product or search ingredient from database

##### Add ons
* modal overlay (are you sure) when navigate away from Creation item with filled components
* settings: if edit text changed => modal overlay (are you sure)
* Group grocery stock list by food categories
* show amount of grocery in inventory in Shopping List Creation view
  * expand back4App & ParseObject models
  * add field to Creation views
* Make Creation tabs Modal Overlays
* User can choose to log current grocery & keep logging OR exit logging
* add custom item to grocery inventory list
  * leftovers or e.g. custommade sauces
* sort and filter in dialog fragment instead
* not let source date exceed expire date
* allow user to filter multiple food groups (programmatically add chips)
* remember prev sort & filter choices + check previously selected sort/filter preferences

##### Optional Stretches
* Calendar view of when grocery expires
* User can search for recipe based on ingredients
  * User can view details of recipe: photo, name, ingredients, instructions
  * User can favourite recipe
    * User can view list of favourite recipe
  * View history of used
    * User can mark used recipe
    * date used => show last cooked on recipe Detail view
    * rating
    * comments => show last cooked on recipe Detail view
  * more details to recipe Detail view:
    * nutrtional info
  * User can compile shopping list based on recipe & stocked grocery
    * backend: shopping list items associated with particular recipe
    * User can remove/adjust amount all shopping list items associated with particular recipe
  * User can create own recipes
    * User can search for ingredients from Spoonacular Ingredient database
  * Automatically decrements amount of grocery used mark recipe as choosed
  * adjust serving size & amount of ingred needed in recipe
* Allow users to plan meals ahead of time (mark recipe to use in future)
* Allow users to keep food diary
  * Log meals outside of recipes chosen
  * Log meals from restaurant [ref: [Spoonacular API Search Menu Items](https://spoonacular.com/food-api/docs#Search-Menu-Items)]
  * Track nutritional intake of the day
* Show stores where shopping list items are in stock

#### Potential Technical Challenges
* ???recyclerview of recipe detail
  * litho: UI
* push notification
* shopping list: when checked, item gets moved to separate list
* barcode scanner
* ???maintaining consistency with local broadcasting (instead of refresh) [Live Data](https://developer.android.com/reference/androidx/lifecycle/LiveData)
* calendar view
* keep local cache of queries: consistency bn activities
  * when update, need update cache & database
* ticked shopping item list automatically added to inventory

### 2. Screen Archetypes

* Login Screen
  * User can login
* Registration Screen
  * User can create a new account
* Stream1: Grocery Inventory List
  * User can view a list of stocked grocery
  * User can view name, expiration date, amount left
* Stream2: Shopping List
  * User can view shopping list based on recipe & stocked grocery
* Creation1: Grocery Inventory
  * User can log grocery
  * User can edit grocery
    > [???should be in Creation or Detail]
  * User can delete grocery
    > [???should be in Creation or Stream]
* Creation2: Shopping Item
  * User can add shopping item
* Detail
  * User can view details of recipe
* Search
  * User can search for recipe based on ingredients
  * User can mark used recipe


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Grocery Inventory
* Recipe Search
* Shopping List

**Flow Navigation** (Screen to Screen)

* Login Screen
  * => Stream
* Registration Screen
  * => Stream
* Stream1: Grocery Stock List
  * => Creation1
* Stream2: Shopping List
  * => Creation2
* Creation1: Grocery Stock
  * => N/A
* Creation2: Shopping Item
  * => N/A
* Detail
  * => N/A
* Search
  * => Detail

## Wireframes
![](https://i.imgur.com/dOuTC1O.jpg)


### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema
### Models

Food
| Property | Type     | Description                         |
| -------- | -------- | ----------------------------------- |
| objectId | String   | unique id for food               |
| foodCategory   | String          | food type                |
| itemName           |    String             |    name of food                                 |
| apiId  | String          | id of food in Spoonacular API |
| image | File | photo of food |


User
| Property  | Type     | Description                |
| --------- | -------- | -------------------------- |
| objectId  | String   | unique id for user         |
| username  | String   | username of account        |
| password  | String   | password of account        |
| createdAt | DateTime | date when user was created |
| notificationTime             | Number | when to send inventory reminder push notification ( /10 to get hour (in 24 hour format), %10 to get minute) |
| notificationSourceDateOffset | Number | push notification # days after bought date                                                                  |
| notificationExpireDateOffset | Number | push notification # days before expiration date                                                             |

EntryItem

| Property             | Type            | Description                                                                |
| -------------------- | --------------- | -------------------------------------------------------------------------- |
| updatedAt                     |  DateTime               |   when entry was last updated                                                                         |
| user                 | Pointer to User | user that has item                                                         |
| amount               | Number          | amount of item                                                             |
| amountUnit           | String          | unit of amount                                                             |
| expireDate           | DateTime        | expiration date of grocery                                                 |
| sourceDate           | DateTime        | date food was sourced (bought, made etc) on                                |
| mainFreshnessMetric | Boolean         | "EXPIRE_DATE" or "SOURCE_DATE", default: "NULL"|
| containerList        | String          | "INVENTORY" or "SHOPPING"                                                  |
| food                 | Pointer to Food | item bought                                                                |


> RecipeRecord
> | Property         | Type              | Description                   |
> | ---------------- | ----------------- | ----------------------------- |
> | objectId         | String            | unique id for user            |
> | lastUsedAt       | DateTime          | date recipe last used by user |
> | recipe           | Pointer to Recipe | recipe used by user           |
> | favouritedByUser | Bool              | if recipe favourited by user                             |
> | usedBy     | Pointer to User   | user that has item            |


> Recipe
> | Property | Type   | Description                     |
> | -------- | ------ | ------------------------------- |
> | summary  | String | recipe summary of cake          |
> | apiId    | Number | id of recipe in Spoonacular API |
> | objectId | String | unique id for recipe            |
> > | image    | File   | photo for recipe                |
> >     necessary? or just store recipe id?

### Networking

* Login Screen
  * loginInBackground()
* Registration Screen
  * (Create/POST) new user
* Stream1: Grocery Inventory List
  * (Read/GET) Query "InventoryList" where user is currentUser
  * (Delete) Delete existing "InventoryList" item
* Stream2: Shopping List
  * (Read/GET) Query "ShoppingList" where user is currentUser
  * (Delete) Delete existing "ShoppingList" item
  * (Update/PUT) Update (checked) a "ShoppingList" item
* Creation1: Grocery Inventory
  * (Create/POST) Create a new "InventoryList" item
  * (Update/PUT) Update a "InventoryList" item
* Creation2: Shopping Item
  * (Create/POST) Create a new "ShoppingList" item
  * (Update/PUT) Update a "ShoppingList" item
* Detail
  * (Read/GET) Query a "Recipe" iten
  * (Create/POST) Create a new "Recipe" item
  * (Update/PUT) Update a "Recipe" item (comments)
  * (Delete) Delete (unfavourite & not used) existing "Recipe" item
  * (Create/POST) Create a new "ShoppingList" item
* Search
  * (Read/GET) Query "InventoryList" where user is currentUser


### API Endpoints

* Login Screen
  * N/A
* Registration Screen
  * N/A
* Stream1: Grocery Inventory List
* Stream2: Shopping List
* Creation1: Grocery Inventory
* Creation2: Shopping Item
* Detail
* Search
  * [When recipe marked as used, Convert measuring unit to decrement amount in inventory](https://spoonacular.com/food-api/docs#Convert-Amounts)