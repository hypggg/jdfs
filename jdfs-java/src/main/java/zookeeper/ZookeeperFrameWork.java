package zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperFrameWork {
	private String connectString;
	public static CuratorFramework client;
	
	
	
	public ZookeeperFrameWork(String connectString) {
		super();
		this.connectString = connectString;
	}



	private void initZookeeper(){
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		 client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
		client.start();
	}
	
	private void destoryZookeeper(){
		client.close();
	}
}
