
           ______________________________________
  ________|                                      |_______
  \       |                HBase                 |      /
   \      |             2018-01-10               |     /
   /      |______________________________________|     \
  /__________)                                (_________\


机器配置
	hadoop-2.6.0-cdh5.12.2
	hbase-1.2.0-cdh5.12.2
	
    192.168.137.10	node1.example.com	node1
    192.168.137.20	node2.example.com	node2
    192.168.137.30	node3.example.com	node3
	192.168.137.40	node4.example.com	node4




node1	NameNode	ResourceManager
node2	DataNode	NodeManager
node3	DataNode	NodeManager
node4	DataNode	NodeManager

Hadoop 安装
    1、core-site.xml
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://node1.example.com:8020</value>
        </property>

        <property>
            <name>hadoop.tmp.dir</name>
            <value>/root/data/hadoop/tmp/hadoop-root</value>
        </property>
        ______________________________________________________
        node1: 主节点机器名

    2、hdfs-site.xml
        <property>
            <name>dfs.namenode.name.dir</name>
            <value>/root/data/hadoop/name</value>
        </property>
        <property>
            <name>dfs.datanode.data.dir</name>
            <value>/root/data/hadoop/data</value>
        </property>
        ______________________________________________________

    3、mapred-site.xml
        <property>
            <name>mapreduce.framework.name</name>
            <value>yarn</value>
        </property>
        ______________________________________________________

    4、yarn-site.xml
        <property>
            <name>yarn.nodemanager.aux-services</name>
            <value>mapreduce_shuffle</value>
        </property>
        <property>
            <name>yarn.resourcemanager.hostname</name>
            <value>node1</value>
        </property>
        ______________________________________________________
        node1 主的 resourcemanager 机器名

    5、slaves
            node2
            node3
			node4
        ______________________________________________________
        node2、ndoe3、ndoe4 从节点

    对 NameNode 格式化：只在主节点上执行
    hdfs namenode -format

    启动 只在主节点上运行：
    start-dfs.sh
    start-yarn.sh

    验证 WEBUI
    node1:50070
    node1:8088

HBase
    主要进程
        Master
        RegionServer

    表结构
        Column Family       必须确定
        column              可以动态增加
        ____________________________________________________
        |  个人信息           教育经历           工作经历     | ----------->  Column Family 列簇
        ----------------------------------------------------
        |姓名 年龄 电话    大学 研究生 博士   工作1 工作2 工作3| ------------>  根据数据增加，可以不设定
        ----------------------------------------------------
        |___________________________________________________|
        |___________________________________________________|

    安装


