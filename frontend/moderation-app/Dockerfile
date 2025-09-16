FROM node:22.18-alpine
LABEL authors="Ivan"
WORKDIR /app

# Installing dependencies
COPY package*.json .
RUN npm install

# Copy source code
COPY . .

EXPOSE 3000
CMD ["npm", "run", "dev"]