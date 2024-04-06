### spring-mongo
#### Simple reactive app using Mongo DB

Run:
1. /docker/docker compose up
2. gradlew bootrun
3. by default running on http://localhost:8080

#### API:

Users:
+ GET /api/user - get all users. 
+ GET /api/user/{id} - get user by id.
+ POST /api/user - create, body: username, email
+ PUT /api/user/{id} - update, body: username, email
+ DELETE /api/user/{id} - delete

Tasks:
+ GET /api/task, 
+ GET /api/task/{id}
+ POST /api/task, body: name, description, status, authorId, assigneeId, observerIds - set of ids
+ PUT /api/task/{id}, body: name, description, status, authorId, assigneeId, observerIds - set of ids
+ DELETE /api/task/{id}
+ POST api/task/add-observer/{task id}, param observerId - id of added observer 

status values of (TODO, IN_PROGRESS, DONE)