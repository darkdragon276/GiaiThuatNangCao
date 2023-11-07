package src;

public class Result {
    private String[] text_preprocessed;
    private String province;
    private String district;
    private String ward;

    public Result(String[] text_preprocessed, String province, String district, String ward) {
        this.text_preprocessed = text_preprocessed;
        this.province = province;
        this.district = district;
        this.ward = ward;
    }

    public Result(String province, String district, String ward) {
        this.province = province;
        this.district = district;
        this.ward = ward;
    }

    public String[] getText_preprocessed() {
        return text_preprocessed;
    }

    public void setText_preprocessed(String[] text_preprocessed) {
        this.text_preprocessed = text_preprocessed;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}
