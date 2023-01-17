package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> subTaskIds = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(String name, String description, int id, Status status, Duration duration, LocalDateTime startTime) {
        super(name, description, id, status, duration, startTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void addSubTaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    public void removeTaskId(Object subTaskId) {
        subTaskIds.remove(subTaskId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIds, epic.subTaskIds) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskIds, endTime);
    }
}
