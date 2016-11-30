package com.firstidea.android.brokerx.fragment.dummy;

import com.firstidea.android.brokerx.model.Enquiry;
import com.firstidea.android.brokerx.http.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Enquiry> BUYER_ITEMS = new ArrayList<Enquiry>();
    public static final List<List<User>> CIRCLE_ITEMS = new ArrayList<List<User>>();
    public static final List<List<User>> SELECTION_ITEMS = new ArrayList<List<User>>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Enquiry> ITEM_MAP = new HashMap<String, Enquiry>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyEnquiries(i));
        }
        List<User> pendingRequests = new ArrayList<>();
        List<User> myCircle = new ArrayList<>();
        for(int i=0; i<2;i++) {
            User user = craeteDummyUser(i);
            user.setStatus("P");
            pendingRequests.add(user);
        }
        for(int i=2; i<12;i++) {
            User user = craeteDummyUser(i);
            user.setStatus("A");
            myCircle.add(user);
        }
        CIRCLE_ITEMS.add(pendingRequests);
        CIRCLE_ITEMS.add(myCircle);
        SELECTION_ITEMS.add(myCircle);
    }

    private static User craeteDummyUser(int i) {
        String items = "Borker "+i+"Item 1"
        +",Borker "+i+"Item 2"
        +",Borker "+i+"Item 3"
        +",Borker "+i+"Item 4"
        +",Borker "+i+"Item 5";
        return new User("Govind Kulkarni "+i,"Pimple Saudagar "+i,((i%6)+1)+"","973065615"+i, items);
    }
    private static void addItem(Enquiry item) {
        BUYER_ITEMS.add(item);
//        ITEM_MAP.put(item.getLeadID(), item);
    }

    private static Enquiry createDummyEnquiries(int position) {
        return new Enquiry();
                /*(String.valueOf(position), "Benzene Toluene Ethyl benzene, Xylenes "+position,
                "Posted by Govind "+position,"Pimple Saudagar, Pimpri Chinchwad "+position,"You get "+(40+position)+" %",
                " "+(40+position)+" Units Available");*/
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

}
