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
A python-based CLI is provided for generating entries. Currently it only supports PBKDF2WithHmacSHA1 with a 256 bit key size
and 10,000 iterations. You can tweak the script if you want to change the configuration. Install the CLI as follows:

    cd go_strong_auth_cli
    pip install .

Then you can use the CLI with the generate_entry command as follows:

    ╰─➤ generate_entry

    Username: aUser
    Password: ******

    aUser:437ce21f5faa936934b24eb46af0197e5467fa85bd1f549951865950a8e55c8a:ZSIH77NB3VK5I5NOXJA6:PBKDF2WithHmacSHA1(10000,256)

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

TODO
----
- make script smarter about finding Go CD host to connect to
- delete docker image when finished with e2e test
- strong default algorithm, keysize, and iteration count, if not specified
- more flexible hash algorithm configuration
- add configuration for how often to check file for reload, based on env variable, for testing purposes
- create UI for generating entries (?)

Go CD Core Recommendations
--------------------------
- Change behavior of auth extension so auth is enable by request for any auth plugin (rather than hard coded file and oauth)
- Make logging api more delegate-friendly by making plugin id capable without constantly adding the static field