package by.zborovskaya.final_project.service;

import by.zborovskaya.final_project.util.mail.Mail;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail {

    public static void main(String [] args) {
        Mail.createMail("a.zborovskii@gmail.com","teashop","logged in");
    }
}
