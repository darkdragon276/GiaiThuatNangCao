package src;

public class JsonAddressObject {
    private String text;

    private Result result;

    public JsonAddressObject(Result result) {
        this.result = result;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Result getAddress() {
        return result;
    }

    public void setAddress(Result result) {
        this.result = result;
    }
}
