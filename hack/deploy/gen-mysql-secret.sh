#!/bin/bash
svc_list="assurance auth config consign-price consign contacts delivery food food-delivery inside-payment notification order-other order payment price route security station-food station ticket-office train-food train travel travel2 user voucher wait-order"

secret_yaml="deployment/kubernetes-manifests/quickstart-k8s/yamls/secret.yaml"
dp_sample_yaml="deployment/kubernetes-manifests/quickstart-k8s/yamls/deploy.yaml.sample"
sw_dp_sample_yaml="deployment/kubernetes-manifests/quickstart-k8s/yamls/sw_deploy.yaml.sample"

dp_yaml="deployment/kubernetes-manifests/quickstart-k8s/yamls/deploy.yaml"
sw_dp_yaml="deployment/kubernetes-manifests/quickstart-k8s/yamls/sw_deploy.yaml"


function gen_secret_for_tt {
  s="$1"
  name="ts-$s-mysql"
  hostVal="$2"
  userVal="$3"
  passVal="$4"
  dbVal="$5"

  prefix=`echo "${s}-mysql-" | tr '-' '_' | tr a-z A-Z`
  host=$prefix"HOST"
  port=$prefix"PORT"
  database=$prefix"DATABASE"
  user=$prefix"USER"
  pwd=$prefix"PASSWORD"

  cat>>$secret_yaml<<EOF
apiVersion: v1
kind: Secret
metadata:
  name: $name
type: Opaque
stringData:
  $host: "$hostVal"
  $port: "3306"
  $database: "$dbVal"
  $user: "$userVal"
  $pwd: "$passVal"
---
EOF
}

function gen_secret_for_services {
  echo "gen_secret_for_services: Started with $# arguments"

  mysqlUser="$1"
  mysqlPassword="$2"
  mysqlDatabase="$3"
  mysqlHost=""
  useOneHost=0

  if [ $# -eq 4 ]; then
    mysqlHost="$4"
    useOneHost=1
    echo "gen_secret_for_services: Received hostname - $mysqlHost"
  fi

  secret_yaml="path-to-your-secret-yaml-file.yaml"
  echo "gen_secret_for_services: Removing and touching secret yaml - $secret_yaml"
  rm $secret_yaml > /dev/null 2>&1
  touch $secret_yaml
  
  for s in $svc_list
  do
    echo "gen_secret_for_services: Processing service - $s"
    if [ $useOneHost -eq 0 ]; then
      mysqlHost="ts-$s-mysql-leader"
      echo "gen_secret_for_services: Generated mysql host - $mysqlHost"
    fi
    echo "gen_secret_for_services: Calling gen_secret_for_tt with service: $s, host: $mysqlHost"
    gen_secret_for_tt $s $mysqlHost $mysqlUser $mysqlPassword $mysqlDatabase
  done

  echo "gen_secret_for_services: Finished"
}

function update_tt_dp_cm {
  nacosCM="$1"
  rabbitmqCM="$2"

  cp $dp_sample_yaml $dp_yaml

  if [ "$(uname)"="Darwin" ]; then
    sed -i "" "s/nacos/${nacosCM}/g" $dp_yaml
    sed -i "" "s/rabbitmq/${rabbitmqCM}/g" $dp_yaml
  else
    sed -i "s/nacos/${nacosCM}/g" $dp_yaml
    sed -i "s/rabbitmq/${rabbitmqCM}/g" $dp_yaml
  fi
}

function update_tt_sw_dp_cm {
  nacosCM="$1"
  rabbitmqCM="$2"

  cp $sw_dp_sample_yaml $sw_dp_yaml
  if [ "$(uname)"="Darwin" ]; then
    sed -i "" "s/nacos/${nacosCM}/g" $sw_dp_yaml
    sed -i "" "s/rabbitmq/${rabbitmqCM}/g" $sw_dp_yaml
  else
    sed -i "s/nacos/${nacosCM}/g" $sw_dp_yaml
    sed -i "s/rabbitmq/${rabbitmqCM}/g" $sw_dp_yaml
  fi
}

