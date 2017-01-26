package rsvp.booking.model;

import rsvp.resources.model.TimeSlot;
import rsvp.resources.model.UniversityRoom;
import rsvp.user.model.User;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Booking")
public class Booking {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "root_id")
    private long rootId;

    @Column(name = "description")
    private String description;

    @Column(name = "reservation_date")
    private Date reservationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "first_slot_id")
    private TimeSlot firstSlot;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "last_slot_id")
    private TimeSlot lastSlot;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "universityroom_id")
    private UniversityRoom universityRoom;

    @ManyToMany(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.EAGER)
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

    public long getRootId() {
        return rootId;
    }

    public void setRootId(long rootId) {
        this.rootId = rootId;
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

    public void setUniversityRoom(UniversityRoom universityRoom) { this.universityRoom = universityRoom; }

    public UniversityRoom getUniversityRoom() { return this.universityRoom; }

    public Set<User> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public boolean isNewRecord() { return this.newRecord; }

    public void markAsNewRecord(boolean newRecord) { this.newRecord = newRecord; }

    public LocalTime getStartTime() {
        if (getFirstSlot() == null) {
            return LocalTime.MIN;
        }
        return getFirstSlot().getStartTime();
    }

    public LocalTime getEndTime() {
        if (getLastSlot() == null) {
            return LocalTime.MAX;
        }
        return getLastSlot().getEndTime();
    }

    public boolean isValid() {
        return getEndTime().compareTo(getStartTime()) >= 0;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
