package ro.budget.planner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.budget.planner.controllers.requests.PlanningTransactionRequest;
import ro.budget.planner.controllers.requests.UpdateAmountTransactionRequest;
import ro.budget.planner.controllers.requests.UpdateCompletedRequest;
import ro.budget.planner.exceptions.PlanningTransactionNotFoundException;
import ro.budget.planner.exceptions.UserNotFoundException;
import ro.budget.planner.models.PlanningTransaction;
import ro.budget.planner.services.PlanningTransactionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/planner/planning-transactions")
public class PlanningTransactionController {

    private final PlanningTransactionService planningTransactionService;

    @Autowired
    public PlanningTransactionController(PlanningTransactionService planningTransactionService) {
        this.planningTransactionService = planningTransactionService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPlanningTransactions() {
        List<PlanningTransaction> planningTransactions = planningTransactionService.getAllPlanningTransactions();
        return new ResponseEntity<>(planningTransactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlanningTransactionById(@PathVariable Long id) {
        try {
            Optional<PlanningTransaction> transaction = planningTransactionService.getPlanningTransactionById(id);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        }catch (PlanningTransactionNotFoundException e){
            return new ResponseEntity<>("Transaction not found!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PlanningTransaction>> getTransactionsByUsername(@PathVariable String username) {
        List<PlanningTransaction> transactions = planningTransactionService.getTransactionsByUsername(username);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPlanningTransaction(@RequestBody PlanningTransactionRequest request) {
        try{
            PlanningTransaction createdTransaction = planningTransactionService.createPlanningTransaction(request);
            return new ResponseEntity<>(createdTransaction, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/amount")
    public ResponseEntity<?> updateTransactionAmount(@RequestBody UpdateAmountTransactionRequest request) {
        try {
            planningTransactionService.updateTransactionAmount(request);
            return new ResponseEntity<>("Amount update!", HttpStatus.OK);
        } catch (PlanningTransactionNotFoundException e) {
            return new ResponseEntity<>("Transaction not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/completed")
    public ResponseEntity<?> updateCompleted(@RequestBody UpdateCompletedRequest request) {
        try {
            planningTransactionService.updateCompleted(request);
            return new ResponseEntity<>("Completed!", HttpStatus.OK);
        } catch (PlanningTransactionNotFoundException e) {
            return new ResponseEntity<>("Transaction not found!", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlanningTransaction(@PathVariable Long id) {
        planningTransactionService.deletePlanningTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
