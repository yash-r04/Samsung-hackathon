from models import db, User
from app import app

with app.app_context():
    # Create all the tables
    db.create_all()
    
    # Check if the admin user already exists
    admin = User.query.filter_by(username='admin').first()
    
    if admin is None:
        # If admin user does not exist, create it
        admin = User(username='admin', password='adminpass', is_admin=True)
        db.session.add(admin)
        db.session.commit()
        print("Admin user created.")
    else:
        print("Admin user already exists.")
