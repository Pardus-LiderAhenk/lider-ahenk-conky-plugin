# !/usr/bin/python
# -*- coding: utf-8 -*-


import json

from base.plugin.abstract_plugin import AbstractPlugin


class Conky(AbstractPlugin):
    def __init__(self, data, context):
        super(Conky, self).__init__()
        self.data = data
        self.context = context
        self.logger = self.get_logger()
        self.conky_config_file = '/etc/conky/conky.conf'
        self.autostart_path = self.get_home_path() + '.config/autostart'
        self.autorun_file_path = self.autostart_path + '/conky.desktop'
        self.logger.debug('[Conky] Parameters were initialized.')

    def handle_policy(self):
        try:

            try:
                if self.is_installed('conky') is False:
                    self.logger.info('[Conky] Could not found Conky. It will be installed')
                    self.logger.debug('[Conky] Conky installing with using apt-get')
                    self.install_with_apt_get('conky')
                    self.logger.info('[Conky] Could installed')

                self.logger.debug('[Conky] Some processes found which names are conky. They will be killed.')
                self.execute('killall -9 conky')
            except:
                self.logger.error('[Conky] Conky install-kill problem.')
                raise

            self.create_file('/tmp/conky.conf')
            self.logger.debug('[Conky] Temp file was created.')

            self.write_file('/tmp/conky.conf', json.loads(self.data)['message'])
            self.logger.debug('[Conky] Temp file was filled by context.')

            self.copy_file('/tmp/conky.conf', '/etc/conky/conky.conf')
            self.logger.debug('[Conky] Configurated conf file.')

            self.logger.debug('[Conky] Creating autorun file...')
            self.create_autorun_file()

            try:
                self.logger.debug('[Conky] Starting autorun...')
                self.execute('conky -d -q', result=False)
                self.logger.debug('[Conky] Autorun is OK.')
            except Exception as e:
                self.logger.error('[Conky] Conky execution problem.')
                raise

            self.logger.debug('[Conky] Creating response.')
            self.context.create_response(code=self.get_message_code().POLICY_PROCESSED.value, message='Conky policy executed successfully')


        except Exception as e:
            self.logger.error('[Conky] A problem occurred while handling Conky policy. Error Message: {}'.format(str(e)))
            self.context.create_response(code=self.get_message_code().POLICY_ERROR.value, message='A problem occurred while handling Conky policy')

    def create_autorun_file(self):

        if self.is_exist(self.autostart_path) == False:
            self.create_directory(self.autostart_path)

        file_content = '[Desktop Entry]\n' \
                        'Encoding=UTF-8 \n' \
                        'Type=Application \n' \
                        'Name=Conky \n' \
                        'Comment=Conky Monitor \n' \
                        'Exec=conky -d\n' \
                        'StartupNotify=false \n' \
                        'Terminal=false \n'

        autorun_file = open(self.autorun_file_path, 'w')
        autorun_file.write(file_content)
        autorun_file.close()


    def get_home_path(self):
        # TODO get home path from util
        return '/home/' + self.context.get('username') + '/'

def handle_policy(profile_data, context):
    print('[Conky] Handling...')
    plugin = Conky(profile_data, context)
    plugin.handle_policy()
    print('[Conky] Executed.')
