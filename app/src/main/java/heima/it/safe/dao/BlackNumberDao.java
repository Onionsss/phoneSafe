package heima.it.safe.dao;

import java.util.List;

import heima.it.safe.bean.BlackNumber;

/**
 * 作者：张琦 on 2016/5/23 18:50
 */
public interface BlackNumberDao {
    /**
     * 插入一个黑名单对象
     * @param bn
     * @return
     */
    boolean insert(BlackNumber bn);

    /**
     * 删除一个黑名单对象
     * @param bn
     * @return
     */
    boolean delete(BlackNumber bn);

    /**
     * 更新一个黑名单对象
     * @param bn
     * @return
     */
    boolean update(BlackNumber bn);

    /**
     * 查找一个黑名单
     */
    BlackNumber find(BlackNumber bn);

    List<BlackNumber> findAll();

    List<BlackNumber> findPart(int limit, int offset);

    String findMode(String phone);
}
