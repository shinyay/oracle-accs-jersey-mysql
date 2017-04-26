# RESTful Web Service connected to MySQL Sample on Oracle Application Container Cloud Service

![ACCS with MySQLCS](https://raw.githubusercontent.com/shinyay/oracle-accs-jersey-mysql/develop/docs/images/accs_with_mysql.png)

Oracle Application Container Cloud Service は、その他の Oracle Cloud で提供しているサービスを外部サービス (BaaS)　として **サービス・バインディング** して使用する事が可能です。

このサンプルアプリケーションでは、MySQL Cloud Service を外部データベース・サービスとしてバインドして利用してみます。


## 説明
[Oracle MySQL Cloud Service Create Shell](https://github.com/shinyay/mysqlcs-create-shell) では、MySQL Cloud Service にデータベース・サーバを簡単に作成するシェルを用意しました。

これにより作成した MySQL Server に対して、Application
 Container Cloud Service 上に RESTful Web サービスを作成し、MySQL の情報を JSON で返すシンプルなアプリケーションを作成してみます。

 このアプリケーションを次のコンポーネントを利用して構成します:

- **JAX-RS**: Jersey
- **JPA**: Hibernate

![jersey_and_jpa](https://raw.githubusercontent.com/shinyay/oracle-accs-jersey-mysql/develop/docs/images/jersey_and_jpa.png)

### サービスバインディング
**サービスバインディング** は、Application Contariner Cloud Service 以外のクラウドサービスをアタッチして **リソース** として利用するための仕組みです。

**Twelve-Factor App** で唱えられている **[Backing services](https://12factor.net/backing-services)** を実現する仕組みと言えます。

リソースとしてアタッチされた MySQL Cloud Service を Application Copntainr Cloud Service から利用するためには、バインド時に設定されている環境変数から情報を取得して利用します。

これもまた同様に **Twelve-Factor App** で唱えられている **[Config](https://12factor.net/config)** を実現するアプローチになります。

## 動作イメージ

以下は、Application Container Cloud Service 上にデプロイしたアプリケーションに GET リクエストを行い、MySQL に格納されているデータが JSON フォーマットで取得できている動作イメージです:

![Demo](https://raw.githubusercontent.com/shinyay/oracle-accs-jersey-mysql/develop/docs/images/accs_with_mysql_demo.gif)

データ内容がポケモン情報なのはご愛嬌です^^

## 機能/実行方法

機能というまでもないのですが、RESTアプリケーションで実装しているサブリソースは以下の２つです。

- /all
  - 全件取得
    - `GET http(s)://<DOMAIN>:<PORT>/myapp/pokemon/all`
- {id}
  - IDによるデータ検索
    - `GET http(s)://<DOMAIN>:<PORT>/myapp/pokemon/{id}`

## 前提

以下の依存ライブラリを使用しています:

- jersey-container-grizzly2-http
  - version: 2.26-b02
- jersey-media-json-jackson
  - version: 2.26-b02
- hibernate-core
  - version: 5.2.9.Final
- hibernate-validator
  - version: 5.4.1.Final
- hibernate-java8
  - version: 25.2.9.Final
- mysql-connector-java
  - version: 26.0.6


## 実装ポイント

サービスバインディングの項目で紹介しましたが、MySQL Cloud Service を外部リソースとして使用します。そのために使用する情報は **環境変数** から取得します。

以下の環境変数を取得し、ソースコード内で動的にバインドするように実装を行います:

|環境変数|説明|
|---|---|
|HOSTNAME|ホスト名|
|PORT|HTTPサーバがバインドするポート番号|
|MYSQLCS_CONNECT_STRING|MySQL に接続するアクセスURI<br>IPアドレス + ポート番号|
|MYSQLCS_USER_NAME|MySQL アクセスユーザ名|
|MYSQLCS_USER_PASSWORD)|MySQL アクセスパスワード|

これらの環境変数は以下のようにロードして使用します:

- HTTP Server (Grizzly)

```java
static{
    PROTOCOL = "http://";
    HOST = Optional.ofNullable(System.getenv("HOSTNAME"));
    PORT = Optional.ofNullable(System.getenv("PORT"));
    PATH = "myapp";
    BASE_URI = PROTOCOL
            + HOST.orElseGet(() -> "localhost")
            + ":"
            + PORT.orElseGet(() -> "8080")
            + "/"
            + PATH
            + "/";
}
```

- MySQL

```java
static {
    JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    JDBC_URL = "jdbc:mysql://" + System.getenv("MYSQLCS_CONNECT_STRING");
    JDBC_USER = System.getenv("MYSQLCS_USER_NAME");
    JDBC_PASSWORD = System.getenv("MYSQLCS_USER_PASSWORD");
    if (JDBC_USER == null) {
        JDBC_ENV_FLG = false;
    } else {
        JDBC_ENV_FLG = true;
    }
}
```

## Licence

Released under the [MIT license](https://gist.githubusercontent.com/shinyay/56e54ee4c0e22db8211e05e70a63247e/raw/44f0f4de510b4f2b918fad3c91e0845104092bff/LICENSE)

## Author

[shinyay](https://github.com/shinyay)
