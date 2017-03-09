Go Strong Auth CLI
=======================
A CLI for generating password file entries for the `Go CD <https://www.gocd.io/>`_ 
    `Strong Auth Plugin <https://github.com/danielsomerfield/go-strong-auth-plugin>`_.

Currently it only supports bcrypt. You can tweak the script if you want to change the configuration. 
Install the CLI as follows:

    cd go_strong_auth_cli
    pip install .

Then you can use the CLI with the generate_entry command as follows:

    ╰─➤ generate_entry

    Username: aUser
    Password: ******

    aUser:$2a$12$DfogsROFDSLnWqEyKW2i8ep4ILtkhJ6h2a7heXJtgN2aKVjl0B6ZS::bcrypt

Notice that the salt fields is empty because bcrypt builds the salt into the hash string.

