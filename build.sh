docker run -v `pwd`:/usr/local/maven -v `pwd`/m2cache:/root/.m2 -it registry.manadrdev.com/maven
docker build -t registry.manadrdev.com/attendances-reporting .
docker run -p 8080:8080 -it registry.manadrdev.com/attendances-reporting