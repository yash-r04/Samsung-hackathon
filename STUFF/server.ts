import express from 'express';
import mysql from 'mysql2';
import bodyParser from 'body-parser';

const app = express();
const port = 3000; // Port for Node.js server

// MySQL connection setup
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'db', // Your database name
  port: 3306 // MySQL port
});

db.connect((err: mysql.QueryError) => {
  if (err) {
    console.error('Database connection failed:', err);
    return;
  }
  console.log('Connected to MySQL database.');
});

app.use(bodyParser.json());

app.post('/register', (req, res) => {
  const { username, email, aadhar } = req.body;
  const query = 'INSERT INTO user (username, email, aadhar) VALUES (?, ?, ?)'; // Your table name

  db.query(query, [username, email, aadhar], (err: mysql.QueryError, result: any) => {
    if (err) {
      console.error('Error inserting data:', err);
      res.status(500).json({ error: 'Database error' });
      return;
    }
    res.status(200).json({ message: 'User added successfully', result });
  });
});

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
