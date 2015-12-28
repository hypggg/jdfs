package testSpring;

import net.jdfs.dfs.server.fieupload.HttpUploadServer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zookeeper.ZookeeperFrameWork;
/**
 * 
 * @author Administrator
 * 
 * 打印虚拟机加载了哪些类-XX:+TraceClassLoading -XX:+TraceClassUnloading
 *
 */
public class BeanTest {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("jdfsServer-spring.xml");
		/*ZookeeperFrameWork zFrameWork = (ZookeeperFrameWork)context.getBean("zookeeperClient");
		 try {
			byte[] b=zFrameWork.client.getData().forPath("/");
			System.out.println(new String(b));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		HttpUploadServer uploadServer = (HttpUploadServer)context.getBean("httpUploadServer");
	}
}
