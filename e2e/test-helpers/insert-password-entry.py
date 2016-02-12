#!/usr/bin/python3

import sys

if len(sys.argv) < 1:
    raise Exception("An entry string is required")

with open("/etc/go/passwd", "w") as passwordFile:
    passwordFile.write(sys.argv[1])