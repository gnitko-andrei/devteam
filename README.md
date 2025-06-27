<h1>Run as Java Application</h1>
Preconditions:

- local MySql database setup

Run `DevteamApplication.java`

<h1>Run Standalone App Container with local MySql DB</h1>

Preconditions:

- local MySql database setup
  <br>

Build the docker image

`docker build . -t devteam-app`

Run the container

`docker run -d -p 8080:8080 devteam-app`

<h1>Run App with Docker Compose</h1>

`docker compose up -d`

