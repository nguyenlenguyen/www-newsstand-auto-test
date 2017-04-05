package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import core.Browser;
import utilities.ApiUtils;

public class API {
    public static void setNewsstandInfo(String newsstandID, String property, String value) {
        ApiUtils apiUtils = new ApiUtils("/newsstand/v2/newsstands/" + newsstandID);
        apiUtils.addParam("remote_identifier", 1);
        apiUtils.addParam(property, value);
        apiUtils.sendPUT();
    }

    public static void setPublicationInfo(String publicationID, String property, String value) {
        ApiUtils apiUtils = new ApiUtils("/catalog/v1/publications/" + publicationID);
        apiUtils.addParam(property, value);
        apiUtils.sendPUT();
    }

    public static String getPublicationInfo(String publicationID, String property) {
        ApiUtils apiUtils = new ApiUtils("/catalog/v1/publications/" + publicationID);
        JsonObject response = apiUtils.sendGET().getAsJsonObject("data");
        switch (property) {
            case "latest_issue_id":
                return response.getAsJsonObject("latest_issue").get("id").getAsString();
            case "latest_issue_name":
                return response.getAsJsonObject("latest_issue").get("name").getAsString();
            case "category_id":
                return response.getAsJsonArray("categories").get(0).getAsJsonObject().get("id").getAsString();
            case "name":
                return response.get("name").getAsString();
            default:
                return null;
        }
    }

    private static String addSubscriptionProductID(String publicationID, int credits) {
        ApiUtils apiUtils = new ApiUtils("/commerce/v2/products");
        apiUtils.addParam("type", 2);
        apiUtils.addParam("publication_id", publicationID);
        apiUtils.addParam("credits", credits);
        return apiUtils.sendPOST().getAsJsonObject("data").get("id").getAsString();
    }

    private static String getSubscriptionProductID(String publicationID, int credits) {
        ApiUtils apiUtils = new ApiUtils("/commerce/v2/products?publication_id=" + publicationID + "&type=2");
        JsonArray response = apiUtils.sendGET().getAsJsonArray("data");
        for (JsonElement jsonElement : response) {
            if (credits == 0 || jsonElement.getAsJsonObject().get("credits").getAsInt() == credits) {
                return jsonElement.getAsJsonObject().get("id").getAsString();
            }
        }
        return addSubscriptionProductID(publicationID, credits);
    }

    public static void setSubscriptionPrice(String publicationID, String price, int credits) {
        ApiUtils apiUtils = new ApiUtils("/commerce/v2/publications/" + publicationID + "/issue_subscription_prices");
        apiUtils.addParam("currency_code", "USD");
        apiUtils.addParam("distribution_platform", 1);
        apiUtils.addParam("product_type", 2);
        apiUtils.addParam("price", price);
        apiUtils.addParam("product_id", getSubscriptionProductID(publicationID, credits));
        apiUtils.addParam("default_product", 1);
        apiUtils.sendPOST();
    }

    public static void setSingleIssuePrice(String publicationID, String price, String issueType) {
        ApiUtils apiUtils = new ApiUtils("/commerce/v2/publications/" + publicationID + "/single_issue_prices");
        apiUtils.addParam("currency_code", "USD");
        apiUtils.addParam("distribution_platform", 1);
        apiUtils.addParam("price", price);
        switch (issueType) {
            case "standard":
                apiUtils.addParam("issue_type", 0);
                break;
            case "special":
                apiUtils.addParam("issue_type", 1);
                break;
            default:
                apiUtils.addParam("issue_type", 2);
                break;
        }
        apiUtils.sendPOST();
    }

    public static boolean hasSubscriptionPrice(String publicationID, int credits) {
        String productID = getSubscriptionProductID(publicationID, credits);
        ApiUtils apiUtils = new ApiUtils("/commerce/v2/publications/" + publicationID
                + "/issue_subscription_prices?product_id=" + productID);
        return apiUtils.sendGET().getAsJsonArray("data").size() > 0;
    }

    public static boolean hasSingleIssuePrice(String publicationID, String issueType) {
        String type;
        switch (issueType) {
            case "standard":
                type = "0";
                break;
            case "special":
                type = "1";
                break;
            default:
                type = "2";
                break;
        }
        ApiUtils apiUtils = new ApiUtils("/commerce/v2/publications/" + publicationID
                + "/single_issue_prices?issue_type=" + type);
        return apiUtils.sendGET().getAsJsonArray("data").size() > 0;
    }

    public static void addCountryRestriction(String publicationID, String countryCode) {
        ApiUtils apiUtils = new ApiUtils("/catalog/v1/country_restrictions");
        apiUtils.addParam("object_type", 3);
        apiUtils.addParam("object_id", publicationID);
        apiUtils.addParam("country_code", countryCode);
        apiUtils.sendPOST();
    }

    public static void removeCountryRestriction(String publicationID, String countryCode) {
        try {
            ApiUtils apiUtils = new ApiUtils("/catalog/v1/country_restrictions?object_id=" + publicationID
                    + "&country_code=" + countryCode);
            String id = apiUtils.sendGET().getAsJsonArray("data").get(0).getAsJsonObject().get("id").getAsString();
            new ApiUtils("/catalog/v1/country_restrictions/" + id).sendDELETE();
        } catch (Exception ex) {
            // if no restriction found then do nothing
        }
    }

    public static void setCampaignTarget(String campaignID, String publicationID, String couponID, int priority) {
        String productID = getSubscriptionProductID(publicationID, 0);
        ApiUtils apiUtils = new ApiUtils("/commerce/v2/campaigns/" + campaignID + "/targets");
        apiUtils.addParam("object_id", publicationID);
        apiUtils.addParam("product_id", productID);
        apiUtils.addParam("product_type", 2);
        apiUtils.addParam("auto_renew_product_id", productID);
        apiUtils.addParam("coupon_id", couponID);
        apiUtils.addParam("priority", priority);
        apiUtils.sendPOST();
    }

    public static String getUserID(String email) {
        ApiUtils apiUtils = new ApiUtils("/identity/v1/users?email=" + email);
        return apiUtils.sendGET().getAsJsonArray("data").get(0).getAsJsonObject().get("id").getAsString();
    }

    public static void waitItemInLibrary(String userID, String... issueIDList) {
        for (String issueID : issueIDList) {
            int tryCount = 0;
            ApiUtils apiUtils = new ApiUtils("/entitlement/v2/issue_entitlements?user_id=" + userID + "&issue_id=" + issueID);
            while (tryCount <= 5 && apiUtils.sendGET().getAsJsonArray("data").size() == 0) {
                Browser.sleep(1000);
                tryCount++;
            }
        }
    }

    public static String createNewUser(String email, String password) {
        ApiUtils apiUtils = new ApiUtils("/identity/v1/users");
        apiUtils.addParam("directory_id", 25);
        apiUtils.addParam("email", email);
        apiUtils.addParam("password", password);
        return apiUtils.sendPOST().getAsJsonObject("data").get("id").getAsString();
    }
}