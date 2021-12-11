# RecipeReady

RecipeReady is a web app that helps you with both storage management and recipe recommendation.<br />
It tracks your ingredients in the fridge and recommends recipes based on ingredients you already have.
<img src="https://github.com/qianhuiwei/RecipeReady/blob/master/pageDemo.png" width="900"/>

## Motivation
This app is designed to: 
* Prevent food from expiring.
* Relieve people from searching recipes by ingredients manually.
* Save time for shopping missing ingredients.
                                                                                                                                                                            
## Features
#### Frontend
* Used HTML, CSS, Javascript, AJAX to set up a responsive and interactive SPA.
* Applied AWS Rekognition to allow people add ingredients from uploaded photos.
* Improved UX by marking missing ingredients in recipes.
* Imported icons from [Google Icon Font Awesome](https://fontawesome.com/v5.15/icons/google).

#### Backend
* Implemented an Apache Tomcat server to handle tasks (including user registration/ login, database communication, recipe collection from external APIs, and recipe recommendation algorithms).
* Implemented a cloud database by using AWS MySQL to store data (inclusing user profile, all recipes, ingredients in user's fridge, and user's favorite recipes).
* Deployed the server to AWS EC2 using Docker.
