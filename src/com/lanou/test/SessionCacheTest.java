package com.lanou.test;

import com.lanou.domain.Item;
import com.lanou.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dllo on 17/10/19.
 */
public class SessionCacheTest {
    /**
     * 一级缓存, session级别的缓存
     */
    private Session session;
    private Transaction transaction;

    @Before
    public void init(){
        session = HibernateUtil.getSession();
        transaction = session.beginTransaction();
    }
//    @After
//    public void destroy(){
//        transaction.commit();
//        HibernateUtil.closeSession();
//    }

    @Test
    public void find(){
        long startTime = System.currentTimeMillis();
        Item item = session.get(Item.class,1);
        Item item1 = session.get(Item.class,2);
        Item item2 = session.get(Item.class,3);
        Item item3 = session.get(Item.class,4);
        long endTime = System.currentTimeMillis();
        System.out.println("时间为: "+(endTime-startTime));
    }

    /**
     * 当缓存中的数据发生变化时, 会在下一次事物提交时将修改后的数据同步到数据库中
     */
    @Test
    public void update(){
        // 从数据库查询
        Item item = session.get(Item.class,1);
        // 从一级缓存中获取
        Item item1 = session.get(Item.class,1);
        // 修改缓存中的数据
        item1.setName("修改后的数据");
    }

    /**
     * 数据库中的数据同步到缓存中
     */
    @Test
    public void update2(){
        Item item = session.get(Item.class,1);
        System.out.println(item);
        // 先睡10秒
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("睡醒之后的:" + item);
         Item item1 = this.session.get(Item.class,1);
        System.out.println("重新获取" + item1);

    }
}
