package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTaskIds = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(String name, String description, int id, Status status, long minutes, String startTime) {
        super(name, description, id, status, minutes, startTime);
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
}
