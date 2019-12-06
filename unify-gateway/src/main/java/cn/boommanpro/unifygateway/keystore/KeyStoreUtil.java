package cn.boommanpro.unifygateway.keystore;

import java.io.*;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author wangqimeng
 * @date 2019/10/15 13:36
 */
@Slf4j
public class KeyStoreUtil {

    private KeyStoreUtil() {

    }

    public static void saveCertificate(String cerNameAlias,
                                       InputStream certIn,
                                       String passphrase) throws Exception {
        log.debug("程序进行证书导入工作");
        final char sep = File.separatorChar;
        File dir = new File(System.getProperty("java.home") + sep + "lib" + sep + "security");
        log.debug("导入证书路径:{}", dir);
        char[] passphraseArray = passphrase.toCharArray();
        OutputStream out = null;
        File targetKeyStore = new File(dir, "cacerts");
        //输入流和输出流不可同时在同一文件,否则文件会被置空
        try (InputStream localCertIn = new FileInputStream(targetKeyStore)) {

            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(localCertIn, passphraseArray);
            //判断是否已经存在在该证书
            if (keystore.containsAlias(cerNameAlias)) {
                log.debug("已经存在该证书:{},先删除,再导入", cerNameAlias);
                keystore.deleteEntry(cerNameAlias);
            }
            BufferedInputStream bis = new BufferedInputStream(certIn);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                Certificate cert = cf.generateCertificate(bis);
                keystore.setCertificateEntry(cerNameAlias, cert);
            }
            out = new FileOutputStream(targetKeyStore);
            keystore.store(out, passphraseArray);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("输出流关闭失败");
                }
            }
        }
    }

    public static void doKeyStore(ResourceLoader resourceLoader,
                                  String[] certificatePath, String certificateFileName,
                                  String certificateNameAlias, String passphrase) throws Exception {
        try (InputStream inputStream = getResource(resourceLoader, certificatePath, certificateFileName).getInputStream()) {
            KeyStoreUtil.saveCertificate(certificateNameAlias,
                    inputStream, passphrase);
        }
    }

    private static Resource getResource(ResourceLoader resourceLoader, String[] certificatePath, String fileName) {
        for (int i = certificatePath.length - 1; i >= 0; i--) {
            Resource resource = resourceLoader.getResource(certificatePath[i] + fileName);
            if (resource.exists()) {
                return resource;
            }
        }
        throw new RuntimeException(fileName + "资源未找到");
    }
}
