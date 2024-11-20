package model;

import java.time.LocalDateTime;

public class Request extends Subject{
    private final RequestTypes requestType;
    private LocalDateTime submissionDate;
    private final String name;
    private final String description;
    private String submitterUsername;
    private String solverUsername;

    public String getSolverUsername() {
        return solverUsername;
    }

    public RequestTypes getRequestType() {
        return requestType;
    }

    public String getName() {
        return name;
    }

    public String getSubmitterUsername() {
        return submitterUsername;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSolverUsername(String solverUsername) {
        this.solverUsername = solverUsername;
    }

    public void setSubmitterUsername(String submitterUsername) {
        this.submitterUsername = submitterUsername;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Request(RequestTypes requestType, String name, String description) {
        this.requestType = requestType;
        this.name = name;
        this.description = description;
    }

    public Request(RequestTypes requestType, LocalDateTime submissionDate, String name, String description,
                   String submitterUsername, String solverUsername) {
        this.requestType = requestType;
        this.submissionDate = submissionDate;
        this.name = name;
        this.description = description;
        this.submitterUsername = submitterUsername;
        this.solverUsername = solverUsername;
    }

    @Override
    public String toString() {
        return "Request Type: " + requestType + "\nSubmission Date: " + submissionDate +
                (name == null ? "" : "\nName: " + name) + "\nDescription: " + description + "\nSubmitted by: " +
                submitterUsername + "\nAssigned to: " + solverUsername;
    }
}
