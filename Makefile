build:
	docker run -v `pwd`:/usr/local/maven -v `pwd`/m2cache:/root/.m2 -it registry.manadrdev.com/maven
	docker build -t registry.manadrdev.com/attendances-reporting .

run:
	docker-compose up
	
deploy:	build
	docker push registry.manadrdev.com/attendances-reporting
	kubectl delete pod -l service=attendance --namespace=attendance