package models.visitors.buyer;

import models.visitors.Visitor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;

public class Buyer extends Visitor<Buyer> {
    public Buyer(String email, String password) {
        super(email, password);
    }

    public static <P extends Buyer> Matcher<P> shouldSeePropperLoggingStatus(boolean isSuccess, String errorMessage) {
        return new BaseMatcher<P>() {
            Buyer buyer;
            boolean isErrorMsgPresent = false;

            @Override
            public boolean matches(Object item) {
                buyer = (Buyer) item;
                if (isSuccess) {
                    return buyer.isOnline();
                } else {
                    isErrorMsgPresent = buyer.browser().isElementPresent(3, By.xpath("//*[text()='" + errorMessage + "']"));
                    return !buyer.isOnline() && isErrorMsgPresent;
                }

            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText(getExpectedMsg(isSuccess))
                        .appendText("\n" + buyer.getEmail())
                        .appendText("\n" + buyer.getPassword());
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendValue(getActualMsg(isSuccess));
            }

            public String getExpectedMsg(boolean isSuccess) {
                if (isSuccess) {
                    return "Buyer should be logged in to Trendyol with:";
                } else {
                    if (isErrorMsgPresent) {
                        return "Buyer should NOT be logged in to Trendyol with:";
                    } else {
                        return "Buyer should see error message on the page as '" + errorMessage + "'";
                    }
                }
            }

            public String getActualMsg(boolean isSuccess) {
                if (isSuccess) {
                    return "But it could NOT be logged in";
                } else {
                    if (isErrorMsgPresent) {
                        return "But it could be logged in";
                    } else {
                        return "But it could NOT be displayed on the screen";
                    }
                }
            }
        };
    }
}
