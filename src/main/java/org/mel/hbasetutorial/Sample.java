package org.mel.hbasetutorial;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class Sample {
    public static void main(String[] args) {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "hbase.example.com");
        config.set("hbase.zookeeper.property.clientPort", "2181");
    }

    protected void createTable() {

    }
}
