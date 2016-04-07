import getpass
import bcrypt


def generate_entry():
    username = raw_input("Username: ")
    password = getpass.getpass("Password: ")
    rounds = 12
    salt = bcrypt.gensalt(prefix=b"2a")
    algorithm = "bcrypt"
    hash = bcrypt.hashpw(password, salt)

    return "{0}:{1}::{2}".format(username, hash, algorithm)
