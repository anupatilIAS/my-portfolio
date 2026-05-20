package com.jobapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JobApplicationEmailService {

    @Autowired
    private JavaMailSender emailSender;

    // Text file containing the HR contacts (in table format)
    private final String emailFilePath = "emails.txt";
    
    // File to keep track of emails we've already sent to
    private final String historyFilePath = "sent_emails.txt";
    
    // Path to your resume PDF
    private final String resumeFilePath = "D:\\Spring Boot\\bulkmailsoftware2\\bulkmailsoftware\\Anushka_Patil_Resume.pdf";

    @Scheduled(cron = "0 0 10 * * ?") 
    public void processEmails() {
        System.out.println("Starting email processing from emails.txt...");
        Path path = Paths.get(emailFilePath);
        
        if (!Files.exists(path)) {
            System.out.println("Error: Email list file not found ('" + emailFilePath + "'). Please create it.");
            return;
        }

        File resumeFile = new File(resumeFilePath);
        if (!resumeFile.exists()) {
            System.out.println("Error: Resume PDF not found ('" + resumeFilePath + "'). Please place it in the project folder.");
            return;
        }

        // Load emails we have already sent to
        Set<String> sentEmails = loadSentEmails();

        try {
            List<String> lines = Files.readAllLines(path);
            
            for (String line : lines) {
                // Skip headers and separators
                if (line.trim().isEmpty() || line.startsWith("SNo") || line.startsWith("---")) {
                    continue;
                }
                
                // Split the line by the pipe character '|'
                String[] parts = line.split("\\|");
                
                // We need at least 3 parts to get the email address
                if (parts.length < 3) {
                    continue; 
                }
                
                String targetEmail = parts[2].trim();
                
                if (targetEmail.isEmpty() || sentEmails.contains(targetEmail)) {
                    continue; // Skip empty emails or people we already emailed
                }

                String hrName = parts.length > 1 ? parts[1].trim() : "";
                if (hrName.isEmpty()) {
                    hrName = "HR Team";
                }

                String companyName = parts.length > 4 ? parts[4].trim() : "";
                if (companyName.isEmpty()) {
                    companyName = "your company";
                }

             // Gender-based greeting logic
                String gender = parts.length > 5 ? parts[5].trim().toUpperCase() : "";

                String greeting;

                switch (gender) {
                    case "F":
                        greeting = "Ma'am";
                        break;
                    case "M":
                        greeting = "Sir";
                        break;
                    case "L":
                        greeting = "Ma'am/Sir";
                        break;
                    default:
                        greeting = "Ma'am/Sir";
                }
                
                // Personalize the email subject and body
                String emailSubject = "Application for Java Backend Developer | 2 Years Experience | Immediate Joiner";
             // Updated Email Body
                String emailBody =
                		"Dear " + greeting + ",\n\n" +

                        "I hope you are doing well.\n\n" +

                        "I am writing to express my interest in the 𝐉𝐚𝐯𝐚 𝐁𝐚𝐜𝐤𝐞𝐧𝐝 𝐃𝐞𝐯𝐞𝐥𝐨𝐩𝐞𝐫 position at your organization. " +
                        "I have around 𝟐 𝐲𝐞𝐚𝐫𝐬 𝐨𝐟 𝐞𝐱𝐩𝐞𝐫𝐢𝐞𝐧𝐜𝐞 in backend development and am currently working as a " +
                        "𝐒𝐨𝐟𝐭𝐰𝐚𝐫𝐞 𝐄𝐧𝐠𝐢𝐧𝐞𝐞𝐫 𝐚𝐭 𝐓𝐃𝐓𝐋, 𝐏𝐮𝐧𝐞, contributing to banking applications for 𝐈𝐂𝐈𝐂𝐈 𝐁𝐚𝐧𝐤.\n\n" +

                        "In my current role, I have designed and developed secure 𝐑𝐄𝐒𝐓 𝐀𝐏𝐈𝐬 using 𝐉𝐚𝐯𝐚 and " +
                        "𝐒𝐩𝐫𝐢𝐧𝐠 𝐁𝐨𝐨𝐭 for high-volume production systems. I have strong experience in 𝐎𝐫𝐚𝐜𝐥𝐞 𝟏𝟗𝐜, " +
                        "where I performed query optimization and indexing, significantly improving API performance " +
                        "and reducing response time.\n\n" +

                        "I also have hands-on experience in implementing security features such as " +
                        "𝐀𝐄𝐒-𝟐𝟓𝟔 𝐞𝐧𝐜𝐫𝐲𝐩𝐭𝐢𝐨𝐧/𝐝𝐞𝐜𝐫𝐲𝐩𝐭𝐢𝐨𝐧, 𝐉𝐖𝐓-𝐛𝐚𝐬𝐞𝐝 𝐚𝐮𝐭𝐡𝐞𝐧𝐭𝐢𝐜𝐚𝐭𝐢𝐨𝐧, and " +
                        "𝐒𝐐𝐋 𝐢𝐧𝐣𝐞𝐜𝐭𝐢𝐨𝐧 𝐩𝐫𝐞𝐯𝐞𝐧𝐭𝐢𝐨𝐧, ensuring secure data handling in banking applications. " +
                        "Additionally, I have worked on deployments using 𝐓𝐨𝐦𝐜𝐚𝐭 and 𝐖𝐞𝐛𝐋𝐨𝐠𝐢𝐜 servers " +
                        "and handled production issues through efficient debugging and monitoring.\n\n" +

                        "Along with backend development, I also have basic frontend experience in " +
                        "𝐑𝐞𝐚𝐜𝐭, 𝐇𝐓𝐌𝐋, and 𝐂𝐒𝐒, which helps me understand end-to-end application flow " +
                        "and UI integration.\n\n" +

                        "I have contributed to projects like 𝐂𝐮𝐬𝐭𝐨𝐦𝐞𝐫 𝟑𝟔𝟎 𝐃𝐚𝐬𝐡𝐛𝐨𝐚𝐫𝐝 and " +
                        "𝐈𝐧𝐟𝐫𝐚𝐬𝐭𝐫𝐮𝐜𝐭𝐮𝐫𝐞 𝐌𝐚𝐧𝐚𝐠𝐞𝐦𝐞𝐧𝐭 𝐒𝐞𝐫𝐯𝐢𝐜𝐞 𝐆𝐫𝐨𝐮𝐩 (𝐈𝐌𝐒𝐆), where I worked on API development, " +
                        "database optimization, and third-party integrations such as 𝐏𝐀𝐍, 𝐀𝐚𝐝𝐡𝐚𝐚𝐫, " +
                        "and 𝐂𝐈𝐁𝐈𝐋 verification APIs.\n\n" +

                        "I am currently available as an 𝐈𝐦𝐦𝐞𝐝𝐢𝐚𝐭𝐞 𝐉𝐨𝐢𝐧𝐞𝐫 and am actively looking for opportunities " +
                        "where I can contribute to scalable, secure, and high-performance backend systems.\n\n" +

                        "Please find my resume attached for your reference. I would appreciate the opportunity " +
                        "to discuss how my skills and experience align with your requirements.\n\n" +

                        "Thank you for your time and consideration.\n\n" +

                        "Best regards,\n" +
                        "𝐀𝐧𝐮𝐬𝐡𝐤𝐚 𝐏𝐚𝐭𝐢𝐥\n" +
                        "📞 7972848640\n" +
                        "📧 anushkapatil6444@gmail.com\n" +
                        "🔗 LinkedIn: https://www.linkedin.com/in/anushka-patil-647a402234";

                try {
                    sendEmailWithAttachment(targetEmail, emailSubject, emailBody, resumeFilePath);
                    System.out.println("Successfully sent email to: " + targetEmail + " (" + hrName + " at " + companyName + ")");
                    
                    // Add to sent history so we don't send again next time the code runs
                    recordSentEmail(targetEmail);
                    sentEmails.add(targetEmail);
                    
                    // Add a 5-second delay to prevent spam filters
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.err.println("Failed to send email to " + targetEmail + ": " + e.getMessage());
                }
            }
            
            System.out.println("Finished processing all emails in emails.txt.");

        } catch (IOException e) {
            System.err.println("Error reading emails.txt: " + e.getMessage());
        }
    }

    private Set<String> loadSentEmails() {
        Set<String> sent = new HashSet<>();
        Path path = Paths.get(historyFilePath);
        if (Files.exists(path)) {
            try {
                sent.addAll(Files.readAllLines(path));
            } catch (IOException e) {
                System.err.println("Could not read sent_emails.txt");
            }
        }
        return sent;
    }

    private void recordSentEmail(String email) {
        Path path = Paths.get(historyFilePath);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.writeString(path, email + System.lineSeparator(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Could not save to sent_emails.txt: " + e.getMessage());
        }
    }

    private void sendEmailWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); 
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        
        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Resume.pdf", file);
        
        emailSender.send(message);
    }
}
