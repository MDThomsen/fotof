package dtu.fotof;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.omg.CORBA.portable.Delegate;

import javax.inject.Inject;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

@Stateless
@Named
public class OrderBusinessLogic {
    @Inject
    private TaskForm taskForm;

    // Inject the entity manager
    @PersistenceContext
    private EntityManager entityManager;

    public void persistBooking(DelegateExecution delegateExecution) {
        // Create new order instance
        BookingEntity bookingEntity = new BookingEntity();

        // Get all process variables
        Map<String, Object> variables = delegateExecution.getVariables();

        // Set order attributes
        //bookingEntity.setCustomer((String) variables.get("customer"));
        //bookingEntity.setAddress((String) variables.get("address"));
        //bookingEntity.setPizza((String) variables.get("pizza"));

    /*
      Persist order instance and flush. After the flush the
      id of the order instance is set.
    */
        entityManager.persist(bookingEntity);
        entityManager.flush();

        // Remove no longer needed process variables
        delegateExecution.removeVariables(variables.keySet());

        // Add newly created order id as process variable
        delegateExecution.setVariable("bookingId", bookingEntity.getId());
    }

    public void assignPhotographer(DelegateExecution delegateExecution) {
        Map<String, Object> variables = delegateExecution.getVariables();

        BookingEntity bookingEntity = getBooking((Long) variables.get("bookingId"));

        Random rand = new Random();
        double n = rand.nextDouble();

        if(n < 0.5)
            bookingEntity.setAssignedPhotographer("Jean-Claude");
        else
            bookingEntity.setAssignedPhotographer("Jørgen");

        mergeOrderAndCompleteTask(bookingEntity);
    }

    // Inject task form available through the Camunda cdi artifact


    public BookingEntity getBooking(Long bookingId) {
        // Load order entity from database
        return entityManager.find(BookingEntity.class, bookingId);
    }

    /*
      Merge updated order entity and complete task form in one transaction. This ensures
      that both changes will rollback if an error occurs during transaction.
     */
    public void mergeOrderAndCompleteTask(BookingEntity bookingEntity) {
        // Merge detached order entity with current persisted state
        entityManager.merge(bookingEntity);
        try {
            // Complete user task from
            taskForm.completeTask();
        } catch (IOException e) {
            // Rollback both transactions on error
            throw new RuntimeException("Cannot complete task", e);
        }
    }
}