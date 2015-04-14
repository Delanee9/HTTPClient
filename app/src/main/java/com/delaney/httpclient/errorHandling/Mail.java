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
    private String[] to;
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

        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public Mail(String users, String pass) {
        this();
        user = users;
        password = pass;
    }

    public void send() throws Exception {
        Properties props = _setProperties();

        if(!user.isEmpty() && !password.isEmpty() && to.length > 0 && !from.isEmpty() && !subject.isEmpty() && !body.isEmpty()) {
            Session session = Session.getInstance(props, this);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress[] addressTo = new InternetAddress[to.length];

            for(int i = 0; i < to.length; i++) {
                addressTo[i] = new InternetAddress(to[i]);
            }

            message.setRecipients(MimeMessage.RecipientType.TO, addressTo);
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
        Properties props = new Properties();

        props.put("mail.smtp.host", host);

        if(_debuggable) {
            props.put("mail.debug", "true");
        }

        if(auth) {
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", _sport);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        return props;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTo(String[] toArr) {
        this.to = toArr;
    }

    public void setFrom(String string) {
        this.from = string;
    }

    public void setSubject(String string) {
        this.subject = string;
    }
}