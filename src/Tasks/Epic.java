package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description, int id, Statuses status) {
        super(name, description, id, status);
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
