package models.visitors.buyer;

import base.TestContext;

public class BuyerPool {
    private BuyerPool() {}

    public static Buyer with(String email, String password){
        Buyer buyer = new Buyer(email, password);
        TestContext.get().addVisitor(buyer);
        return buyer;
    }

    public static Buyer anonymous(){
        return with("", "");
    }
}
