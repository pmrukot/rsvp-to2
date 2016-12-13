package rsvp.booking.model;

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

    @Column(name = "userId")
    private Long userId;

    @Column(name = "roomId")
    private Long roomId;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_participants", joinColumns=@JoinColumn(name="booking_id"), inverseJoinColumns=@JoinColumn(name="login"))
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

    public void setUserId(Long userId) { this.userId = userId; }

    public Long getUserId() { return this.userId; }

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
