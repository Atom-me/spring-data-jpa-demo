# spring-data-jpa-demo

# TL;DR

```shell
helm upgrade --install sealed-secrets sealed-secrets \
  --namespace kube-system \
  --version 2.15.3 \
  --set-string fullnameOverride=sealed-secrets-controller \
  --repo https://bitnami-labs.github.io/sealed-secrets  
  
  
# monitor
kubectl rollout status deploy/sealed-secrets-controller -n kube-system

```

## 1. 创建 datasource-secret.yaml 文件

我们不会去手动apply创建这个secret。只是使用这个secret文件去创建另外一个加密的secret文件

> vim datasource-secret.yaml
>
> ```yaml
> apiVersion: v1
> kind: Secret
> metadata:
> name: spring-data-jpa-demo-datasource-credentials
> type: Opaque
> data:
> "spring.datasource.url": "amRiYzpteXNxbDovL215c3FsLnNxbHB1Yi5jb206MzMwNi9hdG9tX3Rlc3RfZGI=" # <- base64 encode "jdbc:mysql://mysql.sqlpub.com:3306/atom_test_db"
> "spring.datasource.username": "YXRvbV90ZXN0X2RiX3VzZQ==" # <- base64 encode "atom_test_db_use"
> "spring.datasource.password": "eTFqWDhlS0g3bjVQWDhFRA==" # <- base64 encode "y1jX8eKH7n5PX8ED"
> 
> 
> ```
>
>

## 2. 使用 kubeseal 工具加密这个对象：

```shell
$ kubeseal --controller-namespace=kube-system --format yaml < datasource-secret.yaml > sealed-datasource-secret.yaml

```

> cat sealed-datasource-secret.yaml
>
> ```yaml
> ---
> apiVersion: bitnami.com/v1alpha1
> kind: SealedSecret
> metadata:
> creationTimestamp: null
> name: spring-data-jpa-demo-datasource-credentials
> namespace: default
> spec:
> encryptedData:
>  spring.datasource.password: AgAJNPOGdpj3ZWntGwfXkXy5wTloVmMLVDnpHKC7nv4h0T6z35CIcN3DwB7ciBMlMNbdcgx+Hlb/hZHOKJgNiIKxf2xnzVQ/IMl7TN4GXbwjzweAdR2GBw3mnlul+I4czU+ArMjWuqHXWCuq0s9tO9QM6ZLwOP/RD/ylY5s55s0rB+RXMyNt35wGhLHXlMGa1TwwiXfzA8+6c5suFLmC7wPv8iT3ipHj9FHeshnt4dsUtJWu4ka7ULtWNU/CYuEx//AeN3ainvZCRh5EGGCK4JJp8+pBNI6AwjxMo6XRNY2hynIsocanjwyEctWLrJbWFAQypQ6emZ+RJI/x1DwysbI9ib0ZZIlZ4/8hufOQ2tH6BYf1bnDSPR9y/fHEVb7T2z95xB12T2yHARFSfDZToF6qRGX925r+OKNgsnL7OTOY9uYE1z6Pw51w+fVqp/4l7ZRBv5VpiRKYgfikJZHQGt2s22olCqYgonDjzPyZFeDxNVbZBJP/hbCAQ4H0gINLAhdkHz2fQzOOgEZyFvmu2X+9B0BMhjqL7vhHKHUPuu2OgfamDQFW9tzpE1RvYupfjxLHjzl8tFMp0vy1qyH+j4FyhddrwBvUkGOoEzGWSY8GbAh5E57v3aNZSZvuoH5JyjfV9D7jtX7u4g+JViubaDb249ghLfzfDDyILUZ7rkwjLFTxHtcsSBqISMiNc3uvtpkMGoTVlwR+pZYFJ41dBMvZ
>  spring.datasource.url: AgCwAkM5bY470LjunqxtPH5qTEcJGNE88DnoxY24y8jfsUmUog07H0RJmHbM1eWtrB+7np9oUbpe3klgCZnbsGHAc2upo0R6eWD5vJP5TSRsPk0cY/3I7KqMsZ/JMlNmCmqMF1190phi5Leu3EAnLpRTGo23JmK5UuYQI7wp162JMGqJZHfCnHYhO/4rIGgK3Vj432yAQ38B7fZ42y4Szp5kl1JD7K3437xS5oac2c13EMR391SIv6Kcd2FzNP/5Znwhs2yQHq6QufL7IEKmDqONrAcDemakSbRwaoAVlO8oWf4O7h+vyf3sBRVL9h7FIvoH+Atv1LWzdqv35p3FKyTkGGrBr8tYGzAbGoLEmGcy1mjoYjU7Vjx86gV79HWWrOSpM666pxKCGNxq34fkPBAApSxdrjN7BY8y+dvIMYj0RtiYB2rbcgf2NqjAYI/+M1qCGTDgPUhGS4qsSwl/BiYj016iflL3Zy+fll66DyzVlgucZWmcVGEYpyKCfdVvuk6Ms8MQnV4LqPYo2FuL/GsH6kmMOn189edmuweE2u7YpkmI7k24VYFdPNC48Uad+nuyuK9UbGhmbHx90PbqbaujIuPQT+LLd3wP4Ew66Lm2ogM2C5E9rfKQ2AgQ/eHESNXxD06dWNszNP6qPHrtVIkfznaRLEopvWgchdAQFLMSv3uKVtPFMwlIDbYrbGu9eVG26Z3TVPq1+HCnGX1uLNgI/X9pwvZ5iDMV5gIrM0bnlFAcDvT+7zh60Ju8pTTPZQ==
>  spring.datasource.username: AgAWria8T8EwtqxBjNcYnSx7edDWVKGezt32Q8gTa9zWJleqYwD+IpjU2/jCUWQky4xgNOlOP3IxsEj1S9czgFARQdWrBO74SSOkkTMdYDK8F8fEbLjX8eJgctuViXTyz3x7PVJMFRzaZTSaZ6tTZLMRyPNPzo9+PQdg3jYWSpgAd3xXpfWKZigAuHRREYoVS/rksSkP5YqfbpMOaZCy7IiWrBM48BzgzQeQZgrsijjp3WG5+Uu1x6XBlTDkQJRKIgjPG2zRP0BpuEfBpdi1rzDTeeLkBhQFzgt/inBUSGBj08BtLePmTZtnsf+VRCsPgUVu9bytJrWAdhbPQIEm6yIiqt/Y270MD5C06GSNdK5doD7keNObNnmW54qT3IuKTC+Uw8ICMIFo4A7nLKuESzzzzaET9Td2gBbMi3PaUfBCNlr55RBJG7gLf2GG43e3Ztp4xbVNaTAOVdZ8Xc5wkbfABWljfvq7wczPitZcuV9yo+AUfCq4XEmT+EThaRQdDtd3nBf8Woy/n1h+f9LJ33SxaRAhwHMURy5n0ih/psTZ3G4gdYmL1Qb4eOwAjbdk7DwLqjZU/2B3DbxSMagmN8t/XlUe3ryS4OjT8uL/L4QCizeG/ijiLewUeBAIkxK1InziqOQrJpJqm/WNlMVN5MrvwmoxamhwMYhTaiECHLMs8uqFJGqPLsKynTIZLWfO5ke+rg3e/gyfYaIpj/ShJ/4G
> template:
>  metadata:
>    creationTimestamp: null
>    name: spring-data-jpa-demo-datasource-credentials
>    namespace: default
>  type: Opaque
> 
> ```
>
>

可以看到 Secret 信息已经被加密了，现在我们就可以放心的将它存储在源码仓库中去了。

接下来我们在一个示例 Pod 中来使用下这个 Secret 对象，看能否正确获取到它的数据。

## 3. 直接创建上面生成的 SealedSecret 对象：

```shell
$ kubectl apply -f sealed-datasource-secret.yaml
```

创建完成后正常我们也可以在 default 的命名空间下面找到一个名为 `spring-data-jpa-demo-datasource-credentials` 的 Secret 对象：

```shell
$ kubectl get secret -n default
NAME                                          TYPE     DATA   AGE
spring-data-jpa-demo-datasource-credentials   Opaque   3      67s

```

## 4. 使用如下所示的资源清单来创建一个测试的 Pod：

```shell
$ kubectl apply -f - <<EOF
apiVersion: v1
kind: Pod
metadata:
  name: busybox
  namespace: default
  labels:
    app: busybox
spec:
  containers:
  - name: test
    image: busybox
    imagePullPolicy: IfNotPresent
    command:
      - sleep
      - "3600"
    volumeMounts:
    - name: mysecretvol
      mountPath: "/tmp/mysecret"
      readOnly: true
  volumes:
  - name: mysecretvol
    secret:
      secretName: spring-data-jpa-demo-datasource-credentials
EOF





$ kubectl get pods busybox -n default
NAME      READY   STATUS    RESTARTS   AGE
busybox   1/1     Running   0          19s
```

运行成功后我们可以查看容器中的密码数据来验证是否正确：

```bash
$ kubectl exec -it -n default busybox -- cat -n /tmp/mysecret/spring.datasource.url /tmp/mysecret/spring.datasource.username  /tmp/mysecret/spring.datasource.password
     1	jdbc:mysql://mysql.sqlpub.com:3306/atom_test_db
     2	atom_test_db_use
     3	y1jX8eKH7n5PX8ED
     
     
```

可以看到正确打印出了我们定义的用户名密码信息。

