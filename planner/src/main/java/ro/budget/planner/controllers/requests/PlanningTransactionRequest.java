package ro.budget.planner.controllers.requests;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class PlanningTransactionRequest {

    private String description;
    private Double amount;
    private boolean completed;
    @JsonFormat(pattern="dd.MM.yyyy")
    private Date completionDate;
    private String username;

    // Constructori, getteri și setteri

    public PlanningTransactionRequest() {
    }

    public PlanningTransactionRequest(String description, Double amount, boolean completed, Date completionDate, String username) {
        this.description = description;
        this.amount = amount;
        this.completed = completed;
        this.completionDate = completionDate;
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
