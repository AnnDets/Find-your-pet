package models;
import java.util.ArrayList;

public class Report {
    private int id;
    private User user;
    private ArrayList<String> colors;
    private ArrayList<String> specialMarks;
    private String breed;
    private String description;
    private String foundDate;
    private String location;
    private String status;

    public Report(int id, User user, ArrayList<String> colors, ArrayList<String> specialMarks, String breed, String description, String foundDate, String location, String status) {
        this.id = id;
        this.user = user;
        this.colors = colors;
        this.specialMarks = specialMarks;
        this.breed = breed;
        this.description = description;
        this.foundDate = foundDate;
        this.location = location;
        this.status = status;
    }

    // Геттеры и сеттеры для всех полей
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ArrayList<String> getColors() { return colors; }
    public void setColors(ArrayList<String> colors) { this.colors = colors; }

    public ArrayList<String> getSpecialMarks() { return specialMarks; }
    public void setSpecialMarks(ArrayList<String> specialMarks) { this.specialMarks = specialMarks; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFoundDate() { return foundDate; }
    public void setFoundDate(String foundDate) { this.foundDate = foundDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
