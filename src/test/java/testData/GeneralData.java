package testData;

import java.util.ArrayList;
import java.util.List;

public class GeneralData {

    // Authentication Data
    public static final String USER_EMAIL = "www-automation@zinio.com";
    public static final String USER_PASSWORD = "123456789";

    // Data
    public static final String US_NEWSSTAND_ID = "101";
    public static final String AU_NEWSSTAND_ID = "121";
    public static final String PUBLICATION_SPECIAL = "8378";

    // Data
    public static final String CAMPAIGN_PUBLIC = "www-auto-public";
    public static final String CAMPAIGN_PRIVATE = "www-auto-private";

    // Price
    public static String standardPrice = "5";
    public static String backPrice = "4";
    public static String specialPrice = "6";
    public static String subscriptionPrice = "10";
    public static int issueQuantity = 10;

    // Cache-Refresh Handle
    public static List<String> alreadyCacheRefresh = new ArrayList<>();
}