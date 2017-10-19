package com.lanou.test;

import com.lanou.domain.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dllo on 17/10/19.
 */
public class SecondCacheTest {

    private SessionFactory sessionFactory;

    @Before
    public void init() {
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
    }
    @Test
    public void find(){
        Session session = sessionFactory.openSession();
        // 第一次访问, 从数据库查询, 并存储到二级缓存中
        Item item = session.get(Item.class,1);
        System.out.println("第一次: "+item);
        session.close(); // 关闭第一个session
        Session session1 = sessionFactory.openSession();
        Item item1 = session1.get(Item.class,1);
        System.out.println("第二次: "+item1);
        session1.close();
        System.out.println(sessionFactory.getStatistics().getEntityLoadCount());

    }

    /**
     * save方法不操作二级缓存
     */
    @Test
    public void save(){
        Session session = sessionFactory.openSession();
        Item item = new Item("测试数据");
        session.save(item);
        session.close();
        System.out.println(sessionFactory.getStatistics().getEntityLoadCount());
    }
}
