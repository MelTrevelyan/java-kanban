package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, int id, Status status, Duration duration, LocalDateTime startTime,
                   int epicId) {
        super(name, description, id, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
