import getpass
import string
import random
import hashlib
import binascii


def generate_hash(password, salt, iterations, keysize):
    return binascii.b2a_hex(hashlib.pbkdf2_hmac("sha1", password, salt, iterations, keysize / 8))


def generate_entry():
    username = raw_input("Username: ")
    password = getpass.getpass("Password: ")
    salt = ''.join(random.SystemRandom().choice(string.ascii_uppercase + string.digits) for _ in range(20))
    algorithm = "PBKDF2WithHmacSHA1"
    iterations = 10000
    keysize = 256
    hash = generate_hash(password, salt, iterations, keysize)

    return "{0}:{1}:{2}:{3}({4},{5})".format(username, hash, salt, algorithm, iterations, keysize)
