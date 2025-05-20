package main;

public class VolunteerProgram {
    private String name;
    private String date; // yyyy-MM-dd
    private String location;
    private String category;
    private int maxParticipants;
    private int hours;

    public VolunteerProgram(String name, String date, String location, String category, int maxParticipants, int hours) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.category = category;
        this.maxParticipants = maxParticipants;
        this.hours = hours;
    }

    public String getName() { return name; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public int getMaxParticipants() { return maxParticipants; }
    public int getHours() { return hours; }
}
