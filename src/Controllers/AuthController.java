package Controllers;

import Models.Staff;

public class AuthController {
    public Staff Login(String username, String password) {
        StaffController staffController = new StaffController();
        return staffController.getAuthenticateUser(username, password);
    }
}
