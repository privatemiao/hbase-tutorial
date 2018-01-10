
           ______________________________________
  ________|                                      |_______
  \       |                HBase                 |      /
   \      |             2018-01-10               |     /
   /      |______________________________________|     \
  /__________)                                (_________\


HBase
	hadoop-2.6.0-cdh5.12.2
	hbase-1.2.0-cdh5.12.2
	
192.168.137.20	node1.example.com	node1
192.168.137.30	node2.example.com	node2
192.168.137.40	node3.example.com	node3


node1	NameNode/DataNode	ResourceManager/NodeManager
node2	DataNode	NodeManager
node3	DataNode	NodeManager

Hadoop
    core-site.xml
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://node1.example.com:8020</value>
        </property>

        <property>
            <name>hadoop.tmp.dir</name>
            <value>/root/data/hadoop/tmp/hadoop-root</value>
        </property>

    hdfs-site.xml
        <property>
            <name>dfs.namenode.name.dir</name>
            <value>/root/data/hadoop/name</value>
        </property>
        <property>
            <name>dfs.datanode.data.dir</name>
            <value>/root/data/hadoop/data</value>
        </property>

    yarn-site.xml
        <property>
            <name>yarn.nodemanager.aux-services</name>
            <value>mapreduce_shuffle</value>
        </property>
        <property>
            <name>yarn.resourcemanager.hostname</name>
            <value>node1</value>
        </property>

    mapred-site.xml
        <property>
            <name>mapreduce.framework.name</name>
            <value>yarn</value>
        </property>

    slaves
            node1
            node2
            node3

    对 NameNode 格式化：只在主节点上执行
    hdfs namenode -format

    启动 只在主节点上运行：
    sbin/start-all.sh

    验证 WEBUI
    node1:50070
    node1:8088
		