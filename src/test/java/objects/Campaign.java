package objects;

import com.google.gson.JsonObject;
import utilities.ApiUtils;

public class Campaign {
    public String campaignID;
    public String shortCode;
    public String couponID;
    public String couponName;
    public String percentString;
    public double percentNumber;
    public String publicationID;

    public Campaign(String shortCode) {
        this.shortCode = shortCode;
    }

    public Campaign getCampaignInfo() {
        ApiUtils apiUtils = new ApiUtils("/commerce/v2/campaigns?short_code=" + this.shortCode);
        this.campaignID = apiUtils.sendGET().getAsJsonArray("data").get(0).getAsJsonObject().get("id").getAsString();

        apiUtils = new ApiUtils("/commerce/v2/campaigns/" + this.campaignID + "/targets");
        JsonObject jsonObject = apiUtils.sendGET().getAsJsonArray("data").get(0).getAsJsonObject();
        this.publicationID = jsonObject.get("object_id").getAsString();
        this.couponID = jsonObject.get("coupon_id").getAsString();

        apiUtils = new ApiUtils("/commerce/v2/campaigns/" + this.campaignID + "/coupons/" + this.couponID);
        jsonObject = apiUtils.sendGET().getAsJsonObject("data");
        this.couponName = jsonObject.get("name").getAsString();
        this.percentNumber = jsonObject.get("amount_one").getAsDouble();
        this.percentString = Integer.toString((int) (this.percentNumber * 100)) + "%";

        return this;
    }
}