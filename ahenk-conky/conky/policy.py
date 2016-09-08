# !/usr/bin/python
# -*- coding: utf-8 -*-
# Author: Volkan Şahin <volkansah.in> <bm.volkansahin@gmail.com>

import json

from base.plugin.abstract_plugin import AbstractPlugin


class Conky(AbstractPlugin):
    def __init__(self, data, context):
        super(Conky, self).__init__()
        self.data = data
        self.context = context
        self.logger = self.get_logger()
        self.machine_profile = True
        self.conky_config_file_dir = '/etc/conky/'
        self.conky_config_file_path = '/etc/conky/conky.conf'
        self.command_autorun_conky = 'sleep 3;conky -d {0} -c {1}'
        self.username = None
        self.autostart_dir_path = '{0}.config/autostart/'
        self.autorun_file_path = '{0}conky.desktop'
        self.logger.debug('[Conky] Parameters were initialized.')

    def handle_policy(self):
        try:

            # Checking dependecies
            if self.check_dependencies(['conky', 'conky-all']) is True:
                self.logger.debug('[Conky] Dependencies checked.')
            else:
                return

            # Killing conky processes
            self.logger.debug('[Conky] Conky named processes will be killed.')
            result_code, p_out, p_err = self.execute('killall -9 conky')
            # if result_code != 0:
            #     self.logger.error('[Conky] Conky kill command not worked properly.')
            #     raise Exception

            # Is user profile
            if 'username' in self.context.data and self.context.get('username') is not None:
                self.logger.debug('[Conky] This is user profile, parameters reinitializing.')
                self.username = self.context.get('username')
                self.conky_config_file_dir = '{0}.conky/'.format(self.Sessions.user_home_path(self.username))
                self.conky_config_file_path = '{0}conky.conf'.format(self.conky_config_file_dir)
                self.machine_profile = False

            # Creating/checking conky file dir and conky conf file
            self.logger.debug('[Conky] Conky file directory and configuration file is creating/checking')
            if self.is_exist(self.conky_config_file_dir):
                self.logger.debug('[Conky] Old config file will be deleted.')
                self.delete_file(self.conky_config_file_path)
            else:
                self.logger.debug(
                    '[Conky] Creating directory for conky config at {0}'.format(self.conky_config_file_dir))
                self.create_directory(self.conky_config_file_dir)

            if self.create_file(self.conky_config_file_path):
                self.logger.debug('[Conky] Config file was created.')
                self.write_file(self.conky_config_file_path, json.loads(self.data)['message'])
                self.logger.debug('[Conky] Config file was filled by context.')
            else:
                self.logger.error('[Conky] A problen occurred while creating Conky configuration file.')
                raise Exception('File {0} could not created.'.format(self.conky_config_file_path))

            # Creating autorun
            self.logger.debug('[Conky] Creating autorun file...')
            self.initialize_auto_run()

            if self.machine_profile is False:
                self.execute(
                    self.command_autorun_conky.format('--display=' + self.Sessions.display(self.username),
                                                      self.conky_config_file_path),
                    as_user=self.username, result=False)


                self.execute('chown -hR ' + self.username + ':' + self.username + ' ' + self.conky_config_file_dir)
                self.logger.debug('[Conky] Owner of Conky config file was changed.')
            else:
                self.execute(self.command_autorun_conky.format('', self.conky_config_file_path), result=False)

            self.logger.debug('[Conky] Autorun command executed successfully')
            self.context.create_response(code=self.get_message_code().POLICY_PROCESSED.value,
                                         message='Conky politikası başarıyla çalıştırıldı.')

        except Exception as e:
            self.logger.error(
                '[Conky] A problem occurred while handling Conky policy. Error Message: {}'.format(str(e)))
            self.context.create_response(code=self.get_message_code().POLICY_ERROR.value,
                                         message='Conky politikası uygulanırken bir hata oluştu.')

    def check_dependencies(self, packages):

        self.logger.debug('[Conky] Checking dependencies')
        for package in packages:
            if self.is_installed(package) is False:
                self.logger.debug('[Conky] Could not found {0}. It will be installed'.format(package))
                result_code, p_out, p_err = self.install_with_apt_get(package)
                if result_code == 0:
                    self.logger.debug('[Conky] {0} installed successfully'.format(package))
                else:
                    self.logger.error(
                        '[Conky] A problem occurred while installing {0} package. Error Message: {1}'.format(package,
                                                                                                             str(
                                                                                                                 p_err)))
                    self.context.create_response(code=self.get_message_code().POLICY_ERROR.value,
                                                 message='Bağımlılıklardan {0} paketi kurulurken hata oluştu.')
                    return False

        return True

    def initialize_auto_run(self):

        if self.machine_profile is True:
            self.logger.debug('[Conky] All users conky configuration files will be removed because of machine profile')
            if self.Sessions.user_name() is not None and len(self.Sessions.user_name()) > 0:
                for username in self.Sessions.user_name():
                    self.logger.debug(
                        '[Conky] Removing conf file of user {0}'.format(username))
                    self.delete_file(
                        self.autorun_file_path.format(
                            self.autostart_dir_path.format(self.Sessions.user_home_path(username))))
            else:
                self.logger.debug(
                    '[Conky] There are no user')

        else:
            home_path = self.Sessions.user_home_path(self.username)
            self.logger.debug(
                '[Conky] Creating autorun file for user {0}'.format(self.username))
            self.create_autorun_file(self.autostart_dir_path.format(home_path),
                                     self.conky_config_file_path,
                                     self.autorun_file_path.format(self.autostart_dir_path.format(home_path)))
            self.logger.debug(
                '[Conky] Autorun created')

    def create_autorun_file(self, autostart_path, conky_config_file_path, autorun_file_path):
        if not self.is_exist(autostart_path):
            self.logger.debug(
                '[Conky] Creating file: {0}'.format(autostart_path))
            self.create_directory(autostart_path)

        file_content = '[Desktop Entry]\n' \
                       'Encoding=UTF-8 \n' \
                       'Type=Application \n' \
                       'Name=Conky \n' \
                       'Comment=Conky Monitor \n' \
                       'Exec=conky -d -c ' + conky_config_file_path + '\n' \
                                                                      'StartupNotify=false \n' \
                                                                      'Terminal=false \n'
        self.logger.debug(
            '[Conky] Writing content to autorun file.')
        self.write_file(autorun_file_path, file_content, 'w')


def handle_policy(profile_data, context):
    plugin = Conky(profile_data, context)
    plugin.handle_policy()
