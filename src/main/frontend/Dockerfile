FROM node:18-alpine as frontend

WORKDIR /frontend

COPY package.json package-lock.json ./
RUN npm install
COPY . ./
RUN npm run build

