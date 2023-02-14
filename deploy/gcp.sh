#!/usr/bin/env bash

echo -e "\033[1m\n ----- Starting deploy on GCP... ----- \n\033[0m"

echo -e "\033[1m\n ----- Cleaning old gradle builds... ----- \n\033[0m"
./gradlew clean

echo -e "\033[1m\n ----- Building new fat jar... ----- \n\033[0m"
./gradlew shadowJar

echo -e "\033[1m\n ----- Creating new bucket in Google Cloud Storage... ----- \n\033[0m"
gsutil mb gs://example-data

echo -e "\033[1m\n ----- Copying fat jar into bucket... ----- \n\033[0m"
gsutil cp build/libs/spark-kotlin-on-gcp-1.0-SNAPSHOT-all.jar gs://example-data/

echo -e "\033[1m\n ----- Copying example data into bucket... ----- \n\033[0m"
gsutil cp data/esa_air_pollution_measurements_poland.csv gs://example-data/

echo -e "\033[1m\n ----- Starting new Dataproc cluster for Apache Spark... ----- \n\033[0m"
gcloud dataproc clusters create my-cluster --region europe-west1 --image-version 2.1.2-debian11

echo -e "\033[1m\n ----- Submitting Apache Spark job on created Dataproc cluster... ----- \n\033[0m"
gcloud dataproc jobs submit spark --cluster my-cluster --region europe-west1 --jars gs://example-data/spark-kotlin-on-gcp-1.0-SNAPSHOT-all.jar --class pl.jakubpradzynski.measurements.MeasurementsPerCityCounterJobKt

echo -e "\033[1m\n ----- Deleting Dataproc cluster... ----- \n\033[0m"
gcloud -q dataproc clusters delete my-cluster --region europe-west1

echo -e "\033[1m\n ----- Deleting GCS bucket... ----- \n\033[0m"
gsutil rm -r gs://example-data

echo -e "\033[1m\n ----- The end ----- \n\033[0m"
