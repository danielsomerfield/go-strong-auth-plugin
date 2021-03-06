GO Strong Auth Plugin
=====================
An authentication plugin for Go CD that supports configurable that supports good password storage practices through:

- configurable algorithms, key-sizes, and iterations
- salts

Currently, a file is the only supported password mechanism, although it should be fairly straightforward to extend it to
support other options. The plugin monitors changes in configuration, and changes in the password file and dynamically
 reloads when they change.

Using the Plugin
================

# Installation and Configuration

1. [Install the plugin](https://docs.go.cd/current/extension_points/plugin_user_guide.html#installing-and-uninstalling-of-plugins)
on your Go CD instance
2. (Optional) Set the path to your password file (defaults to /etc/go/passwd). You can find the configuration page at
    Admin -> Plugins -> StrongAuthPlugin (click the gear next to the name)
3. Set a dummy entry for the legacy Password File Settings component. This is a hack because auth won't enable without this value. Hopefully
    this requirement will disappear soon. Go to Admin -> Server Configuration and set the "Password File Path" to any value.
4. Add entries to the password file (see the "Password File Management" section below)
5. Login!

# Password File Management #

The plugin runs against a password file at /etc/go/passwd, or another path if you choose.

## File format ##
The file format is as follows:

*USERNAME*:*PASSWORD_HASH*:*SALT*:*HASH_CONFIGURATION*

The following entry:

    username:       aUser
    password:       aSecret
    salt:           9bwuMoD1hVie3v+blZ/3+Q==
    hash algorithm: PBKDF2WithHmacSHA1
    iterations:     10000
    key size:       256

would look like this:

    aUser:1f78942ee56928663434a0157ccd9ee3463236b7088f7da49c864f3214408f28:9bwuMoD1hVie3v+blZ/3+Q==:PBKDF2WithHmacSHA1(10000, 256)

The implementation currently assumes that any supported hash algorithm will take a count of iterations and key size, but
there are plans to make this mechanism more flexible to support other algorithm types through custom configuration parsers.

## Creating password entries
A python-based CLI is provided for generating entries. Currently it only supports bcrypt.
You can tweak the script if you want to change the configuration. Install the CLI as follows:

    cd go_strong_auth_cli
    pip install .

Then you can use the CLI with the generate_entry command as follows:

    ╰─➤ generate_entry

    Username: aUser
    Password: ******

    aUser:$2a$12$DfogsROFDSLnWqEyKW2i8ep4ILtkhJ6h2a7heXJtgN2aKVjl0B6ZS::bcrypt

Notice that the salt fields is empty because bcrypt builds the salt into the hash string.

# Building
You can build the plugin by executing `./gradlew jar` from the project root directory.
The plugin will be built at build/libs/e2e-VERSION.jar

## Targets
- jar - create the deployable jar
- test - run unit / integration tests
- executeFeatures - run end-to-end tests (requires docker environment to be loaded)

## Prerequisites
- Java 7 or later
- Docker (to run end-to-end tests)

License
-------

```plain

Copyright 2016 Daniel Somerfield

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```

TODO
----
- make script smarter about finding Go CD host to connect to
- strong default algorithm, keysize, and iteration count, if not specified
- more flexible hash algorithm configuration
- add configuration for how often to check file for reload, based on env variable, for testing purposes
- create UI for generating entries (?)

Go CD Core Recommendations
--------------------------
- Change behavior of auth extension so auth is enable by request for any auth plugin (rather than hard coded file and oauth)
    - IsSecurityEnabledVoter should delegate to plugins such that if any plugin returns true, then auth is enabled
    - IsSecurityEnabledVoter.vote() -> AuthenticationPluginRegistry.getAuthenticationPlugins
                                    -> AuthenticationExtension.isSecurityEnabled(): makes request with message to check auth enabled

- Make logging api more delegate-friendly by making plugin id capable without constantly adding the static field

Road Map (subject to change)
--------
0.3 - add migration support for legacy files

Release Notes
-------------
0.2 - added support for different hash mechanisms via provider interface
    - add providers that uses bcrypt or built-in Java PBE
    - changed python script to use bcrypt format
0.1 - initial release with support for PBE-based hashing