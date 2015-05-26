package com.delaney.httpclient.errorHandling;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class Mail extends javax.mail.Authenticator {
    private final String port;
    private final String _sport;
    private final String host;
    private final boolean auth;
    private final boolean _debuggable;
    private final Multipart _multipart;
    private String user;
    private String password;
    private String to;
    private String from;
    private String subject;
    private String body;

    private Mail() {
        host = "smtp.gmail.com";        // default smtp server
        port = "465";                   // default smtp port
        _sport = "465";                 // default socketfactory port

        user = "";                      // username
        password = "";                  // password
        from = "";                      // email sent from
        subject = "";                   // email subject
        body = "";                      // email body

        _debuggable = false;            // debug mode on or off - default off
        auth = true;                    // smtp authentication - default on

        _multipart = new MimeMultipart();

        MailcapCommandMap commandMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        commandMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        commandMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        commandMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        commandMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        commandMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(commandMap);
    }

    public Mail(String users, String pass) {
        this();
        user = users;
        password = pass;
    }

    public void send() throws Exception {
        Properties properties = _setProperties();

        if(!user.isEmpty() && !password.isEmpty() && !to.isEmpty() && !from.isEmpty() && !subject.isEmpty() && !body.isEmpty()) {
            Session session = Session.getInstance(properties, this);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress addressTo = new InternetAddress(to);

            message.setRecipient(MimeMessage.RecipientType.TO, addressTo);
            message.setSubject(subject);
            message.setSentDate(new Date());
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            _multipart.addBodyPart(messageBodyPart);
            message.setContent(_multipart);
            Transport.send(message);
        }
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    private Properties _setProperties() {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", host);

        if(_debuggable) {
            properties.put("mail.debug", "true");
        }

        if(auth) {
            properties.put("mail.smtp.auth", "true");
        }

        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.socketFactory.port", _sport);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        return properties;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTo(String toArr) {
        this.to = toArr;
    }

    public void setFrom(String string) {
        this.from = string;
    }

    public void setSubject(String string) {
        this.subject = string;
    }
}