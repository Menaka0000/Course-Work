package model.tm;

public class ItemTm {
    private String Id;
    private String  Description;

    public ItemTm() {
    }

    public ItemTm(String id, String description) {
        setId(id);
        setDescription(description);
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
