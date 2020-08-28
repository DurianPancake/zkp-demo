package cn.durian.api_demo;



import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author Liuluhao
 * @Date 2020/8/28
 */
@Slf4j
public class ZookeeperDemo {

    private static final int SESSION_TIME_OUT = 10000;
    // ZooKeeper服务的地址，如为集群，多个地址用逗号分隔
    private static final String CONNECT_STRING = "10.68.77.8:2181";
    private static final String ZNODE_PATH = "/zk_demo";
    private static final String ZNODE_PATH_PARENT = "/Server";
    private static final String ZNODE_PATH_CHILDREN = "/Server/grpc";

    private ZooKeeper zk = null;

    @Before
    public void init() throws IOException {
        zk = new ZooKeeper(CONNECT_STRING, SESSION_TIME_OUT,
                event -> System.out.println("已经触发了" + event.getType() + "事件！"));
    }

    @Test
    public void testCreate() throws KeeperException, InterruptedException {
        zk.create(ZNODE_PATH, "anna2019".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void testCreateParentZnode() throws KeeperException, InterruptedException {
        zk.create(ZNODE_PATH_PARENT, "anna2019".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void testCreateChildrenZnode() throws KeeperException, InterruptedException {
        zk.create(ZNODE_PATH_CHILDREN, "anna2020".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void testCreateChildrenZnode2() throws KeeperException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            int i1 = new Random().nextInt(100);
            zk.create(String.format("%s%d", ZNODE_PATH_CHILDREN, i1), String.format("anna2020_%d", i1).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    @Test
    public void testGet2() throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren(ZNODE_PATH_PARENT, false);
        for (String child : children) {
            log.info(child);
        }
    }

    @Test
    public void testGet() throws KeeperException, InterruptedException {
        byte[] data1 = zk.getData(ZNODE_PATH, false, null);
        byte[] data2 = zk.getData(ZNODE_PATH_PARENT, false, null);
        byte[] data3 = zk.getData(ZNODE_PATH_CHILDREN, false, null);
        log.info("{}的信息：{}", ZNODE_PATH, new String(data1) );
        log.info("{}的信息：{}", ZNODE_PATH_PARENT, new String(data2) );
        log.info("{}的信息：{}", ZNODE_PATH_CHILDREN, new String(data3) );
    }


    /**
     *  删除
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void testDelete() throws KeeperException, InterruptedException {
        // 指定要删除的版本，-1表示删除所有版本
        zk.delete(ZNODE_PATH, -1);
    }

    /**
     *  删除含有子节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void testDeleteHasChildrenZnode() throws KeeperException, InterruptedException {
        // 指定要删除的版本，-1表示删除所有版本
        zk.delete(ZNODE_PATH_PARENT, -1);
    }

}
