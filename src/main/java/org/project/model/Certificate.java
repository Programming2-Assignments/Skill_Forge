package org.project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Certificate {
    private int certificateId;
    private int studentId;
    private int courseId;
    private String issueDate;

    public Certificate(int studentId, int courseId) {
        this.certificateId = new Random().nextInt(10000);
        this.studentId = studentId;
        this.courseId = courseId;
        this.issueDate =  setDate();
    }

    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(int certificateId) {
        this.certificateId = certificateId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String setDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate now = LocalDate.now();
        String date = dtf.format(now);
        return date;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
}
