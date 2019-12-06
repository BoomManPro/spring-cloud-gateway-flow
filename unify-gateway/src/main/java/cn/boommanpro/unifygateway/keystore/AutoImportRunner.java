package cn.boommanpro.unifygateway.keystore;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;

/**
 * @author wangqimeng
 * @date 2019/10/15 11:15
 */
@Slf4j
public class AutoImportRunner implements ApplicationRunner {

    @Autowired
    private CertificateProperties certificateProperties;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        KeyStoreUtil.doKeyStore(resourceLoader, certificateProperties.getCertificatePath(), certificateProperties.getCertificateFileName(),
                certificateProperties.getCertificateNameAlias(), certificateProperties.getPassphrase());
    }

}
