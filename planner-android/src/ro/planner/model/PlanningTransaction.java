package ro.planner.model;

import java.util.Date;

public class PlanningTransaction {
    private String description;
    private Double amount;
    private boolean completed = false;
    private Date completionDate;

    public PlanningTransaction(){}

    public PlanningTransaction(String description, Double amount, boolean completed, Date completionDate) {
        this.description = description;
        this.amount = amount;
        this.completed = completed;
        this.completionDate = completionDate;
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
}
