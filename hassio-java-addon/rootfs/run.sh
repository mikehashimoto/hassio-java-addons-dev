#!/command/with-contenv bashio

cd simple

export ACCESS_TOKEN=$(bashio::config 'access_token')

./gradlew bootRun