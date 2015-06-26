/**
 * Title: Acer Internal Project
 * Copyright: (c) 2015, Acer Inc.
 * Name: Credit
 *
 * @author Oscar Wei
 * @since 2015/3/14
 * <p>
 * H i s t o r y
 * <p>
 * 2015/3/14 Oscar Wei v1
 * + File created
 */
package tw.com.oscar.orm.hibernate.domain;

import com.google.common.base.MoreObjects;
import tw.com.oscar.orm.hibernate.domain.enums.CreditCardType;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * <strong>Description:</strong><br>
 * This function include: - TODO <br>
 *
 * @author Oscar Wei
 * @version v1, 2015/3/14
 * @since 2015/3/14
 */
@Entity
@Table(name = "CREDIT")
public class Credit extends NamedEntity {

    private String description;
    private CreditCardType creditCardType = CreditCardType.VISA;

    private Account account;

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "DESCRIPTION", nullable = false, length = 50)
    @Size(max = 50)
    public String getDescription() {
        return description;
    }

    @Column(name = "CREDIT_CARD_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    @OneToOne(mappedBy = "credit")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credit that = (Credit) o;

        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return description != null ? description.hashCode() : 0;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("description", description)
                .add("creditCardType", creditCardType)
                .toString();
    }
}
