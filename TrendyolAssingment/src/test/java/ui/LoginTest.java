package ui;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import base.BaseUITest;
import models.visitors.buyer.Buyer;
import models.visitors.buyer.BuyerPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import models.pages.LoginPage;

import static models.visitors.buyer.Buyer.shouldSeePropperLoggingStatus;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class LoginTest extends BaseUITest {

    @Test
    @FileParameters("src/main/resources/loginCredentials.csv")
    public void shouldLoginWithMultipleCredentialsAsParameterized(String caseDescription, String email, String password, boolean isSuccess, String errorMessage) {
        Buyer buyer = (Buyer) BuyerPool
                .with(email, password)
                .open(new LoginPage())
                .login();

        assertThat(caseDescription, buyer, shouldSeePropperLoggingStatus(isSuccess, errorMessage));
    }
}
