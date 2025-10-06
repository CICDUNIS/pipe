FROM node:18 AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

# Copy the rest of the source code and build for production
COPY . .
RUN npm run build

FROM nginx:alpine

COPY --from=build /app/dist/<your-angular-project-name> /usr/share/nginx/html

EXPOSE 80
