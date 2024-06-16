# Use an official Node runtime as the base image
FROM node:18.13.0

# Set the working directory in the Docker image
WORKDIR /usr/src/app

# Install serve to serve the built app
RUN npm install -g serve

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install project dependencies
RUN npm install

# Copy the rest of the project files to the working directory
COPY . .

# Build the app
RUN npm run build

# Expose port 5000 for the app to be accessible
EXPOSE 3000

# Define the command to run the app
CMD ["serve", "-s", "dist", "-l", "3000"]