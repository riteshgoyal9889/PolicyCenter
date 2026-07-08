package registerpeople;

public enum  Jurisdiction {
    IL("Illinois"),
    IN("Indiana"),
    MN("Minnesota");

    private final String fullName;

    Jurisdiction(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}

