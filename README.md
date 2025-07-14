# Map App (Spring × Docker × PostgreSQL)

## 💡 このアプリについて
地図にピンを立てて、吹き出しコメントや画像を添付できるパンフレット作成アプリです。

## 🔧 起動方法

```bash
docker-compose up --build

## 🔐 環境変数（APIキー）の設定

このリポジトリでは `.env` ファイルにAPIキーを記述して管理しています。
公開されていないため、以下の手順で `.env` を自分で用意してください。

```bash
cp .env.example .env
