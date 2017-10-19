package com.lanou.test;

import com.lanou.domain.Category;
import com.lanou.domain.Item;
import com.lanou.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by dllo on 17/10/19.
 */
public class ManyToManyTest {

//    {
//        Configuration configuration = new Configuration();
//        configuration.configure();
//        SessionFactory sessionFactory = configuration.buildSessionFactory();
//        session = sessionFactory.openSession();
//        transaction = session.beginTransaction();
//    }
    private Session session;
    private Transaction transaction;

    @Before
    public void init(){
        session = HibernateUtil.getSession();
        transaction = session.beginTransaction();
    }
    @After
    public void destroy(){
        transaction.commit();
        HibernateUtil.closeSession();
    }

    /**
     * 保存数据
     */
    @Test
    public void save(){
        Category category = new Category("办公用品");
        Item item = new Item("签字笔");
        Item item1 = new Item("打印机");
        // 关联关系
        category.getItems().add(item);
        category.getItems().add(item1);
        session.save(category);
    }

    /**
     * 删除数据
     * 1. 删除关联的集合中的某个对象时, 需要先解除关联关系, 再session.delete
     */
    @Test
    public void delete(){
        Category category = session.get(Category.class,1);
        // 1.先删除该分类下的某一个item
        Set<Item> items = category.getItems();
        Iterator<Item> itemIterator = items.iterator();
        Item firstItem = itemIterator.next();
        // 解除关联
        category.getItems().remove(firstItem);
        session.delete(firstItem);
        session.update(category);
    }
    /**
     * 删除分类
     * 2. 删除设置了set关联关系的对象时, 如果cascade级联没有包含delete级联,
     * 则只删除本对象, 以及中间表中本对象id的数据: 不会删除级联的对方对象
     */
    @Test
    public void deleteCategory(){
        Category category = session.get(Category.class, 2);
        session.delete(category);
    }

    @Test
    public void find(){
        Category category = new Category("厨房用品");
        Item item = new Item("拖布");
        // 建立关联关系
        category.getItems().add(item);
        // 保存对象
        session.save(category);
        Query query = session.createQuery("from Category where name=:na");
        // 设置参数
        query.setParameter("na","厨房用品");
        // 返回查询的结果集合
        List<Category> categories = query.list();
        for (Category category1 : categories) {
            System.out.println("基础信息: " + category1);
            // 遍历item集合
            Iterator<Item> itemIterator = category1.getItems().iterator();
            while (itemIterator.hasNext()){
                System.out.println("分类信息: " + itemIterator.next());

            }
        }
    }
}
