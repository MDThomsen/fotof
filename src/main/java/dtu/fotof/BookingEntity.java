package dtu.fotof;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;

@Entity
public class BookingEntity implements Serializable {

    private static  final long serialVersionUID = 1L;

    protected String customerName;
    protected String shippingAddress;
    protected String email;
    protected String location;
    protected String locationAddress;
    protected String specialRequest;
    protected String assignedPhotographer;
    protected boolean photoshootDone;
    protected boolean picturesPrepared;
    protected boolean specialRequestDone;
    protected boolean packaged;
    protected boolean delivered;

    @Id
    @GeneratedValue
    protected Long id;

    @Version
    protected long version;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getAssignedPhotographer() {
        return assignedPhotographer;
    }

    public void setAssignedPhotographer(String assignedPhotographer) {
        this.assignedPhotographer = assignedPhotographer;
    }

    public boolean isPhotoshootDone() {
        return photoshootDone;
    }

    public void setPhotoshootDone(boolean photoshootDone) {
        this.photoshootDone = photoshootDone;
    }

    public boolean isPicturesPrepared() {
        return picturesPrepared;
    }

    public void setPicturesPrepared(boolean picturesPrepared) {
        this.picturesPrepared = picturesPrepared;
    }

    public boolean isSpecialRequestDone() {
        return specialRequestDone;
    }

    public void setSpecialRequestDone(boolean specialRequestDone) {
        this.specialRequestDone = specialRequestDone;
    }

    public boolean isPackaged() {
        return packaged;
    }

    public void setPackaged(boolean packaged) {
        this.packaged = packaged;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}