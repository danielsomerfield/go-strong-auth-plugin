#!/usr/bin/python3

import xml.etree.ElementTree as ET
import time

config = ET.parse("/etc/go/cruise-config.xml")
server = config.getroot()[0]

if len(server.findall("security")) > 0:
    for country in server.findall("security"):
        server.remove(country)

    config.write("/etc/go/cruise-config.xml")
    time.sleep(5) #TODO: can we find a better way to do this?