## Database

This project uses **MongoDB Atlas** as the cloud database to store application data securely.

### Database Collections

The application stores data in the following collections:

- **users** – Stores user account information such as name, email, password (hashed), phone number, district, and role.
- **chats** – Stores user questions, AI-generated responses, and chat history.
- **images** – Stores metadata of uploaded crop images, including filename, file path, upload time, and associated user.

### Database Configuration

Configure the MongoDB connection string in `application.properties`:

```properties
spring.data.mongodb.uri=your_mongodb_connection_string
spring.data.mongodb.database=farm_advisory
```

### Features

- Cloud-based MongoDB Atlas database
- Automatic collection creation
- Spring Data MongoDB integration
- Secure password storage using BCrypt hashing
- Efficient CRUD operations through repository interfaces