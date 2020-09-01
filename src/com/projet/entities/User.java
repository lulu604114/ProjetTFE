package com.projet.entities;

import com.projet.enumeration.Language;
import com.projet.enumeration.UserStatus;
import com.projet.enumeration.UserTitle;
import com.projet.security.SecurityManager;
import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * =================================================================
 * Created by Intellij IDEA.
 *
 * @author lucas
 * @project TFE
 * Date: 27/01/2020
 * Time: 19:28
 * =================================================================
 */
@Entity
@Table(name = "Users", schema = "jsf_tfe")
@NamedQueries({
        @NamedQuery(name = "User.findUserByUsername", query = "SELECT u FROM User u WHERE u.username=:username"),
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findUserByEmail", query = "SELECT u FROM User u WHERE u.email=:email")
})
public class User implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(columnDefinition = "boolean default 1", name = "active")
    private boolean active;

    @Column(name = "first_name")
    @NotEmpty
    @Size(min = 1, max = 250)
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    @Size(min = 1, max = 250)
    private String lastName;

    @Column(name = "inami_number")
    @Nullable
    @Size(min = 8, max = 12)
    private String inamiNumber;

    @Column(name = "iban")
    @Nullable
    @Size(min = 16, max = 16)
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}(?:[ ]?[0-9]{4}){4}(?:[ ]?[0-9]{1,2})?$")
    private String iban;

    @Column(name = "password")
    @NotEmpty
    @Size(min = 1, max = 100)
    private String password;

    @Column(name = "email")
    @NotEmpty
    @Size(min = 6, max = 120)
    @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;

    @Column(name = "username")
    @NotEmpty
    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "phone")
    @Nullable
    @Size(min = 10, max = 13)
    private String phone;

    @Column(name = "mobile")
    @Nullable
    @Size(min = 10, max = 13)
    private String mobile;

    @Column(name = "tva")
    @Nullable
    @Size(min = 4, max = 50)
    private String tva;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    @Nullable
    private Date birthdate;

    @Column(name = "charge_config_set")
    private boolean chargeConfigSet;

    @Column(columnDefinition = "varchar(255) default 'avatar.svg'", name = "avatar")
    private String avatar;


    // ENUMERATION
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'NONE'", name = "title")
    private UserTitle title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(2) default 'FR'", name = "language")
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'NONE'", name = "status")
    private UserStatus status;

    // OneToMany
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Charge> charges;
    @OneToMany(mappedBy = "user")
    private List<Patient> patients;
    @OneToMany(mappedBy = "user")
    private List<Connection> connections;
    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Dashboard> dashboards;
    @OneToMany(mappedBy = "user")
    private List<Document> documents;
    @OneToMany(mappedBy = "user")
    private List<Place> places;
    @OneToMany(mappedBy = "user")
    private List<ToDo> toDos;
    @OneToMany(mappedBy = "user")
    private List<AccountCategory> accountCategories;
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<Diary> diaries;
    @OneToMany(mappedBy = "user")
    private List<FinancialYear> financialYears;
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<UserAccount> userAccounts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<UserSupplier> userSuppliers;

    // ManyToOne
    @ManyToOne
    @JoinColumn(name = "role", referencedColumnName = "id", nullable = false)
    private Role role;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserTitle getTitle() {
        return title;
    }

    public void setTitle(UserTitle title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInamiNumber() {
        return inamiNumber;
    }

    public void setInamiNumber(String inamiNumber) {
        this.inamiNumber = inamiNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = SecurityManager.encryptPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getTva() {
        return tva;
    }

    public void setTva(String tva) {
        this.tva = tva;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isChargeConfigSet() {
        return chargeConfigSet;
    }

    public void setChargeConfigSet(boolean chargeConfigSet) {
        this.chargeConfigSet = chargeConfigSet;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<Charge> getCharges() {
        return charges;
    }

    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Charge addCharge(Charge charge) {
        if (getCharges() == null)
            setCharges(new ArrayList<>());

        getCharges().add(charge);
        charge.setUser(this);

        return charge;
    }

    public Charge removeCharge(Charge charge) {
        getCharges().remove(charge);
        charge.setUser(null);

        return charge;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Dashboard> getDashboards() {
        return dashboards;
    }

    public void setDashboards(List<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }

    public Dashboard addDashboard(Dashboard dashboard) {
        if (this.getDashboards() == null)
            setDashboards(new ArrayList<>());

        this.getDashboards().add(dashboard);
        dashboard.setUser(this);

        return dashboard;
    }

    public Dashboard removeDashboard(Dashboard dashboard) {
        getDashboards().remove(dashboard);
        dashboard.setUser(null);

        return dashboard;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ToDo> getToDos() {
        return toDos;
    }

    public void setToDos(List<ToDo> toDos) {
        this.toDos = toDos;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public List<AccountCategory> getAccountCategories() {
        return accountCategories;
    }

    public void setAccountCategories(List<AccountCategory> accountCategories) {
        this.accountCategories = accountCategories;
    }

    public List<Diary> getDiaries() {
        return diaries;
    }

    public void setDiaries(List<Diary> diaries) {
        this.diaries = diaries;
    }

    public Diary addDiary(Diary diary) {
        if (getDiaries() == null)
            setDiaries(new ArrayList<>());

        getDiaries().add(diary);
        diary.setUser(this);

        return diary;
    }

    public List<FinancialYear> getFinancialYears() {
        return financialYears;
    }

    public void setFinancialYears(List<FinancialYear> financialYears) {
        this.financialYears = financialYears;
    }

    public FinancialYear addFinancialYear(FinancialYear financialYear) {
        if (getFinancialYears() == null)
            setFinancialYears(new ArrayList<>());

        getFinancialYears().add(financialYear);
        financialYear.setUser(this);

        return financialYear;
    }

    public FinancialYear removeFinancialYear(FinancialYear financialYear) {
        getFinancialYears().remove(financialYear);
        financialYear.setUser(null);

        return financialYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                active == user.active &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(inamiNumber, user.inamiNumber) &&
                Objects.equals(iban, user.iban) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(username, user.username) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(birthdate, user.birthdate) &&
                Objects.equals(tva, user.tva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, inamiNumber, iban, password, email, username, phone, birthdate, tva, active);
    }

    @Override
    public User clone() {

        User user = null;

        try {
            user = (User) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace(System.err);
        }

        return user;
    }

    /**
     * setUserFields
     * @param user
     */
    public void setFields(User user) {
        this.title = user.title;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.inamiNumber = user.inamiNumber;
        this.iban = user.iban;
        this.password = user.password;
        this.email = user.email;
        this.username = user.username;
        this.phone = user.phone;
        this.birthdate = user.birthdate;
        this.tva = user.tva;
        this.active = user.active;
        this.mobile = user.mobile;
        this.status = user.status;
        this.language = user.language;
    }

    public List<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(List<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    public List<UserSupplier> getUserSuppliers() {
        return userSuppliers;
    }

    public void setUserSuppliers(List<UserSupplier> userSuppliers) {
        this.userSuppliers = userSuppliers;
    }
}
