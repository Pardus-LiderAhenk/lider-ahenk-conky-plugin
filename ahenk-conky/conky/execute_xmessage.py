#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author: Edip YILDIZ


from base.model.enum.content_type import ContentType
import json

from base.plugin.abstract_plugin import AbstractPlugin

import threading


class RunXMessageCommand(AbstractPlugin):
    def __init__(self, data, context):
        super(AbstractPlugin, self).__init__()
        self.data = data
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()

        self.xmessage_command= "su {0} -c 'export DISPLAY={1} && export XAUTHORITY=~{2}/.Xauthority && xmessage \"{3}\" ' "

    def execute_xmessage(self, message, timeout):

        users=self.Sessions.user_name();

        for user in users:
            user_display = self.Sessions.display(user)
            user_ip= self.Sessions.userip(user)

            if user_display is None:
                self.logger.debug('[XMessage] executing for display none for user  '+ str(user))

            else :

                self.logger.debug('[XMessage] user display ' + str(user_display) +' user '+ str(user))

                if user_ip is None:
                    self.execute(" xmessage -nearmouse -buttons Tamam -timeout "+ str(timeout) + " "+str(message))
                else:
                    self.execute(self.xmessage_command.format(user, user_display,user,message), ip=user_ip)


        self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                     message='İşlem başarıyla gerçekleştirildi.',
                                     data=json.dumps({'Result': message}),
                                     content_type=ContentType.APPLICATION_JSON.value)

    def handle_task(self):
        try:
            message = self.data['message']
            timeout = self.data['timeout']

            self.execute_xmessage(message,timeout)

        except Exception as e:
            self.logger.error(" error on handle xmessage task. Error: " + str(e))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='XMessage mesajı olusturulurken hata oluştu:' + str(e),
                                         content_type=ContentType.APPLICATION_JSON.value)


def handle_task(task, context):
    cls = RunXMessageCommand(task, context)
    cls.handle_task()
