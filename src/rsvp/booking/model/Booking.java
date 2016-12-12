package rsvp.booking.model;

import rsvp.user.model.User;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_ID")
    private User owner;

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

    public void setOwner(User owner) { this.owner = owner; }

    public User getOwner() { return this.owner; }


    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public Long getRoomId() { return this.roomId; }

    public boolean isNewRecord() { return this.newRecord; }

    public void markAsNewRecord(boolean newRecord) { this.newRecord = newRecord; }
}
