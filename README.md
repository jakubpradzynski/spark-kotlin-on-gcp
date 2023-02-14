# Example Spark-Kotlin project with deployment on GCP

--- 

Repository is based on my other
repository: [Example Spark-Kotlin-Gradle project](https://github.com/jakubpradzynski/example-spark-kotlin-gradle-project)

## Deployment steps

1. Prepare new version of shadow jar:

```shell
./gradlew clean && ./gradlew shadowJar
```

2. Create new Google Cloud Storage bucket on which you can store JAR file & example data

```shell
gsutil mb gs://example-data
gsutil cp build/libs/spark-kotlin-on-gcp-1.0-SNAPSHOT-all.jar gs://example-data/
gsutil cp data/esa_air_pollution_measurements_poland.csv gs://example-data/
```

3. Create Dataproc cluster on which Apache Spark will run

```shell
gcloud dataproc clusters create my-cluster --region europe-west1 --image-version 2.1.2-debian11
```

4. Submit Apache Spark job to run on created cluster:

```shell
gcloud dataproc jobs submit spark --cluster my-cluster --region europe-west1 --jars gs://example-data/spark-kotlin-on-gcp-1.0-SNAPSHOT-all.jar --class pl.jakubpradzynski.measurements.MeasurementsPerCityCounterJobKt
```

5. Cleanup on GCP:

```shell
gcloud -q dataproc clusters delete my-cluster --region europe-west1
gsutil rm -r gs://example-data
```

## What's changed between repos?

On GCP I use Dataproc cluster (image `2.1.2-debian11`) with
the [newest available versions](https://cloud.google.com/dataproc/docs/concepts/versioning/dataproc-release-2.1) of
Apache Spark, Scala & Java but there are different from source repo.
I had to adjust versions in `build.gradle.kts`:

- JVM version 11 (`jvmToolchain(11)`)
- Apache Spark version 3.3.0 with Scala 2.12 (`compileOnly("org.apache.spark:spark-sql_2.12:3.3.0")`)
- Kotlin Spark API (`implementation("org.jetbrains.kotlinx.spark:kotlin-spark-api_3.3.0_2.12:1.2.3")`)

Also, dependency for [Spark BigQuery Connector](https://github.com/GoogleCloudDataproc/spark-bigquery-connector) has
been added: `implementation("com.google.cloud.spark:spark-bigquery_2.12:0.28.0")`.

In code, [CSV file](data/esa_air_pollution_measurements_poland.csv) is loaded from Google Cloud Storage bucket and also
result is stored in the same bucket.

## How to process test deploy?

If you want to process all steps in single run, you can use [deploy script](deploy/gcp.sh):

```shell
./deploy/gcp.sh
```

## Sources

- [Write and run Spark Scala jobs on Dataproc](https://cloud.google.com/dataproc/docs/tutorials/spark-scala)
- [Dataproc versioning](https://cloud.google.com/dataproc/docs/concepts/versioning/dataproc-release-2.1)
- [Spark BigQuery Connector](https://github.com/GoogleCloudDataproc/spark-bigquery-connector)
- [Example Spark-Kotlin-Gradle project](https://github.com/jakubpradzynski/example-spark-kotlin-gradle-project)
- [ESA Air pollution measurements data](https://dane.gov.pl/pl/dataset/2913,dane-pomiarowe-esa-edukacyjna-siec-antysmogowa)