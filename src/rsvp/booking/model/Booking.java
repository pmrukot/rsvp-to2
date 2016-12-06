package rsvp.booking.model;

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

    @Column(name = "userId")
    private Long userId;

    @Column(name = "roomId")
    private Long roomId;

    @Transient
    private boolean newRecord;

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

    public void setUserId(Long userId) { this.userId = userId; }

    public Long getUserId() { return this.userId; }


    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public Long getRoomId() { return this.roomId; }

    public boolean isNewRecord() { return this.newRecord; }

    public void markAsNewRecord(boolean newRecord) { this.newRecord = newRecord; }
}
