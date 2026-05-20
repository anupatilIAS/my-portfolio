package com.jobapp;

import com.jobapp.service.JobApplicationEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobApplicationApplication implements CommandLineRunner {

    @Autowired
    private JobApplicationEmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(JobApplicationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // This will trigger the email sending process immediately when you run the application.
        System.out.println("Application started. Triggering immediate email processing...");
        emailService.processEmails();
    }
}
