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
        self.username = self.context.get('username')
        self.home_path = '/home/' + self.username
        self.conky_config_file_dir = self.home_path + '/.conky'
        self.conky_config_file_path = self.conky_config_file_dir + '/conky.conf'
        self.autostart_path = self.home_path + '/.config/autostart'
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

            if self.is_exist(self.conky_config_file_dir) == True:
                self.logger.debug('[Conky] Old config file will be deleted.')
                self.delete_file(self.conky_config_file_path)
            else :
                self.logger.debug('[Conky] Creating directory for conky config at ' + self.conky_config_file_dir)
                self.create_directory(self.conky_config_file_dir)


            if self.create_file(self.conky_config_file_path) :
                self.logger.debug('[Conky] Config file was created.')

                self.write_file(self.conky_config_file_path, json.loads(self.data)['message'])
                self.logger.debug('[Conky] Config file was filled by context.')

                try:
                    self.logger.debug('[Conky] Creating autorun file...')
                    self.create_autorun_file()
                    self.logger.debug('[Conky] Executing conky with command : su ' + self.username + ' -c "conky -d -c ' + self.conky_config_file_path + '"')
                    self.execute('su ' + self.username + ' -c "conky -d -c ' + self.conky_config_file_path + '"', result=False)
                    self.logger.debug('[Conky] Autorun is OK.')
                except Exception as e:
                    self.logger.error('[Conky] Conky autorun problem.')
                    raise

                change_owner = 'chown -hR ' + self.username + ':' + self.username + ' ' + self.conky_config_file_dir
                self.execute(change_owner)
                self.logger.info('[Conky] Owner of Conky config file was changed.')

                self.context.create_response(code=self.get_message_code().POLICY_PROCESSED.value, message='Conky policy executed successfully')

            else:
                self.logger.error('[Conky] Conky config file could not be created.')
                self.context.create_response(code=self.get_message_code().POLICY_ERROR.value, message='Conky config file could not be created.')

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
                        'Exec=conky -d -c ' + self.conky_config_file_path + '\n' \
                        'StartupNotify=false \n' \
                        'Terminal=false \n'

        autorun_file = open(self.autorun_file_path, 'w')
        autorun_file.write(file_content)
        autorun_file.close()


def handle_policy(profile_data, context):
    print('[Conky] Handling...')
    plugin = Conky(profile_data, context)
    plugin.handle_policy()
    print('[Conky] Executed.')
