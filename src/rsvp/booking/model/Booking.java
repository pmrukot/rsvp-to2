package booking.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Booking")
public class Booking {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "reservationDate")
    private Date reservationDate;

    public Booking() {}

    public Booking(Date reservationDate) {
        setReservationDate(reservationDate);
    }

    public long getId() {
        return id;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

}
