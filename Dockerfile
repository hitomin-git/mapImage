# ベースイメージ（Tomcat公式）
FROM tomcat:9.0-jdk17-openjdk

# WARファイルをビルドして配置する（ローカルの target ディレクトリからコピー）
COPY target/map-app.war /usr/local/tomcat/webapps/

# ポート開放（Tomcatデフォルト）
EXPOSE 8080

CMD ["catalina.sh", "run"]


