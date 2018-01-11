package org.mel.hbasetutorial;

import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class HBaseTest {
    private Configuration config;
    TableName table;
    String family1;
    String family2;

    @Before
    public void init() throws IOException, ServiceException {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "hbase.example.com");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        HBaseAdmin.checkHBaseAvailable(config);

        table = TableName.valueOf("Table1");
        family1 = "Family1";
        family2 = "Family2";
    }

    @Test
    public void createTable() throws IOException {
        System.out.println("Create table");

//        获得连接
        Connection connection = ConnectionFactory.createConnection(config);
        Admin admin = connection.getAdmin();

        HTableDescriptor desc = new HTableDescriptor(table);
        desc.addFamily(new HColumnDescriptor(family1));
        desc.addFamily(new HColumnDescriptor(family2));
        admin.createTable(desc);
    }

    @Test
    public void addElement() {
        byte[] row1 = Bytes.toBytes("row1");
        Put p = new Put(row1);
    }
}
