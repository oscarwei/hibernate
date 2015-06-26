package tw.com.oscar.orm.mybatis.mapper;

import org.apache.ibatis.annotations.Select;
import tw.com.oscar.orm.hibernate.domain.Credit;

/**
 * <p>
 * Title: CreditMapper
 * </p>
 * <strong>Description:</strong> //TODO <br>
 * This function include: - <br>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: Oscar Wei Inc.
 * </p>
 *
 * @author ${USER_NAME}
 * @version v1, 2015/6/26
 * @since 2015/6/26
 */
public interface CreditMapper {

    @Select("SELECT * FROM CREDIT WHERE PID = ${pid}")
    Credit selectCredit(Long pid);
}
