package com.home.service;

import javax.mail.MessagingException;

import com.home.domain.MailInfo;

public interface MailerService {

	void send(String to, String subject, String body) throws MessagingException;

	void send(MailInfo mail) throws MessagingException;
}
