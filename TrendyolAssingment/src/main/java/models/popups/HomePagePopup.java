package models.popups;

import models.components.WebComponent;

public class HomePagePopup extends Popup {
    public HomePagePopup(WebComponent opener){
        this.opener = opener;
        this.browser = opener.browser();
        this.className = "homepage-popup";
    }
}
