package objects;

import com.google.gson.JsonObject;
import testData.GeneralData;
import utilities.ApiUtils;

public class Publication {
    public String id;
    public String name;
    public String issueID;
    public String issueName;
    public String subscriptionCredits;
    public String newsstandID = GeneralData.US_NEWSSTAND_ID;

    public Publication(String id) {
        this.id = id;
    }

    public Publication(String id, String name, String issueName) {
        this.id = id;
        this.name = name;
        this.issueName = issueName;
    }

    public Publication getAllInfo() {
        ApiUtils apiUtils = new ApiUtils("/newsstand/v2/newsstands/" + this.newsstandID + "/publications/" + this.id);
        JsonObject response = apiUtils.sendGET().getAsJsonObject("data");
        this.issueID = response.getAsJsonObject("latest_issue").get("id").getAsString();
        this.issueName = response.getAsJsonObject("latest_issue").get("name").getAsString();
        this.name = response.get("name").getAsString();
        this.subscriptionCredits = response.getAsJsonArray("subscriptions").get(0).getAsJsonObject()
                .getAsJsonObject("product").get("credits").getAsString();
        return this;
    }
}