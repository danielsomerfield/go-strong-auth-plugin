import getpass
import bcrypt


def generate_entry():
    username = raw_input("Username: ")
    password = getpass.getpass("Password: ")
    rounds = 12
    salt = bcrypt.gensalt(rounds)
    algorithm = "bcrypt"
    hash = bcrypt.hashpw(password, salt)

    return "{0}:{1}::{2}({3})".format(username, hash, algorithm, rounds)
