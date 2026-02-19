StaffSphere
===========

A minimal Java Swing HR app (login, dashboard, staff management).

Run (local)
-----------
1. Set DB credentials as environment variables:

```bash
export DB_URL=jdbc:mysql://localhost:3306/staffsphere
export DB_USER=root
export DB_PASSWORD=your_password
```

2. Compile and run from project root:

```bash
mkdir -p out
javac -cp "jbcrypt-0.4.jar:mysql-connector-j-9.3.0.jar" -d out $(find src -name "*.java")
java -cp "out:jbcrypt-0.4.jar:mysql-connector-j-9.3.0.jar" staffsphere.main.Main
```

3. Alternatively create `config.properties` from `config.properties.example`.

Notes
-----
- The project expects `users` and `employees` tables (see code for column names).

Database schema
---------------
Create these two tables in your `staffsphere` database 

```sql
CREATE TABLE users (
   user_id INT AUTO_INCREMENT PRIMARY KEY,
   username VARCHAR(100) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   role VARCHAR(20) NOT NULL,
   status VARCHAR(20) NOT NULL DEFAULT 'active'
);

CREATE TABLE employees (
   emp_id INT AUTO_INCREMENT PRIMARY KEY,
   emp_code VARCHAR(100) NOT NULL UNIQUE,
   name VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   phone VARCHAR(50),
   department VARCHAR(100),
   designation VARCHAR(100),
   salary DOUBLE,
   status VARCHAR(20) NOT NULL DEFAULT 'active',
   created_by INT,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP
);
```

Notes: adjust types, indexes and constraints as needed for your environment.