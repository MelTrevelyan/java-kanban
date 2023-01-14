package tasks;

import java.time.LocalDateTime;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, int id, Status status, long minutes, LocalDateTime startTime,
                   int epicId) {
        super(name, description, id, status, minutes, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
