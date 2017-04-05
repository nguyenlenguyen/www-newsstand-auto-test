package testData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import objects.Campaign;
import org.testng.annotations.Test;
import api.API;
import utilities.ApiUtils;
import utilities.ReportUtils;
import utilities.StringUtils;

import java.util.Arrays;

public class SetData {
    public static String[] getPublicationList(String newsstandID, int quantity) {
        String publicationAPI = "/newsstand/v1/newsstands/" + newsstandID + "/publications";
        String issueAPI = "/newsstand/v2/newsstands/" + newsstandID + "/issues/";
        String[] finalList = {};
        int pageCount = 1;

        while (true) {
            ApiUtils apiUtils = new ApiUtils(publicationAPI + "?page=" + pageCount);
            for (JsonElement jsonElement : apiUtils.sendGET().getAsJsonArray("data")) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String pubID = jsonObject.get("id").getAsString();
                String pubName = jsonObject.get("name").getAsString();
                if (!pubID.equals(GeneralData.PUBLICATION_SPECIAL) && !pubName.contains("'")) {
                    String issueID = jsonObject.getAsJsonObject("latest_issue").get("id").getAsString();
                    if (new ApiUtils(issueAPI + issueID).sendGET() != null) {
                        API.removeCountryRestriction(pubID, "US");
                        finalList = StringUtils.addStringToArray(finalList, pubID);
                    }
                }
                if (finalList.length == quantity) {
                    ReportUtils.logAll("\nUse these publications from newsstand [" + newsstandID + "]: "
                            + Arrays.toString(finalList) + "\n");
                    return finalList;
                }
            }
            pageCount++;
        }
    }

    public static void SetPricesFromList(String[] publicationList) {
        for (String pubID : publicationList) {
            if (!API.hasSingleIssuePrice(pubID, "standard"))
                API.setSingleIssuePrice(pubID, GeneralData.standardPrice, "standard");
            if (!API.hasSingleIssuePrice(pubID, "back"))
                API.setSingleIssuePrice(pubID, GeneralData.backPrice, "back");
            if (!API.hasSubscriptionPrice(pubID, GeneralData.issueQuantity))
                API.setSubscriptionPrice(pubID, GeneralData.subscriptionPrice, GeneralData.issueQuantity);
        }
    }

    public static void SetPricesFromListForce(String[] publicationList) {
        for (String pubID : publicationList) {
            API.setSingleIssuePrice(pubID, GeneralData.standardPrice, "standard");
            API.setSingleIssuePrice(pubID, GeneralData.backPrice, "back");
            API.setSingleIssuePrice(pubID, GeneralData.specialPrice, "special");
            API.setSubscriptionPrice(pubID, GeneralData.subscriptionPrice, GeneralData.issueQuantity);
        }
    }

    @Test
    public void SetPricesForAutomationTest() {
        SetPricesFromListForce(getPublicationList(GeneralData.US_NEWSSTAND_ID, 10));
        SetPricesFromListForce(new String[]{GeneralData.PUBLICATION_SPECIAL});

        Campaign campaign1 = new Campaign(GeneralData.CAMPAIGN_PUBLIC).getCampaignInfo();
        Campaign campaign2 = new Campaign(GeneralData.CAMPAIGN_PUBLIC).getCampaignInfo();
        SetPricesFromListForce(new String[]{campaign1.publicationID, campaign2.publicationID});
    }

    @Test
    public void SetPricesForManualTest() {
        String[] pubIDList = {"1043", "5698", "2133", "3159", "3766", "8378", "1180", "1304", "3169", "3858", "4360", "5881"};
        SetPricesFromListForce(pubIDList);
    }

    @Test
    public void SetCampaignTarget() {
        String campaignID = "2";
        String couponID = "2";
        int priority = 4;

        int pageCount = 3;
        for (int page = 1; page <= pageCount; page++) {
            ApiUtils apiUtils = new ApiUtils("/newsstand/v1/newsstands/" + GeneralData.US_NEWSSTAND_ID + "/publications?page=" + page);
            JsonArray jsonArray = apiUtils.sendGET().getAsJsonArray("data");
            for (JsonElement jsonElement : jsonArray) {
                String publicationID = jsonElement.getAsJsonObject().get("id").getAsString();
                API.setSubscriptionPrice(publicationID, GeneralData.subscriptionPrice, GeneralData.issueQuantity);
                API.setCampaignTarget(campaignID, publicationID, couponID, priority);
                priority++;
            }
        }
    }
}