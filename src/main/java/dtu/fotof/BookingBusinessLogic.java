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
import java.util.logging.Logger;

@Stateless
@Named
public class BookingBusinessLogic {
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
        bookingEntity.setCustomerName((String) variables.get("customerName"));
        bookingEntity.setShippingAddress((String) variables.get("shippingAddress"));
        bookingEntity.setEmail((String) variables.get("email"));
        bookingEntity.setLocation((String) variables.get("location"));
        bookingEntity.setLocationAddress((String) variables.get("locationAddress"));
        bookingEntity.setSpecialRequest((String) variables.get("specialRequest"));

        assignPhotographer(bookingEntity);
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

    private void assignPhotographer(BookingEntity bookingEntity) {
        Random rand = new Random();
        double n = rand.nextDouble();

        if(n < 0.5)
            bookingEntity.setAssignedPhotographer("Jean-Claude");
        else
            bookingEntity.setAssignedPhotographer("Jørgen");
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