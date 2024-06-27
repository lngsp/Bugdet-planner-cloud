package ro.budget.planner.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.budget.planner.models.PlanningTransaction;
import ro.budget.planner.models.User;

import java.util.List;

@Repository
public interface PlanningTransactionRepository extends JpaRepository<PlanningTransaction, Long> {
    PlanningTransaction findByUserAndDescription(User user, String description);

    List<PlanningTransaction> findByUserUsername(String username);
}
