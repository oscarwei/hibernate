/**
 * Title: Acer Internal Project
 * Copyright: (c) 2015, Acer Inc.
 * Name: HibernateTest
 *
 * @author Oscar Wei
 * @since 2015/3/14
 * <p>
 * H i s t o r y
 * <p>
 * 2015/3/14 Oscar Wei v1
 * + File created
 */
package tw.com.oscar.orm.hibernate;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.jboss.logging.Logger;
import tw.com.oscar.orm.hibernate.domain.*;
import tw.com.oscar.orm.hibernate.util.HibernateUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <strong>Description:</strong><br>
 * This function include: - TODO <br/>
 *
 * @author Oscar Wei
 * @version v1, 2015/3/14
 * @since 2015/3/14
 */
public class HibernateTest {

    private static final Logger LOGGER = Logger.getLogger(HibernateTest.class);

    public static void main(String[] args) {

        Statistics statistics = null;
        try {
            statistics = HibernateUtil.getSessionFactory().getStatistics();
            statistics.setStatisticsEnabled(true);
            statistics.clear();

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = session.beginTransaction();
//            Long pid = testCase1(session);
            tx.commit();
            EntityStatistics roleStatic = statistics.getEntityStatistics(Role.class.getName());
            LOGGER.info("InsertCount : " + roleStatic.getInsertCount());

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
//            testCase2(session, pid);
            tx.commit();
            LOGGER.info("UpdateCount : " + roleStatic.getUpdateCount());

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            CompanyId companyId = new CompanyId("EMEA", "0001");
            Company company = (Company) session.get(Company.class, companyId);
            System.out.println("Desc : " + company.getDescription());
            tx.commit();

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            Credit credit = createCredit(session);
            tx.commit();

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            testCase3(session, credit);
            tx.commit();

            // View-to-model example
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            accountSummary(session);
            tx.commit();

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
//            formula(session);
            tx.commit();

            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
//            testCase4(session);
            tx.commit();

            // Natural-Id searching example
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            testNaturalId(session);
            tx.commit();

            // Full-Text searching example
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            testFullText(session);
            tx.commit();

            statistics.clear();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            statistics.setStatisticsEnabled(false);
//            HibernateUtil.getSessionFactory().close();
            LOGGER.info("Hibernate session is closed");
        }

    }

    /**
     * Test dynamic insert/update
     *
     * @param session a hibernate session object
     */
    private static Long testCase1(Session session) {
        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");
        role.setUserCreated("DTS");
        role.setDateCreated(new Date());
        return (Long) session.save(role);
    }

    /**
     * Test dynamic insert/update and cache
     *
     * @param session a hibernate session object
     * @param rolePid a role pid
     */
    private static void testCase2(Session session, Long rolePid) {
        Role role = (Role) session.get(Role.class, rolePid);
        role.setRoleName("ROLE_USER");
        role.setUserCreated("OW");
        role.setUserLastModified("OW");
        role.setDateLastModified(new Date());
        session.save(role); // TODO merge()???
        // TODO Open cache/immutable annotations
    }

    private static void testCase3(Session session, Credit credit) {
        Account account = new Account();
        account.setUsername("oscarwei");
        account.setPassword("12345");
        account.setFirstName("Oscar");
        account.setLastName("Wei");
        account.setEmail("oscar.wei@acer.com");
        account.setSalary(new BigDecimal(1000000L));
        account.setUserCreated("DTS");
        account.setDateCreated(new Date());

        Role admin = new Role();
        admin.setRoleName("ROLE_ADMIN");
        admin.setUserCreated("DTS");
        admin.setDateCreated(new Date());

        Role user = new Role();
        user.setRoleName("ROLE_USER");
        user.setUserCreated("DTS");
        user.setDateCreated(new Date());

        Instant instant = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        ToDo do1 = new ToDo("subject1", "Desc1", Date.from(instant));
        ToDo do2 = new ToDo("subject2", "Desc2", Date.from(instant));
        do1.setUserCreated("DTS");
        do2.setUserCreated("DTS");
        do1.setDateCreated(new Date());
        do2.setDateCreated(new Date());
        account.addToDo(do1);
        account.addToDo(do2);

//        session.save(account);
//        session.save(admin);
//        session.save(user);

        account.setCredit(credit);
        credit.setAccount(account);
        account.addRole(admin);
        account.addRole(user);
        session.save(account);
    }

    private static void formula(Session session) {
        Account account = (Account) session.get(Account.class, 1L);
        LOGGER.info("$$$ : " + account.getYearEndBonus()); // read-only
    }

    private static void testCase4(Session session) {
//        Account account = (Account) session.get(Account.class, 1L);
//        session.delete(account);

        Query query = session.getNamedQuery(Role.SQL_ROLE_FIND_BY_ROLE_NAME);
        query.setParameter("roleName", "ROLE_USER");
        Role user = (Role) query.uniqueResult();
        session.delete(user);
    }

    @SuppressWarnings("unchecked")
    private static void testNaturalId(Session session) {
        checkNotNull(session, "Illegal argument passed: Session object cannot be null");
        List<Account> accounts = session.createCriteria(Account.class)
                .add(Restrictions.naturalId().set("username", "oscarwei")).list();
        if (CollectionUtils.isNotEmpty(accounts)) {
            LOGGER.info("Natural-Id searching size : " + accounts.size());
            Account account = accounts.get(0);
            LOGGER.info("Account email : " + account.getEmail());
        }
    }

    @SuppressWarnings("unchecked")
    private static void testFullText(Session session) {
        FullTextSession textSession = Search.getFullTextSession(session);
        QueryBuilder queryBuilder = textSession.getSearchFactory().buildQueryBuilder()
                .forEntity(Account.class).get();
        org.apache.lucene.search.Query luceneQuery = queryBuilder
                .keyword().onFields("username")
                .matching("oscarwei").createQuery();
        Query query = textSession.createFullTextQuery(luceneQuery, Account.class);
        List<Account> accounts = query.list();
        if (CollectionUtils.isNotEmpty(accounts)) {
            LOGGER.info("Full-Text searching size : " + accounts.size());
            Account account = accounts.get(0);
            LOGGER.info("Account email : " + account.getEmail());
            account.getToDoSet().stream().map(ToDo::getSubject).forEach(System.out::println);
        }
    }

    private static Credit createCredit(Session session) {
        Credit credit = new Credit();
        credit.setName("Normal");
        credit.setDescription("A normal credit...");
        credit.setUserCreated("DTS");
        credit.setDateCreated(new Date());
        session.persist(credit);
        return credit;
    }

    private static void accountSummary(Session session) {
        Query query = session.createQuery("FROM AccountSummary");
        List<AccountSummary> list = query.list();
        for (AccountSummary summary : list) {
            System.out.println("View: " + summary.getFirstName() + " : " + summary.getSalary());
        }
    }
}
