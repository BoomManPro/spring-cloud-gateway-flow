package cn.boommanpro.unifygateway.keystore;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * @author wangqimeng
 * @date 2019/10/15 11:06
 */
@Data
@Validated
public class CertificateProperties {

    private String[] certificatePath;

    private String certificateNameAlias;

    private String certificateFileName;

    private String passphrase = "changeit";

    private boolean autoImportCertificate;

}
