# Build images

- Users image
    ```
    docker build . -t users
    ```
- Courses image
    ```
    docker build . -t courses

    ```

# Create network

```
 docker network create microservices-aws
```

```
 docker network ls
```

# Run images within the same network


```
 docker run -p 8002:8002 -d --rm --name courses-service --network microservices-aws jdlearner/courses
```

```
 docker run -p 8001:8001 -d --rm --name users-service --network microservices-aws jdlearner/users
```

```
docker run -d -p 3350:3306 --name mysql8 --network microservices-aws -e MYSQL_ROOT_PASSWORD=customPassword -e MYSQL_DATABASE=users mysql:8
```

```
docker run --rm -d -p 5490:5432 --name pg --network microservices-aws -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=customPass -e POSTGRES_DB=courses postgres:14
```