package dtu.fotof;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import javax.inject.Inject;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Map;

@Stateless
@Named
public class OrderBusinessLogic {
    @Inject
    private TaskForm taskForm;

    // Inject the entity manager
    @PersistenceContext
    private EntityManager entityManager;

    public void persistOrder(DelegateExecution delegateExecution) {
        // Create new order instance
        OrderEntity orderEntity = new OrderEntity();

        // Get all process variables
        Map<String, Object> variables = delegateExecution.getVariables();

        // Set order attributes
        orderEntity.setCustomer((String) variables.get("customer"));
        orderEntity.setAddress((String) variables.get("address"));
        orderEntity.setPizza((String) variables.get("pizza"));

    /*
      Persist order instance and flush. After the flush the
      id of the order instance is set.
    */
        entityManager.persist(orderEntity);
        entityManager.flush();

        // Remove no longer needed process variables
        delegateExecution.removeVariables(variables.keySet());

        // Add newly created order id as process variable
        delegateExecution.setVariable("orderId", orderEntity.getId());
    }

    // Inject task form available through the Camunda cdi artifact


    public OrderEntity getOrder(Long orderId) {
        // Load order entity from database
        return entityManager.find(OrderEntity.class, orderId);
    }

    /*
      Merge updated order entity and complete task form in one transaction. This ensures
      that both changes will rollback if an error occurs during transaction.
     */
    public void mergeOrderAndCompleteTask(OrderEntity orderEntity) {
        // Merge detached order entity with current persisted state
        entityManager.merge(orderEntity);
        try {
            // Complete user task from
            taskForm.completeTask();
        } catch (IOException e) {
            // Rollback both transactions on error
            throw new RuntimeException("Cannot complete task", e);
        }
    }

}