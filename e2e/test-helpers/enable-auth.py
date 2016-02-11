#!/usr/bin/python3

import xml.etree.ElementTree as ET
import time

config = ET.parse("/etc/go/cruise-config.xml")
server = config.getroot()[0]
if len(server.findall("security")) == 0:
    securityNode = ET.fromstring("<security><passwordFile path=\"/var/lib/go-server/password\" /></security>")
    server.append(securityNode)
    config.write("/etc/go/cruise-config.xml")
    time.sleep(5) #TODO: can we find a better way to do this?