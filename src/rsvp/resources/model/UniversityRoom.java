package rsvp.resources.model;

import javax.persistence.*;

@Entity
@Table(name = "UniversityRoom")
public class UniversityRoom {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "isComputer")
    private Boolean isComputer;

    public UniversityRoom() {
    }

    public UniversityRoom(String number, Integer capacity, boolean isComputer) {
        this.number = number;
        this.capacity = capacity;
        this.isComputer = isComputer;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String toString() {
        return getNumber();
    }

    public boolean getIsComputer(){
        return isComputer;
    }

    public void setIsComputer(boolean isComputer){
        this.isComputer = isComputer;
    }
}