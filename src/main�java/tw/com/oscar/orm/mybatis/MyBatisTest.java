package tw.com.oscar.orm.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import tw.com.oscar.orm.hibernate.domain.Credit;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by oscarwei168 on 2015/6/25.
 */
public class MyBatisTest {

    public static void main(String[] args) throws IOException {
        /** Process configuration **/
        String resource = "Configuration.xml";
        SqlSession sqlSession = null;
        try {
            Reader reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

            /** Obtain SqlSession object **/
            sqlSession = sqlMapper.openSession();
            Credit credit = sqlSession.selectOne("tw.com.oscar.orm.mybatis.mapper.CreditMapper.selectCredit", new
                    Long(1));
            System.out.println(credit);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != sqlSession) {
                sqlSession.close();
            }
            sqlSession = null;
        }

    }
}
