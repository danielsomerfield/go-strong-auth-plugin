from setuptools import setup

setup(name='go_strong_auth_cli',
      version='0.2',
      description='CLI support for strong auth',
      url='https://github.com/danielsomerfield/go-strong-auth-plugin.git',
      author='Daniel Somerfield',
      author_email='dsomerfi@thoughtworks.com',
      license='Apache License 2.0',
      packages=['go_strong_auth_cli'],
      install_requires=[
            'bcrypt',
      ],
      zip_safe=False,
      
      entry_points={
          'console_scripts': ['generate_entry=go_strong_auth_cli.command_line:main'],
      }
      )
