package rsvp.user.model;

import org.omg.PortableInterceptor.LOCATION_FORWARD;

import java.time.LocalDateTime;

public class TimeSlot {

    private LocalDateTime beginning;
    private LocalDateTime end;

    public TimeSlot(LocalDateTime beginning, LocalDateTime end) {
        this.beginning = beginning;
        this.end = end;
    }

    public LocalDateTime getBeginning() {
        return beginning;
    }

    public void setBeginning(LocalDateTime beginning) {
        this.beginning = beginning;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
