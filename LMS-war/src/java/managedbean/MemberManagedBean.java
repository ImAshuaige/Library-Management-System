/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.MemberEntity;
import error.NonUniqueIdException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.NotNull;
import session.MemberSessionBeanLocal;

@Named(value = "memberManagedBean")
@RequestScoped
public class MemberManagedBean {

    @EJB
    private MemberSessionBeanLocal memberSessionBeanLocal;
    @NotNull(message = "First Name is required")
    private String firstName;
    @NotNull(message = "Last Name is required")
    private String lastName;
    @NotNull(message = "Gender is required")
    private Character gender;
    @NotNull(message = "Age is required")
    private Integer age;
    @NotNull(message = "Identity number is required")
    private String identityNo;
    @NotNull(message = "Phone is required")
    private String phone;
    @NotNull(message = "Address is required")
    private String address;

    private Integer numOfMembers;
    private List<MemberEntity> members;

    public MemberManagedBean() {
    }

    public void registerMember() {
        try {
            memberSessionBeanLocal.checkUniqueId(identityNo);
        } catch (NonUniqueIdException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Member with the identity number already exists."));
            //return"registerMember.xhtml";            
        }
        MemberEntity m = new MemberEntity();
        m.setAddress(getAddress());
        m.setAge(getAge());
        m.setFirstName(getFirstName());
        m.setGender(getGender());
        m.setLastName(getLastName());
        m.setIdentityNo(getIdentityNo());
        m.setPhone(getPhone());
        memberSessionBeanLocal.createNewMember(m);
        address = null;
        age = null;
        firstName = null;
        gender = null;
        lastName = null;
        identityNo = null;
        phone = null;
        FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice",
                "The Member is Successfully Created."));
        //return "registerMember.xhtml?faces-redirect=true";
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

    /**
     * @return the gender
     */
    public Character getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Character gender) {
        this.gender = gender;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the identityNo
     */
    public String getIdentityNo() {
        return identityNo;
    }

    /**
     * @param identityNo the identityNo to set
     */
    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the numOfMembers
     */
    public Integer getNumOfMembers() {
        return memberSessionBeanLocal.retrieveAllMembers().size();
    }

    /**
     * @param numOfMembers the numOfMembers to set
     */
    public void setNumOfMembers(Integer numOfMembers) {
        this.numOfMembers = numOfMembers;
    }

    /**
     * @return the members
     */
    public List<MemberEntity> getMembers() {
        return memberSessionBeanLocal.retrieveAllMembers();
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<MemberEntity> members) {
        this.members = members;
    }

}
