# Team 11

## Project Description
To aid University of Waterloo co-op students in their co-op job search, we provide this application where students can submit information and ratings for their past jobs for all to see, as well as connect with other students to expand their network. 

[Link to Wiki](https://git.uwaterloo.ca/e2roth/Team-11/-/wikis/Project-Proposal)

## Essential Product Features
Android app

1. User accounts (login)
2. User's profile page
3. Other user's profile pages
4. Job rating pages
5. Create and submit job ratings
6. Keyword search
7. Search filtering
8. Comment function
9. DMs
10. Networking event function

## Team Members
 - Ethan Roth, e2roth@uwaterloo.ca
 - Lance Xu,   shizheng.xu@uwaterloo.ca
 - Rai Dai,    r24dai@uwaterloo.ca
 - Qin Liu,    q285liu@uwaterloo.ca

 ## API
Supports GET requests from an Azure database.
Request return the contents of the table.

 - GET http://127.0.0.1:8080/benefit
 - GET http://127.0.0.1:8080/company
 - GET http://127.0.0.1:8080/prog
 - GET http://127.0.0.1:8080/report
 - GET http://127.0.0.1:8080/reportBenefit
 - GET http://127.0.0.1:8080/reportInfo
 - GET http://127.0.0.1:8080/studentPersonalInfo

The API project should be built and run from the KtorAPI directory.
Since the API is hosted locally, a Azure blocks unregistered IP adresses. 
Contact e2roth@uwaterloo.ca to register your IP address.
