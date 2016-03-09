from setuptools import setup

setup(name='go_strong_auth_cli',
      version='0.1',
      description='CLI support for strong auth',
      url='https://github.com/danielsomerfield/go-strong-auth-plugin.git',
      author='Daniel Somerfield',
      author_email='dsomerfi@thoughtworks.com',
      license='TBD',
      packages=['go_strong_auth_cli'],
      zip_safe=False,
      entry_points={
          'console_scripts': ['generate_entry=go_strong_auth_cli.command_line:main'],
      }
      )
