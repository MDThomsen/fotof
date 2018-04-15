package dtu.fotof;

import org.camunda.bpm.engine.cdi.BusinessProcess;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ConversationScoped
public class FormController implements Serializable {

    private static  final long serialVersionUID = 1L;

    // Inject the BusinessProcess to access the process variables
    @Inject
    private BusinessProcess businessProcess;

    // Inject the EntityManager to access the persisted order
    @PersistenceContext
    private EntityManager entityManager;

    // Inject the OrderBusinessLogic to update the persisted order
    @Inject
    private BookingBusinessLogic bookingBusinessLogic;

    // Caches the OrderEntity during the conversation
    private BookingEntity bookingEntity;

    public BookingEntity getBookingEntity() {
        if (bookingEntity == null) {
            // Load the order entity from the database if not already cached
            bookingEntity = bookingBusinessLogic.getBooking((Long) businessProcess.getVariable("bookingId"));
        }
        return bookingEntity;
    }

    public void submitForm() throws IOException {
        // Persist updated order entity and complete task form
        bookingBusinessLogic.mergeOrderAndCompleteTask(bookingEntity);
    }

    public void sendMail() {
        BookingEntity bookingEntity = getBookingEntity();
        Logger logger = Logger.getLogger(BookingBusinessLogic.class.getName());
        logger.log(Level.INFO, "\n\n\nSending Email:\nDear {0}, your Fotof order {1} has been prepared.\n\n\n", new String[]{bookingEntity.getCustomerName(), String.valueOf(bookingEntity.getId())});
    }
}