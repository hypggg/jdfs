package net.jdfs.dfs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 不通过配置 ，没法配置文件路径在类加载时就传入进来，所以暂时要通过new 对象。
 * 尝试自定义类的加载 ，在类加载前设置 属性
 * @author Administrator
 *
 */
public class PropertiesUtil {

	// 配置文件路径
	private static String[] paths;
	private static Properties prop = new Properties();
	private static PropertiesUtil propUtil ;
	
	
	private PropertiesUtil(String[] paths) {
		super();
		this.paths = paths;
		init();
	}
	
	
	public static PropertiesUtil getInstance(String[] paths){
		if(propUtil==null){
			propUtil = new PropertiesUtil(paths);
		}
		return propUtil;
	}
	// 配置初始化
	private void init() {
	 
		for (String path : paths) {
			InputStream in = PropertiesUtil.class
					.getResourceAsStream(path);
			try {
				prop.load(in);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getValus(String key){
		return (String)prop.get(key);
	}
	
	public static void main(String[] args) throws Exception{
		//带/ 表示classPath根路径 
		String[] paths ={"/jdfs.conf","/test.conf"};
		PropertiesUtil.getInstance(paths);
		System.out.println(PropertiesUtil.getValus("jdfs.file.savePath"));
		System.out.println(PropertiesUtil.getValus("a"));
	}
}
