/**
 * Title: Acer Internal Project
 * Copyright: (c) 2015, Acer Inc.
 * Name: Account
 *
 * @author Oscar Wei
 * @since 2015/3/7
 * <p>
 * H i s t o r y
 * <p>
 * 2015/3/7 Oscar Wei v1
 * + File created
 */
package tw.com.oscar.orm.hibernate.domain;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.validator.constraints.Email;
import tw.com.oscar.orm.hibernate.domain.enums.Gender;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

/**
 * <strong>Description:</strong><br>
 * This function include: - A Account entity <br>
 *
 * @author Oscar Wei
 * @version v1, 2015/3/7
 * @since 2015/3/7
 */
@Entity
@Table(name = "ACCOUNT", uniqueConstraints = @UniqueConstraint(name = "UK_USERNAME", columnNames
        = { "USERNAME" }), indexes = { @Index(name = "INX_USERNAME", columnList = "USERNAME",
        unique = true), @Index(name = "INX_EMAIL", columnList = "EMAIL") })
@BatchSize(size = 5)
@DynamicInsert
@DynamicUpdate
@Where(clause = "1 = 1")
@NamedQuery(name = Account.SQL_ACCOUNT_FIND_BY_EMAIL, query = "FROM Account a WHERE a.email = :email")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Indexed
@AnalyzerDef(name = "customanalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "English")
                })
        })
public class Account extends VersionEntity {

    public static final String SQL_ACCOUNT_FIND_BY_EMAIL = "accountFindByEmail";

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Gender gender = Gender.MALE;
    private String email;
    private BigDecimal salary;
    private BigDecimal yearEndBonus;
    private Byte[] photo;

    private Set<String> telephones; // 1:N value types
    private Credit credit; // 1:1
    private Set<Role> roles = new HashSet<>(); // N:M
    private Set<Address> addressSet; // 1:N(U)
    private List<ToDo> toDoSet = new LinkedList<>(); // 1:N(B)(INX)

    public Account() {
    }

    @Column(name = "USERNAME", nullable = false, length = 50, unique = true, updatable = false)
    @NotNull
    @Size(min = 5, max = 50)
    @NaturalId(mutable = false)
    @Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "customanalyzer")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "PASSWORD", nullable = false, length = 100)
    @NotNull
    @Size(min = 8, max = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "FIRST_NAME", nullable = false, length = 30)
    @NotNull
    @Size(max = 30)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME", nullable = false, length = 30)
    @NotNull
    @Size(max = 30)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "GENDER", nullable = false, length = 1)
    @Enumerated(EnumType.ORDINAL)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Column(name = "EMAIL", nullable = false, length = 100)
    @Email
    @Size(max = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "SALARY", nullable = false, scale = 2)
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Formula("SALARY * 5.6")
    public BigDecimal getYearEndBonus() {
        return yearEndBonus;
    }

    private void setYearEndBonus(BigDecimal yearEndBonus) {
        this.yearEndBonus = yearEndBonus;
    }

    @Lob
    @Column(name = "PHOTO")
    public Byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(Byte[] photo) {
        this.photo = photo;
    }

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "TELEPHONE",
            joinColumns = @JoinColumn(name = "PID_ACCOUNT",
                    foreignKey = @ForeignKey(name = "FK_ACCOUNT_TELEPHONE"))
    )
    @Column(name = "TELEPHONE")
    public Set<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(Set<String> telephones) {
        this.telephones = telephones;
    }

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "PID_CREDIT", nullable = false, updatable = false)
    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    @ManyToMany(mappedBy = "accounts")
    @org.hibernate.annotations.ForeignKey(name = "FK_ACCOUNT_ROLE")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @Fetch(FetchMode.SELECT)
    protected Set<Role> getRoles() {
        return roles;
    }

    protected void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.getRoles().add(role);
        role.getAccounts().add(this);
    }

    public void removeRole(Role role) {
        this.getRoles().remove(role);
        role.getAccounts().remove(this);
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PID_ACCOUNT")
    @org.hibernate.annotations.ForeignKey(name = "FK_ACCOUNT_ADDRESS_PID")
    public Set<Address> getAddressSet() {
        return addressSet;
    }

    public void setAddressSet(Set<Address> addressSet) {
        this.addressSet = addressSet;
    }

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
//    @OrderBy("startDate asc")
    @OrderColumn(name = "INX_TODO")
    @IndexedEmbedded
    public List<ToDo> getToDoSet() {
        return toDoSet;
    }

    protected void setToDoSet(List<ToDo> toDoSet) {
        this.toDoSet = toDoSet;
    }

    public void addToDo(ToDo todo) {
        this.getToDoSet().add(todo);
        todo.setAccount(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (username != null ? !username.equals(account.username) : account.username != null)
            return false;
        if (password != null ? !password.equals(account.password) : account.password != null)
            return false;
        if (firstName != null ? !firstName.equals(account.firstName) : account.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(account.lastName) : account.lastName != null)
            return false;
        if (gender != account.gender) return false;
        if (email != null ? !email.equals(account.email) : account.email != null) return false;
        if (salary != null ? !salary.equals(account.salary) : account.salary != null) return false;
        if (yearEndBonus != null ? !yearEndBonus.equals(account.yearEndBonus) : account.yearEndBonus != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(photo, account.photo)) return false;
        if (telephones != null ? !telephones.equals(account.telephones) : account.telephones != null)
            return false;
        if (credit != null ? !credit.equals(account.credit) : account.credit != null) return false;
        if (roles != null ? !roles.equals(account.roles) : account.roles != null) return false;
        if (addressSet != null ? !addressSet.equals(account.addressSet) : account.addressSet != null)
            return false;
        return !(toDoSet != null ? !toDoSet.equals(account.toDoSet) : account.toDoSet != null);

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        result = 31 * result + (yearEndBonus != null ? yearEndBonus.hashCode() : 0);
        result = 31 * result + (photo != null ? Arrays.hashCode(photo) : 0);
        result = 31 * result + (telephones != null ? telephones.hashCode() : 0);
        result = 31 * result + (credit != null ? credit.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        result = 31 * result + (addressSet != null ? addressSet.hashCode() : 0);
        result = 31 * result + (toDoSet != null ? toDoSet.hashCode() : 0);
        return result;
    }
}
