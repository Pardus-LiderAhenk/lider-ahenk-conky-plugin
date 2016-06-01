
#!/usr/bin/python
# -*- coding: utf-8 -*-

"""
Style Guide is PEP-8
https://www.python.org/dev/peps/pep-0008/
"""

import json
import os
import tempfile

from base.plugin.AbstractCommand import AbstractCommand


class Conky(AbstractCommand):
    def __init__(self, data, context):
        super(Conky, self).__init__()
        self.data = data
        self.context = context
        self.conky_config_file = '/etc/conky/conky.conf'
        self.start_command = 'conky &'

    def handle_policy(self):
        self.create_file()
        self.replace_conky_files()
        self.start_conky()

    def create_file(self):
        path = '/tmp/'

        if not os.path.exists(path):
            os.makedirs(path)

        try:
            filename = 'conky.conf'
            with open(os.path.join(path, filename), 'w') as temp_file:
                message = json.loads(self.data)['message']
                print(message)
                temp_file.write(str(message))
            temp_file.close()
        except Exception as e:
            print(str(e))

    def replace_conky_files(self):
        process = self.context.execute('sudo cp  /tmp/conky.conf /etc/conky/conky.conf')
        process.wait()


    def start_conky(self):
        process = self.context.execute(self.start_command)
        process.wait()


def handle_policy(profile_data, context):
    print('CONKY PLUGIN')
    plugin = Conky(profile_data, context)
    plugin.handle_policy()
    print("This is policy file - CONKY")
