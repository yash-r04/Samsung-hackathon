const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');

const app = express();
const port = 3000;

// MySQL connection setup
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'Suhail@786',
  database: 'db' // Updated to your database name
});

db.connect(err => {
  if (err) {
    console.error('Database connection failed:', err);
    return;
  }
  console.log('Connected to MySQL database.');
});

app.use(bodyParser.json());

app.post('/register', (req, res) => {
  const { username, email, aadhar } = req.body;
  const query = 'INSERT INTO user (username, email, aadhar) VALUES (${username}, ?, ?)'; // Updated to your table name

  db.query(query, [username, email, aadhar], (err, result) => {
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
