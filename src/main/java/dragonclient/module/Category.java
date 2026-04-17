package dragonclient.module;

public enum Category {

    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    MISC("Misc");

    public String mname;
    public int i;
    Category(String mname){
        this.mname = mname;
    }
}