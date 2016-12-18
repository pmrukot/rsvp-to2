package rsvp.booking.model;

import rsvp.resources.model.TimeSlot;
import rsvp.user.model.User;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FIRST_SLOT_ID")
    private TimeSlot firstSlot;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LAST_SLOT_ID")
    private TimeSlot lastSlot;

    @Column(name = "roomId")
    private Long roomId;

    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="user_participants", joinColumns=@JoinColumn(name="booking_id"), inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<User> participants = new HashSet<User>(0);

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

    public void setFirstSlot(TimeSlot firstSlot) {
        this.firstSlot = firstSlot;
    }

    public TimeSlot getFirstSlot() {
        return this.firstSlot;
    }

    public void setLastSlot(TimeSlot lastSlot) {
        this.lastSlot = lastSlot;
    }

    public TimeSlot getLastSlot() {
        return this.lastSlot;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public Long getRoomId() { return this.roomId; }

    public Set<User> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public boolean isNewRecord() { return this.newRecord; }

    public void markAsNewRecord(boolean newRecord) { this.newRecord = newRecord; }
}
