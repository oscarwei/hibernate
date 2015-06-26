/**
 *
 * Title: Acer Internal Project
 * Copyright: (c) 2015, Acer Inc.
 * Name: AccountSummary
 *
 * @author Oscar Wei
 * @since 2015/3/15
 *
 * H i s t o r y
 *
 * 2015/3/15 Oscar Wei v1
 * + File created 
 */
package tw.com.oscar.orm.hibernate.domain;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * <strong>Description:</strong><br>
 * This function include: - TODO <br>
 *
 * @author Oscar Wei
 * @version v1, 2015/3/15
 * @since 2015/3/15
 */
@Entity
@Subselect("SELECT account.pid AS pid, account.first_name AS firstName, max(account.salary) AS " +
        "salary FROM Account account GROUP BY account.first_name")
@Synchronize({ "Account" })
public class AccountSummary {

    private int pid;
    private String firstName;
    private BigDecimal salary;

    @Id
    // @GeneratedValue
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Column
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
