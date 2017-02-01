package wikiAnalicis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import wikiAnalicis.service.EmailService;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {
	@Autowired
	private MailSender sender; 

	public EmailServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enviar(String fuente, String destino, String asunto, String mensaje) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(fuente);
		mail.setTo(destino);
		mail.setSubject(asunto);
		mail.setText(mensaje);
		//sender.send(mail);
	}

}
