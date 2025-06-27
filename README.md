<h1>Docker</h1>

build the docker image

<code>
docker build . -t hnitsan/devteam
</code>

run the container

<code>
docker run -d -p 8080:8080 hnitsan/devteam
</code>