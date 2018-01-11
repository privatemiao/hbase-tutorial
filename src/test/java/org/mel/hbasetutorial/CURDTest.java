package org.mel.hbasetutorial;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CURDTest {
    //    表名
    static final TableName TABLE_NAME = TableName.valueOf("2018");
    //    列簇
    static final byte[] FAMILY_PAYLOAD = Bytes.toBytes("p");

    //    瞬时电压
    static final byte[] FIELD_VOLTAGE = Bytes.toBytes("v");
    //    当前累计电量
    static final byte[] FIELD_CAPACITY = Bytes.toBytes("c");
    //    采集时间
    static final byte[] FIELD_COLLECT_TIME = Bytes.toBytes("t");

    Configuration config = HBaseConfiguration.create();

    Connection connection;
    Admin admin;

    @Before
    public void setUp() {
        config.set("hbase.zookeeper.quorum", "hbase.example.com");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        try {
            HBaseAdmin.checkHBaseAvailable(config);
        } catch (Exception e) {
            System.err.println("HBase is not running. " + e.getMessage());
        }

        try {
            connection = ConnectionFactory.createConnection(config);
            admin = connection.getAdmin();
        } catch (IOException e) {
            System.err.println("Can not connection HBase. " + e.getMessage());
        }
    }

    @Test
    public void testCreateTable() {
        HTableDescriptor desc = new HTableDescriptor(TABLE_NAME);
        desc.addFamily(new HColumnDescriptor(FAMILY_PAYLOAD));
        try {
            admin.createTable(desc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteTable() {
        try {
            if (admin.tableExists(TABLE_NAME)) {
                admin.disableTable(TABLE_NAME);
                admin.deleteTable(TABLE_NAME);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() throws IOException {
        Table table = connection.getTable(TABLE_NAME);

        byte[] rowId = generateRowId();
        Put put = new Put(rowId);
        put.addImmutable(FAMILY_PAYLOAD, FIELD_VOLTAGE, Bytes.toBytes("220"));
        put.addImmutable(FAMILY_PAYLOAD, FIELD_CAPACITY, Bytes.toBytes("1500"));
        put.addImmutable(FAMILY_PAYLOAD, FIELD_COLLECT_TIME, Bytes.toBytes(new Date().getTime()));
        table.put(put);

        try {

            admin.disableTable(TABLE_NAME);
            HColumnDescriptor desc = new HColumnDescriptor(rowId);
            admin.addColumn(TABLE_NAME, desc);
        } finally {
            admin.enableTable(TABLE_NAME);
        }
    }

    private byte[] generateRowId() {
        return Bytes.toBytes(String.format("0001%s",
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
    }

    @Test
    public void testScan() throws IOException {
        Table table = connection.getTable(TABLE_NAME);

        Scan scan = new Scan();
        scan.addColumn(FAMILY_PAYLOAD, FIELD_VOLTAGE);

        try (ResultScanner scanner = table.getScanner(scan)) {
            for (Result result : scanner) {
                System.out.println("Found row: " + result);
            }

        }
    }

    @Test
    public void testFilter() throws IOException {
        Table table = connection.getTable(TABLE_NAME);

//        Filter filter = new PrefixFilter(Bytes.toBytes("0001201801"));
        Scan scan = new Scan();
//        scan.setFilter(filter);
        scan.setStartRow(Bytes.toBytes("000120180111000000"));
        scan.setStopRow(Bytes.toBytes("000120180112000000"));
        try (ResultScanner scanner = table.getScanner(scan)) {
            for (Result result : scanner) {
                System.out.println(">>" + result);
            }
        }
    }

    @After
    public void destroy() {
        try {
            if (admin != null) {
                admin.close();
            }
        } catch (Exception e) {

        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {

        }
    }
}
