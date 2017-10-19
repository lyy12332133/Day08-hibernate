package com.lanou.test;

import com.lanou.domain.Category;
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
public class ManyToManyBothTest {
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
     * 双向n-n, 保存数据时以inverse为主动方的对象进行级联保存
     */
    @Test
    public void save(){
        Category category = new Category("办公软件");
        Category category1 = new Category("办公用品");
        Category category2 = new Category("教学部用品");
        Item item = new Item("WPS");
        Item item1 = new Item("金山毒霸");
        // 关联关系
        category.getItems().add(item);
        category.getItems().add(item1);

//        item1.getCategories().add(category1);
//        item1.getCategories().add(category2);

        category1.getItems().add(item1);
        category2.getItems().add(item1);
        // 保存数据
        session.save(category);
        session.save(category1);
        session.save(category2);
    }

    /**
     * 删除数据
     * 2. 在进行删除还有引用, 即在中间表中还包含该对象的索引时, 只能删除inverse=false的对象,
     * 删除不了, inverse=true的对象, inverse为true放弃了中间表的主动权
     */
    @Test
    public void delete(){
        // 先删主动方, 分类对象
//        Category category = session.get(Category.class,1);
//        session.delete(category);
        // 删除被动方, 有关联关系的Item对象
        Item item = session.get(Item.class,2);
        session.delete(item);
    }
}
