//
//import com.github.tobato.fastdfs.service.TrackerClient;
//import com.xuecheng.filesystem.FileSystemApplication;
//import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
//import com.xuecheng.framework.exception.ExceptionCast;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
///**
// * @author Administrator
// * @version 1.0
// **/
//@SpringBootTest(classes = FileSystemApplication.class)
//@RunWith(SpringRunner.class)
//public class TestFastDFS {
//
//
////    @Value("${xuecheng.fastdfs.tracker_servers}")
////    String tracker_servers;
////    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
////    int connect_timeout_in_seconds;
////    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
////    int network_timeout_in_seconds;
////    @Value("${xuecheng.fastdfs.charset}")
////    String charset;
//
//    //初始化fastDFS环境
//    private void initFdfsConfig(){
//        //初始化tracker服务地址（多个tracker中间以半角逗号分隔）
//        try {
//            ClientGlobal.initByTrackers("47.114.157.42:22122");
//            ClientGlobal.setG_charset("UTF-8");
//            ClientGlobal.setG_network_timeout(30);
//            ClientGlobal.setG_connect_timeout(5);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //抛出异常
//            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
//        }
//    }
//
//    //上传文件
//    @Test
//    public void testUpload(){
//
//        try {
//            initFdfsConfig();
//            //定义TrackerClient，用于请求TrackerServer
//            TrackerClient trackerClient = new TrackerClient();
//            //连接tracker
//            TrackerServer trackerServer = trackerClient.getConnection();
//            //获取Stroage
//            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
//            //创建stroageClient
//            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
//            //向stroage服务器上传文件
//            //本地文件的路径
//            String filePath = "G:\\image\\5bd6cc17-a9bc-4d6f-86ac-a54613ef3920.jpg";
//            //上传成功后拿到文件Id
//            String fileId = storageClient1.upload_file1(filePath, "jpg", null);
//            System.out.println(fileId);
//            //group1/M00/00/01/wKhlQVuhU3eADb4pAAAawU0ID2Q159.png
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    //下载文件
//    @Test
//    public void testDownload(){
//        try {
//            //加载fastdfs-client.properties配置文件
//            ClientGlobal.initByProperties("config/fastdfs-client.properties");
//            //定义TrackerClient，用于请求TrackerServer
//            TrackerClient trackerClient = new TrackerClient();
//            //连接tracker
//            TrackerServer trackerServer = trackerClient.getConnection();
//            //获取Stroage
//            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
//            //创建stroageClient
//            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
//            //下载文件
//            //文件id
//            String fileId = "group1/M00/00/01/wKhlQVuhU3eADb4pAAAawU0ID2Q159.png";
//            byte[] bytes = storageClient1.download_file1(fileId);
//            //使用输出流保存文件
//            FileOutputStream fileOutputStream = new FileOutputStream(new File("c:/logo.png"));
//            fileOutputStream.write(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (MyException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//}
