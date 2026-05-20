# Job Application Email Sender

This Spring Boot application automatically reads a list of email addresses, sends an email with a PDF resume attachment to each, and removes the successfully sent emails from the list.

## Setup Instructions

1. **Add Your Emails**: Open `emails.txt` and add the HR email addresses you want to send your resume to (one email per line).
2. **Add Your Resume**: Place your resume PDF in this folder and name it exactly `resume.pdf`.
3. **Configure Email Settings**: Open `src/main/resources/application.properties` and add your email and app password.
   - If using Gmail, you cannot use your standard password. You must generate an **App Password** from your Google Account settings (Security -> 2-Step Verification -> App passwords).
4. **Customize the Body/Subject**: Open `src/main/java/com/jobapp/service/JobApplicationEmailService.java` and modify the `emailBody` and `emailSubject` fields with your specific details.

## Running the Application

When you run the application, it will immediately process all emails in `emails.txt`. It is also scheduled to run automatically every day at 10:00 AM if you leave it running.

To run via Maven, open a terminal in this directory and execute:
```bash
mvn spring-boot:run
```
