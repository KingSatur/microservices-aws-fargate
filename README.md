# Build images

- Users image
    ```
    docker build . -t users -f ./users/Dockerfile
    ```
- Courses image
    ```
    docker build . -t courses ./courses/Dockerfile

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
docker run --rm -d -p 3350:3306 --name mysql8 --network microservices-aws -e MYSQL_ROOT_PASSWORD=customPassword -e MYSQL_DATABASE=users mysql:8
```

```
docker run --rm -d -p 5490:5432 --name pg --network microservices-aws -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=customPass -e POSTGRES_DB=courses postgres:14
```

# Run database with volumes

```
docker run -d -p 3350:3306 --name mysql8 --network microservices-aws -e MYSQL_ROOT_PASSWORD=customPassword -e MYSQL_DATABASE=users -v data-mysql:/var/lib/mysql --restart=always mysql:8

```

```
docker run -d -p 5490:5432 --name pg --network microservices-aws -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=customPass -e POSTGRES_DB=courses -v data-pg:/var/lib/postgresql/data --restart=always postgres:14
```


# Run services specifying the port dynamically

```
 docker run -p 8002:8002 -e PORT=8002 -d --rm --name courses-service --network microservices-aws jdlearner/courses

```
```
 docker run -p 8001:8001 -e PORT=8001 -d --rm --name users-service --network microservices-aws jdlearner/users
```
