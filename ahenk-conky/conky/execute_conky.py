#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author: Edip YILDIZ


from base.model.enum.content_type import ContentType
import json

from base.plugin.abstract_plugin import AbstractPlugin

class RunConkyCommand(AbstractPlugin):

    def __init__(self, data, context):
        super(AbstractPlugin, self).__init__()
        self.data = data
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()
        self.conky_config_file_dir = '/etc/conky'
        self.conky_config_file_path = self.conky_config_file_dir + '/conky.conf'
        self.logger.debug('[Conky] Parameters were initialized.')


    def remove_conky_message(self):
        pass

    def execute_conky(self, conky_message):
        self.logger.debug("[CONKY] Executing conky.")
        try:
            if self.is_installed('conky') is False:
                self.logger.info('[Conky] Could not found Conky. It will be installed')
                self.logger.debug('[Conky] Conky installing with using apt-get')
                self.install_with_apt_get('conky')
                self.logger.info('[Conky] Could installed')

            self.logger.debug('[Conky] Some processes found which names are conky. They will be killed.')
            self.execute('killall conky')

        except:
            self.logger.error('[Conky] Conky install-kill problem.')
            raise

        if self.is_exist(self.conky_config_file_dir) == False:
            self.logger.debug('[Conky] Creating directory for conky config at ' + self.conky_config_file_dir)
            self.create_directory(self.conky_config_file_dir)

        if self.is_exist(self.conky_config_file_path) == True:
            self.logger.debug('[Conky] Old config file will be renamed.')
            self.rename_file(self.conky_config_file_path,  self.conky_config_file_path+'_old')
            self.logger.debug('[Conky] Old config file will be renamed to '+ (self.conky_config_file_path+'old') )

        self.create_file(self.conky_config_file_path)
        self.write_file(self.conky_config_file_path, conky_message)
        self.logger.debug('[Conky] Config file was filled by context.')

        self.execute('conky ',result=False)

        self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                     message='Conky başarıyla oluşturuldu.',
                                     data=json.dumps({'Result': conky_message}),
                                     content_type=ContentType.APPLICATION_JSON.value)

    def handle_task(self):
        try:
            conky_message = self.data['conkyMessage']
            remove_conky_message = self.data['removeConkyMessage']

            if remove_conky_message:
                self.remove_conky_message()

            else:
                self.execute_conky(conky_message)

        except Exception as e:
            self.logger.error(" error on handle conky task. Error: "+str(e))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='Anlık kaynak kullanım bilgisi toplanırken hata oluştu:' + str(e),
                                         content_type=ContentType.APPLICATION_JSON.value)


def handle_task(task,context):
    cls=RunConkyCommand(task,context)
    cls.handle_task()
