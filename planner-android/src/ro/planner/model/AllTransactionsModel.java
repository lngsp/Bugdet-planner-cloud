package ro.planner.model;

import java.util.Date;

public class AllTransactionsModel {
    private String description;
    private String amount;
    private String completionDate;

    public AllTransactionsModel(){}

    public AllTransactionsModel(String description, String amount, String completionDate) {
        this.description = description;
        this.amount = amount;
        this.completionDate = completionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

}