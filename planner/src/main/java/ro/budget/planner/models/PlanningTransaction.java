package ro.budget.planner.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "planning_transaction")
public class PlanningTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, length = 255, unique = true)
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    @Column(name = "completion_date")
    private Date completionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public PlanningTransaction(String description, Double amount, boolean completed, Date completionDate, User user) {
        this.description = description;
        this.amount = amount;
        this.completed = completed;
        this.completionDate = completionDate;
        this.user = user;
    }

    public PlanningTransaction() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
