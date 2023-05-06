package managedbean;

import entity.Staff;
import error.InvalidLoginException;
import error.NonUniqueUsernameException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import session.StaffSessionBeanLocal;

@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;

    private String username = null;
    private String password = null;
    private String firstName;
    private String lastName;
    private Long userId;

    @PostConstruct
    public void init() {
        username = null;
        password = null;
    }

    public AuthenticationManagedBean() {
    }

    public String login() {
        try {
            staffSessionBeanLocal.retrieveStaffByLoginDetails(username, password);
            return "homePage.xhtml?faces-redirect=true";
        } catch (InvalidLoginException ex) {
            username = null;
            password = null;
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid Login", "Please Check Your Login Details or Register First."));

        }
        return "login.xhtml";
    }

    public String registerAdmin() {
        try {
            staffSessionBeanLocal.checkUniqueUsername(getUsername());
        } catch (NonUniqueUsernameException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "The Username is Already Taken."));
            return "registerAdmin.xhtml";
        }
        Staff s = new Staff();
        s.setFirstName(getFirstName());
        s.setLastName(getLastName());
        s.setUserName(getUsername());
        s.setPassword(getPassword());
        staffSessionBeanLocal.createNewStaff(s);
        return "login.xhtml?faces-redirect=true";
    }

    public String logout() {
        setUsername(null);
        password = null;
        return "/login.xhtml?faces-redirect=true";
    }

    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
