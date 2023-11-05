package src;

public class JsonAddressObject {
    private String text;

    private JsonAddressNestedObject address;

    public JsonAddressObject(JsonAddressNestedObject address) {
        this.address = address;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JsonAddressNestedObject getAddress() {
        return address;
    }

    public void setAddress(JsonAddressNestedObject address) {
        this.address = address;
    }
}
