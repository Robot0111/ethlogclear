package com.ethlogclear;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

/**
 * Hello world!
 *
 */
public class App 
{
	
	/**
	 * 清空文件
	 * @param fileName
	 */
	public static void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**复制文件并根据时间重命名
     * @param fileName
     * @throws IOException 
     */
    public static void copyFile(String srcfileName ,File desfile) throws IOException {
    	File src = new File(srcfileName);
    	if(!src.exists() || !desfile.exists()) {
    		throw new RuntimeException("文件不存在！");
    	}
    	FileUtils.copyFile(src, desfile);
    }
    
    public static void main( String[] args )
    {
    	 // 上海时间
        ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime shanghaiZonedDateTime = ZonedDateTime.now(shanghaiZoneId);
        //上海当前时间
        String formatter = shanghaiZonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
       
        Properties properties = new Properties();
        InputStream in = App.class.getClassLoader().getResourceAsStream("conf.properties");     
        try {
			properties.load(in);
			String srcFile = properties.getProperty("srcfileName");
			String desDir = properties.getProperty("desfileName");
			File desFile = new File(desDir+formatter);
			if(!desFile.exists()) {
				desFile.createNewFile();
			}
			if(srcFile != null && desFile != null) {
				//复制文件
				App.copyFile(srcFile, desFile);
				//清空文件
				App.clearInfoForFile(srcFile);
				//判断文件总数是否大于10 如果大于10 删除数字最小的文件
				File desDirFile = new File(desDir);
				if(desDirFile.isDirectory()&& desDirFile.list().length > 10) {
					List<String> fileName = new ArrayList<String>(Arrays.asList(desDirFile.list()));
					Collections.sort(fileName);
					FileUtils.forceDelete(new File(desDir+fileName.get(0)));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
