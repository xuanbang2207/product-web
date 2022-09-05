package com.home.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.home.domain.MailInfo;
import com.home.service.MailerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class MailerServiceImpl implements MailerService{

	@Autowired
	JavaMailSender sender;
	
	@Override
	public void send(MailInfo mail) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(mail.getFrom());
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(),true);
		helper.setReplyTo(mail.getFrom());
		for (String email : mail.getCc()) {
			helper.addCc(email);
		}
		for (String  email : mail.getBcc()) {
			helper.addBcc(email);
			}
			
		for (File file : mail.getFiles()) {
			helper.addAttachment(file.getName(), file);
		}
		
		sender.send(message);
		
	}
	
	@Override
	public void send(String to, String subject, String body) throws MessagingException {
		MailInfo mail = new MailInfo(to, subject, body);
		this.send(mail);
	}
	
}
