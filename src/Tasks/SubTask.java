package Tasks;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, int id, Statuses status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
