docker run -v `pwd`:/usr/local/maven -v `pwd`/m2cache:/root/.m2 -it registry.manadrdev.com/maven
docker build -t registry.manadrdev.com/attendances-reporting .
docker push registry.manadrdev.com/attendances-reporting
ssh manadr_ci "cd /datadrive/attendances/ && /datadrive/attendances/run.sh"