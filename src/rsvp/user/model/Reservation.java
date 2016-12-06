package rsvp.user.model;


import rsvp.resources.model.UniversityRoom;

public class Reservation {
    private User user;
    private TimeSlot timeSlot;
    private UniversityRoom room;

    public Reservation(User user, TimeSlot timeSlot, UniversityRoom room) {
        this.user = user;
        this.timeSlot = timeSlot;
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public UniversityRoom getRoom() {
        return room;
    }

    public void setRoom(UniversityRoom room) {
        this.room = room;
    }
}
